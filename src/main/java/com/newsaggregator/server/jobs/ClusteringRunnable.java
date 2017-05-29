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
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class ClusteringRunnable implements Runnable {

    private List<String> labelStrings;

    public ClusteringRunnable(List<String> labelStrings) {
        this.labelStrings = labelStrings;
    }


    @Override
    public void run() {

        Logger logger = Logger.getLogger(getClass());

        Utils.printActiveThreads();

        MongoDatabase db = Utils.getDatabase();
        Topics topics = new Topics(db);
        Summaries summaries = new Summaries(db);

        try {

            int counter = 0;

            labelStrings = labelStrings.stream().distinct().collect(Collectors.toList());

            List<LabelHolder> labelHolders = new ArrayList<>();
            for (String labelString : labelStrings) {
                LabelHolder labelHolder = topics.getTopic(labelString);
                labelHolders.add(labelHolder);
            }

            List<ClusterHolder> clusters = labelHolders.stream().map(LabelHolder::getClusters).filter(Objects::nonNull).collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList());

            for (LabelHolder labelHolder : labelHolders) {
                try {
                    counter++;
                    logger.info("Clustering " + counter + " of " + labelHolders.size());
                    List<ClusterHolder> brandNewClusters = new ArrayList<>();
                    if (labelHolder.getArticles().size() > 0) {
                        logger.info("Label id:" + labelHolder.getId());
                        Clusterer clusterer;
                        if (labelHolder.getClusters().size() > 0) {
                            clusterer = new Clusterer(labelHolder.getClusters(), labelHolder.getArticles());
                        } else {
                            clusterer = new Clusterer(labelHolder.getArticles());
                        }
                        labelHolder.setClusters(new ArrayList<>());
                        List<Cluster<ArticleVector>> newClusters = clusterer.cluster();
                        for (Cluster<ArticleVector> cluster : newClusters) {
                            try {
                                List<OutletArticle> articles = cluster.getClusterItems().stream().filter(Objects::nonNull).map(ArticleVector::getArticle).filter(Objects::nonNull).collect(Collectors.toList());
                                if (clusters.stream().noneMatch(clusterHolder -> clusterHolder.sameCluster(articles))) {
                                    ClusterHolder clusterHolder = new ClusterHolder(articles);
                                    Set<OutletArticle> clusterArticles = new HashSet<>(articles);
                                    logger.info("Summarising");
                                    Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
                                    List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
                                    List<Summary> summs = extractives.stream().map(Extractive::summarise).filter(Objects::nonNull).collect(Collectors.toList());
                                    logger.info("Summarising");
                                    clusterHolder.setSummary(summs);
                                    clusters.add(clusterHolder);
                                    brandNewClusters.add(clusterHolder);
                                    labelHolder.addCluster(clusterHolder);
                                    summaries.saveSummaries(Lists.newArrayList(clusterHolder));
                                    topics.saveTopic(labelHolder);
                                } else {
                                    labelHolder.addCluster(clusters.stream().filter(clusterHolder -> clusterHolder.sameCluster(articles)).findAny().get());
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

    }

}

