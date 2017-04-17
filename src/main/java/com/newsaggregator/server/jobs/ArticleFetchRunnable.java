package com.newsaggregator.server.jobs;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.db.Articles;
import com.newsaggregator.ml.clustering.Cluster;
import com.newsaggregator.ml.clustering.Clusterer;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.ArticleFetch;
import com.newsaggregator.server.LabelHolder;

import java.io.IOException;
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
        System.out.println("Fetching articles");
        List<OutletArticle> articleList = ArticleFetch.fetchArticles();
        DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
        Articles articleManager = new Articles(db);
        articleList = articleManager.articlesToAdd(articleList);
        System.out.println("Sending articles");
        articleManager.saveArticles(articleList);
        System.out.println("Articles saved");
        System.out.println("Starting topic modelling and labelling");
        List<OutletArticle> allArticles = articleManager.getAllArticles();
        TopicModelling topicModelling = new TopicModelling();
        Map<String, LabelHolder> topicLabelMap = new HashMap<>();
        try {
            topicModelling.trainTopics(allArticles);
            for (OutletArticle article : articleList) {
                System.out.println("Modelling and labelling article " + articleList.indexOf(article) + " out of " + articleList.size());
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
            }
            System.out.println("Completed topic labelling.");
            System.out.println("Starting clustering and summarising");
            int counter = 0;
            for (Map.Entry<String, LabelHolder> topicLabel : topicLabelMap.entrySet()) {
                System.out.println("Clustering topic " + counter + " out of " + topicLabelMap.size());
                List<OutletArticle> articles = topicLabel.getValue().getArticles();
                Clusterer clusterer = new Clusterer(articles);
                List<Cluster<ArticleVector>> clusters = clusterer.cluster();
                for (Cluster<ArticleVector> cluster : clusters) {
                    System.out.println("Summarising cluster " + clusters.indexOf(cluster) + " out of " + clusters.size());
                    List<OutletArticle> articlesForSummary = cluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
                    Extractive extractive = new Extractive(articlesForSummary);
                    Summary summary = extractive.summarise();
                    topicLabel.getValue().addSummary(summary);
                }
                counter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
