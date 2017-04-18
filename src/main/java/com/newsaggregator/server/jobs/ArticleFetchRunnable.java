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
import com.newsaggregator.server.ArticleFetch;
import com.newsaggregator.server.LabelHolder;
import com.newsaggregator.server.LabelString;

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
        System.out.println("Fetching articles");
        List<OutletArticle> articleList = ArticleFetch.fetchArticles();
        DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
        Articles articleManager = new Articles(db);
        Topics topicManager = new Topics(db);
        articleList = articleManager.articlesToAdd(articleList);
        System.out.println("Sending articles");
        System.out.println("Starting topic modelling and labelling");
        List<OutletArticle> allArticles = articleManager.getAllArticles();
        Map<String, LabelString> allLabels = topicManager.getAllTopics();
        Map<String, LabelHolder> topicLabelMap = new HashMap<>();

        for (Map.Entry<String, LabelString> label : allLabels.entrySet()) {
            String labelName = label.getKey();
            LabelString labelString = label.getValue();
            List<OutletArticle> articles = new ArrayList<>();
            for (String url : labelString.getArticles()) {
                articles.add(allArticles.stream().filter(art -> art.getArticleUrl().equals(url)).findFirst().get());
            }
            List<List<OutletArticle>> clusters = new ArrayList<>();
            for (List<String> strings : labelString.getClusters()) {
                List<OutletArticle> cluster = new ArrayList<>();
                for (String string : strings) {
                    cluster.add(allArticles.stream().filter(art -> art.getArticleUrl().equals(string)).findFirst().get());
                }
                clusters.add(cluster);
            }
            topicLabelMap.put(labelName, new LabelHolder(labelName, labelString.getSummaries(), articles, clusters));
        }

        try {
            TopicModelling topicModelling = new TopicModelling();
            if (allArticles.size() == 0) {
                allArticles = articleList;
            }
            topicModelling.trainTopics(allArticles);
            for (OutletArticle article : articleList) {
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Completed topic labelling.");
            System.out.println("Starting clustering and summarising");
            int counter = 0;
            for (Map.Entry<String, LabelHolder> topicLabel : topicLabelMap.entrySet()) {
                try {
                    System.out.println("Clustering topic " + counter + " out of " + topicLabelMap.size());
                    List<OutletArticle> articles = topicLabel.getValue().getArticles();
                    Clusterer clusterer = new Clusterer(articles);
                    List<Cluster<ArticleVector>> clusters = clusterer.cluster();
                    for (Cluster<ArticleVector> cluster : clusters) {
                        try {
                            List<String> clusterUrls = cluster.getClusterItems().stream().map(vector -> vector.getArticle().getArticleUrl()).collect(Collectors.toList());
                            if (topicLabel.getValue().alreadyClustered(clusterUrls)) {
                                continue;
                            }
                            System.out.println("Summarising cluster " + clusters.indexOf(cluster) + " out of " + clusters.size());
                            List<OutletArticle> articlesForSummary = cluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
                            Extractive extractive = new Extractive(articlesForSummary);
                            Summary summary = extractive.summarise();
                            topicLabel.getValue().addCluster(articlesForSummary);
                            topicLabel.getValue().addSummary(summary);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                counter++;
            }
            topicManager.saveTopics(topicLabelMap);
            articleManager.saveArticles(articleList);
            System.out.println("Articles and topics saved");
        } catch (Exception e) {
            Utils.sendExceptionEmail(e);
            e.printStackTrace();
        }

    }

}
