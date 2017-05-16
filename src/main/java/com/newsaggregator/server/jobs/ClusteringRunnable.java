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

        try {

            MongoDatabase db = Utils.getDatabase();
            Summaries summaries = new Summaries(db);
            Topics topics = new Topics(db);

            List<LabelHolder> labelHolders = topics.getClusteringTopics();


            List<ClusterHolder> clusters = labelHolders.stream().map(LabelHolder::getClusters).collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList());

            for (LabelHolder labelHolder : labelHolders) {
                labelHolder.setClusters(new ArrayList<>());
                Clusterer clusterer = new Clusterer(labelHolder.getArticles());
                List<Cluster<ArticleVector>> newClusters = clusterer.cluster();
                for (Cluster<ArticleVector> cluster : newClusters) {
                    List<OutletArticle> clusterArticles = cluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
                    if (clusters.stream().noneMatch(clusterHolder -> clusterHolder.sameCluster(clusterArticles))) {
                        ClusterHolder clusterHolder = new ClusterHolder(clusterArticles);
                        clusters.add(clusterHolder);
                        labelHolder.addCluster(clusterHolder);
                    } else {
                        labelHolder.addCluster(clusters.stream().filter(clusterHolder -> clusterHolder.sameCluster(clusterArticles)).findAny().get());
                    }
                }
                labelHolder.setNeedsClustering(false);
            }

            summaries.updateSummaries(clusters);

            topics.updateTopics(labelHolders);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

