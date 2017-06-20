package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.base.ArticleHolder;
import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.HomeArticles;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.clustering.Cluster;
import com.newsaggregator.ml.clustering.Clusterer;
import com.newsaggregator.ml.nlp.apache.NLPSingleton;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.*;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class ClusteringRunnable implements Runnable {

    private List<String> labelStrings;
    private Map<String, OutletArticle> articleMap = new HashMap<>();
    private boolean oldArticles;
    private int limit = 10;
    private Comparator<ClusterHolder> byLastPublished = (left, right) -> {
        ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));

        SimpleDateFormat formatter = simpleDateFormatThreadLocal.get();
        try {

            int ldidx = left.getLastPublished().indexOf(".");
            int rdidx = right.getLastPublished().indexOf(".");

            String ld = left.getLastPublished();
            String rd = right.getLastPublished();

            if (ldidx != -1) {
                ld = ld.substring(0, ldidx).concat("+0000");
            } else {
                ld = ld.replaceAll("Z$", "+0000");
            }
            if (rdidx != -1) {
                rd = rd.substring(0, rdidx).concat("+0000");
            } else {
                rd = rd.replaceAll("Z$", "+0000");
            }

            ld = ld.replace("+00:00", "+0000");
            rd = rd.replace("+00:00", "+0000");


            Date leftDate = formatter.parse(ld);
            Date rightDate = formatter.parse(rd);

            return leftDate.compareTo(rightDate);

        } catch (Exception e) {
            Logger.getLogger(getClass()).error("Error in last published", e);
        }
        return 1;
    };

    public ClusteringRunnable(List<String> labelStrings, List<OutletArticle> articleList, boolean oldArticles) {
        this.labelStrings = labelStrings;
        this.oldArticles = oldArticles;
        articleList.forEach(a -> articleMap.put(a.getId(), a));
    }

    @Override
    public void run() {

        Logger logger = Logger.getLogger(getClass());

        Utils.printActiveThreads();

        MongoDatabase db = Utils.getDatabase();
        Topics topics = new Topics(db);
        Summaries summaries = new Summaries(db);

        int counter = 0;

        List<ArticleHolder> newSummaries = new ArrayList<>();

        try {


            labelStrings = labelStrings.stream().distinct().collect(Collectors.toList());

            List<LabelHolder> labelHolders = new ArrayList<>();
            for (String labelString : labelStrings) {
                LabelHolder labelHolder = topics.getTopic(labelString);
                labelHolders.add(labelHolder);
            }

            List<String> clusters = labelHolders.stream().map(LabelHolder::getClusters).filter(Objects::nonNull).collect(Collectors.toList()).stream().flatMap(Collection::stream).map(ClusterString::getId).distinct().collect(Collectors.toList());
            HashMap<String, ClusterHolder> chmap = summaries.getClusters(clusters);
            List<ClusterHolder> chlist = new ArrayList<>(chmap.values());

            for (LabelHolder labelHolder : labelHolders) {
                try {
                    counter++;
                    logger.info("Clustering " + counter + " of " + labelHolders.size());
                    List<ClusterHolder> brandNewClusters = new ArrayList<>();
                    if (labelHolder.getArticles().size() > 0) {
                        logger.info("Label id:" + labelHolder.getId());
                        Clusterer clusterer;
                        List<OutletArticle> articleList = labelHolder.getArticles().stream().map(a -> articleMap.get(a.getId())).collect(Collectors.toList());
                        if (labelHolder.getClusters().size() > 0) {
                            List<ClusterHolder> clusterHolders = labelHolder.getClusters().stream().map(c -> chmap.get(c.getId())).collect(Collectors.toList());
                            if (clusterHolders.size() > limit) {
                                List<String> articleIds = labelHolder.getArticles().stream().map(ArticleString::getId).collect(Collectors.toList());
                                List<OutletArticle> articles = clusterHolders.stream().map(ClusterHolder::getArticles).flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toList());
                                List<String> usedIds = articles.stream().map(OutletArticle::getId).collect(Collectors.toList());
                                articleIds = articleIds.stream().filter(a -> !usedIds.contains(a)).distinct().collect(Collectors.toList());
                                clusterHolders = clusterHolders.stream().sorted(byLastPublished.reversed()).collect(Collectors.toList());
                                List<ClusterHolder> fixedClusters = new ArrayList<>(clusterHolders.subList(limit, clusterHolders.size()));
                                clusterHolders = clusterHolders.stream().limit(limit).collect(Collectors.toList());
                                labelHolder.setClusters(new ArrayList<>());
                                labelHolder.addClusters(fixedClusters);
                                articles = clusterHolders.stream().map(ClusterHolder::getArticles).flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toList());
                                articleIds.addAll(articles.stream().map(OutletArticle::getId).distinct().collect(Collectors.toList()));
                                List<OutletArticle> outletArticles = articleIds.stream().map(articleMap::get).collect(Collectors.toList());

                                clusterer = new Clusterer(clusterHolders, outletArticles);
                            } else {
                                clusterer = new Clusterer(clusterHolders, articleList);
                            }
                        } else {
                            clusterer = new Clusterer(articleList);
                            labelHolder.setClusters(new ArrayList<>());
                        }
                        List<Cluster<ArticleVector>> newClusters = clusterer.cluster();
                        for (Cluster<ArticleVector> cluster : newClusters) {
                            try {
                                List<OutletArticle> articles = cluster.getClusterItems().stream().filter(Objects::nonNull).map(ArticleVector::getArticle).filter(Objects::nonNull).collect(Collectors.toList());
                                if (chlist.stream().noneMatch(clusterHolder -> clusterHolder.sameCluster(articles))) {
                                    ClusterHolder clusterHolder = new ClusterHolder(articles);
                                    Set<OutletArticle> clusterArticles = new HashSet<>(articles);
                                    logger.info("Summarising");
                                    Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
                                    List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
                                    List<Summary> summs = extractives.stream().map(Extractive::summarise).filter(Objects::nonNull).collect(Collectors.toList());
                                    logger.info("Summarising");
                                    clusterHolder.setSummary(summs);
                                    chlist.add(clusterHolder);
                                    brandNewClusters.add(clusterHolder);
                                    summaries.saveSummaries(Lists.newArrayList(clusterHolder));
                                    labelHolder.addCluster(clusterHolder);
                                    topics.saveTopic(labelHolder);
                                } else {
                                    labelHolder.addCluster(chlist.stream().filter(clusterHolder -> clusterHolder.sameCluster(articles)).findAny().get());
                                    topics.saveTopic(labelHolder);
                                }
                            } catch (Exception e) {
                                logger.error("Caught an error saving an individual cluster", e);
                            }
                        }
                    }
                    labelHolder.setNeedsClustering(false);
                    if (brandNewClusters.size() > 0) {
                        logger.info("Saving clusters");
//                        summaries.saveSummaries(brandNewClusters);
                        List<String> categories = Wikipedia.getCategories(labelHolder.getLabel());
                        for (String cat : categories) {
                            LabelHolder catLabelHolder = topics.getTopic(cat);
                            if (catLabelHolder == null) {
                                catLabelHolder = new LabelHolder(cat, labelHolder.getArticles(), labelHolder.getClusters());
                            } else {
                                catLabelHolder.addClusters(brandNewClusters);
                            }
                            topics.saveTopic(catLabelHolder);
                        }
//                    summaryClusters.addAll(brandNewClusters);
                    }
                    newSummaries.addAll(brandNewClusters.stream().map(b -> new ArticleHolder(labelHolder.getId(), b.getClusterString())).collect(Collectors.toList()));
                    topics.saveTopic(labelHolder);
                } catch (Exception e) {
                    logger.error("Caught an exception clustering within a topic but will continue", e);
                }
            }


        } catch (Exception e) {
            logger.error("Caught an exception clustering", e);
        }

        NLPSingleton.removeInstance();

        TaskServiceSingleton.getInstance().execute(new SendEmailRunnable(labelStrings, counter));

        logger.info("Completed " + counter + " topics out of " + labelStrings.size());

        if (oldArticles) {
            TaskServiceSingleton.getInstance().schedule(new LabellingRunnable(), 1L, TimeUnit.MINUTES);
        } else {
            HomeArticles homeArticles = new HomeArticles(Utils.getDatabase());
            homeArticles.saveHomeArticles(newSummaries);
        }

    }

}

