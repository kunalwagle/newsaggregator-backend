package com.newsaggregator.server.jobs;

import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.clustering.Cluster;
import com.newsaggregator.ml.clustering.Clusterer;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class ClusteringRunnable implements Runnable {


    @Override
    public void run() {

        Logger logger = Logger.getLogger(getClass());

        try {

            MongoDatabase db = Utils.getDatabase();
            Summaries summaries = new Summaries(db);
            Topics topics = new Topics(db);

            List<LabelHolder> labelHolders = topics.getClusteringTopics().stream().limit(15).collect(Collectors.toList());


            List<ClusterHolder> clusters = labelHolders.stream().map(LabelHolder::getClusters).collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList());
            List<ClusterHolder> brandNewClusters = new ArrayList<>();

            int counter = 1;
            for (LabelHolder labelHolder : labelHolders) {
                logger.info("Clustering " + counter + " of " + labelHolders.size());
                logger.info("Number of articles: " + labelHolder.getArticles().size());
                if (labelHolder.getArticles().size() > 0) {
                    labelHolder.setClusters(new ArrayList<>());
                    Clusterer clusterer = new Clusterer(labelHolder.getArticles());
                    List<Cluster<ArticleVector>> newClusters = clusterer.cluster();
                    logger.info("Clustering " + counter + " of " + labelHolders.size());
                    for (Cluster<ArticleVector> cluster : newClusters) {
                        List<OutletArticle> clusterArticles = cluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
                        if (clusters.stream().noneMatch(clusterHolder -> clusterHolder.sameCluster(clusterArticles))) {
                            logger.info("1 Clustering " + counter + " of " + labelHolders.size());
                            ClusterHolder clusterHolder = new ClusterHolder(clusterArticles);
                            clusters.add(clusterHolder);
                            brandNewClusters.add(clusterHolder);
                            labelHolder.addCluster(clusterHolder);
                        } else {
                            logger.info("2 Clustering " + counter + " of " + labelHolders.size());
                            labelHolder.addCluster(clusters.stream().filter(clusterHolder -> clusterHolder.sameCluster(clusterArticles)).findAny().get());
                        }
                    }
                }
                labelHolder.setNeedsClustering(false);
                counter++;
            }

            if (brandNewClusters.size() > 0) {
                logger.info("Saving clusters");
                summaries.saveSummaries(brandNewClusters);
            }

            topics.updateTopics(labelHolders);


        } catch (Exception e) {
            logger.error("An Error in the Clustering Runnable", e);
        }

    }

}

