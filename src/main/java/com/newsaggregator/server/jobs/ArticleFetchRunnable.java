package com.newsaggregator.server.jobs;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.newsaggregator.Utils;
import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.clustering.Cluster;
import com.newsaggregator.ml.clustering.Clusterer;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 21/02/2017.
 */
public class ArticleFetchRunnable implements Runnable {

    @Override
    public void run() {
        Logger logger = Logger.getLogger(getClass());
        try {
            logger.info("Fetching articles");
            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
            Articles articleManager = new Articles(db);
            Topics topicManager = new Topics(db);
            logger.info(db.listTables());
            List<OutletArticle> articleList = ArticleFetch.fetchArticles();
            logger.info("Getting database articles");
            List<OutletArticle> allArticles = articleManager.getAllArticles();
            logger.info("Getting database topics");
            List<OutletArticle> finalAllArticles = allArticles;
            articleList.removeIf(art -> finalAllArticles.stream().anyMatch(a -> a.getArticleUrl().equals(art.getArticleUrl())));
            Map<String, LabelString> allLabels = topicManager.getAllTopics();
            Map<String, LabelHolder> topicLabelMap = new HashMap<>();
            logger.info("Starting topic modelling and labelling");

            for (Map.Entry<String, LabelString> label : allLabels.entrySet()) {
                try {
                    String labelName = label.getKey();
                    LabelString labelString = label.getValue();
                    List<OutletArticle> articles = new ArrayList<>();
                    for (String url : labelString.getArticles()) {
                        articles.add(allArticles.stream().filter(art -> art.getArticleUrl().equals(url)).findFirst().get());
                    }
                    List<ClusterHolder> clusters = new ArrayList<>();
                    for (ClusterString clusterString : labelString.getClusters()) {
                        List<OutletArticle> a = new ArrayList<>();
                        for (String string : clusterString.getCluster()) {
                            a.add(allArticles.stream().filter(art -> art.getArticleUrl().equals(string)).findFirst().get());
                        }
                        clusters.add(new ClusterHolder(a, clusterString.getNodes()));
                    }
                    topicLabelMap.put(labelName, new LabelHolder(labelName, articles, clusters));
                } catch (Exception e) {
                    logger.error("Error populating Topic Label Map", e);
                }
            }

            try {
                TopicModelling topicModelling = new TopicModelling();
                if (allArticles.size() < 150) {
                    allArticles = articleList;
                }
                topicModelling.trainTopics(allArticles);
                for (OutletArticle article : articleList) {
                    try {
                        int count = articleList.indexOf(article) + 1;
                        logger.info("Modelling and labelling article " + count + " out of " + articleList.size());
                        Topic topic = topicModelling.getModel(article);
                        List<String> topicLabels = TopicLabelling.generateTopicLabel(topic, article);
                        for (String topicLabel : topicLabels) {
                            if (topicLabelMap.containsKey(topicLabel)) {
                                topicLabelMap.get(topicLabel).addArticle(article);
                            } else {
                                LabelHolder newLabelHolder = new LabelHolder(topicLabel);
                                newLabelHolder.addArticle(article);
                                topicLabelMap.put(topicLabel, newLabelHolder);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("An error", e);
                        e.printStackTrace();
                    }
                }
                logger.info("Completed topic labelling.");
                logger.info("Starting clustering and summarising");

                int counter = 1;
                List<ClusterHolder> clusterHolderList = new ArrayList<>();
                for (Map.Entry<String, LabelHolder> topicLabel : topicLabelMap.entrySet()) {
                    logger.info("Clustering topic " + counter + " out of " + topicLabelMap.size());
                    List<OutletArticle> articles = topicLabel.getValue().getArticles();
                    Clusterer clusterer = new Clusterer(articles);
                    List<Cluster<ArticleVector>> clusters = clusterer.cluster();
                    for (Cluster<ArticleVector> cluster : clusters) {
                        try {
                            List<OutletArticle> clusterArticles = cluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
                            if (clusterHolderList.stream().anyMatch(c -> c.sameCluster(clusterArticles))) {
                                ClusterHolder clusterHolder = clusterHolderList.stream().filter(c -> c.sameCluster(clusterArticles)).findFirst().get();
                                clusterHolder.addLabel(topicLabel.getKey());
                            } else {
                                if (!topicLabel.getValue().clusterExists(cluster)) {
                                    ClusterHolder clusterHolder = new ClusterHolder(clusterArticles);
                                    clusterHolder.addLabel(topicLabel.getKey());
                                    clusterHolderList.add(clusterHolder);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Adding cluster error", e);
                        }
                    }
                    counter++;
                }

                counter = 1;
                for (ClusterHolder clusterHolder : clusterHolderList) {
                    try {
                        logger.info("Summarising cluster " + counter + " out of " + clusterHolderList.size());
                        Extractive extractive = new Extractive(clusterHolder.getArticles());
                        Summary summary = extractive.summarise();
                        clusterHolder.setSummary(summary.getNodes());
                        for (String topicLabel : clusterHolder.getLabels()) {
                            LabelHolder labelHolder = topicLabelMap.get(topicLabel);
                            labelHolder.addCluster(clusterHolder);
                        }
                    } catch (Exception e) {
                        logger.error("Summarisation error", e);
                    }
                    counter++;
                }
                logger.info("Writing Topics");
                topicManager.saveTopics(topicLabelMap);
                logger.info("Topics saved");
                logger.info("Writing Articles");
                articleManager.saveArticles(articleList);
                logger.info("Articles saved");
            } catch (Exception e) {
                Utils.sendExceptionEmail(e);
                logger.error("Error. Email sent", e);
            }
        } catch (Exception e) {
            logger.error("An error", e);
        }
    }

}
