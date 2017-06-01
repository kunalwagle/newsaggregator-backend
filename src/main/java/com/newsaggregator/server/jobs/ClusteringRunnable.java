package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.clustering.Cluster;
import com.newsaggregator.ml.clustering.Clusterer;
import com.newsaggregator.ml.nlp.apache.NLPSingleton;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.ClusterString;
import com.newsaggregator.server.LabelHolder;
import com.newsaggregator.server.TaskServiceSingleton;
import org.apache.log4j.Logger;

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

        try {


            labelStrings = labelStrings.stream().distinct().collect(Collectors.toList());

            List<LabelHolder> labelHolders = new ArrayList<>();
            for (String labelString : labelStrings) {
                LabelHolder labelHolder = topics.getTopic(labelString);
                labelHolders.add(labelHolder);
            }

            List<String> clusters = labelHolders.stream().map(LabelHolder::getClusters).filter(Objects::nonNull).collect(Collectors.toList()).stream().flatMap(Collection::stream).map(ClusterString::getId).distinct().collect(Collectors.toList());
            HashMap<String, ClusterHolder> chmap = summaries.getClusters(clusters);
            Collection<ClusterHolder> chlist = chmap.values();

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
                            clusterer = new Clusterer(clusterHolders, articleList);
                        } else {
                            clusterer = new Clusterer(articleList);
                        }
                        labelHolder.setClusters(new ArrayList<>());
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
        }

    }

}

