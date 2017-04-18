package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.LabelHolder;
import com.newsaggregator.server.LabelString;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Topics {

    private final Table table;
    private DynamoDB database;
    private Logger logger = Logger.getLogger(getClass());

    public Topics(DynamoDB database) {
        this.database = database;
        this.table = getCollection();
    }

    public void saveTopics(Map<String, LabelHolder> topics) {
        for (Map.Entry<String, LabelHolder> topic : topics.entrySet()) {
            try {

                LabelHolder labelHolder = topic.getValue();

                List<String> articleUrls = labelHolder.getArticles().stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());

                ObjectMapper objectMapper = new ObjectMapper();

                List<String> articleClusters = new ArrayList<>();
                for (List<OutletArticle> articles : labelHolder.getClusters()) {
                    List<String> urls = articles.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
                    articleClusters.add(objectMapper.writeValueAsString(urls));
                }

                List<String> articleSummaries = new ArrayList<>();
                for (Summary summary : labelHolder.getSummaries()) {
                    articleSummaries.add(objectMapper.writeValueAsString(summary));
                }

                table.putItem(new Item().withPrimaryKey("Label", topic.getKey())
                        .withList("Articles", articleUrls)
                        .withList("Clusters", articleClusters)
                        .withList("Summaries", articleSummaries)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, LabelString> getAllTopics() {
        Map<String, LabelString> topicLabels = new HashMap<>();
        try {
            logger.info("Starting to scan database");
            ItemCollection<ScanOutcome> items = table.scan();
            logger.info("Scanned. Got " + items.getAccumulatedItemCount() + " items");
            for (Item nextItem : items) {
                try {
                    Map<String, Object> item = nextItem.asMap();
                    String label = (String) item.get("Label");
                    List<String> articles = (List<String>) item.get("Articles");
                    List<String> clusters = (List<String>) item.get("Clusters");
                    List<String> summaries = (List<String>) item.get("Summaries");
                    List<List<String>> finalClusters = new ArrayList<>();
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Summary> summs = new ArrayList<>();
                    for (String sum : summaries) {
                        try {
                            summs.add(objectMapper.readValue(sum, Summary.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    for (String cluster : clusters) {
                        finalClusters.add(objectMapper.readValue(cluster, List.class));
                    }

                    LabelString labelString = new LabelString(articles, finalClusters, summs);
                    topicLabels.put(label, labelString);

                } catch (Exception e) {
                    logger.error("Caught an exception", e);
                }
            }
        } catch (Exception e) {
            logger.error("Caught an exception", e);
        }
        logger.info("About to return topics");
        return topicLabels;
    }

    private Table getCollection() {
        return database.getTable("Topics");
    }
}
