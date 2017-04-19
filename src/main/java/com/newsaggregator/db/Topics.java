package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.LabelHolder;
import com.newsaggregator.server.LabelString;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
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
        int counter = 1;
        for (Map.Entry<String, LabelHolder> topic : topics.entrySet()) {
            try {
                logger.info("Saving topic " + counter + " out of " + topics.size());
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
                logger.error("Saving topics error", e);
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
                            logger.error("Error reading topic", e);
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

    public LabelString getTopic(String label) {
        try {
            QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression("Id = :v_id")
                    .withValueMap(new ValueMap()
                            .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1"));

            ItemCollection<QueryOutcome> items = table.query(spec);

            Iterator<Item> iterator = items.iterator();
            Item item = null;
            while (iterator.hasNext()) {
                item = iterator.next();
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
                        logger.error("Error reading topic", e);
                    }
                }
                for (String cluster : clusters) {
                    try {
                        finalClusters.add(objectMapper.readValue(cluster, List.class));
                    } catch (Exception e) {
                        logger.error("Got an error, but can continue getting topic", e);
                    }
                }

                return new LabelString(articles, finalClusters, summs);
            }
        } catch (Exception e) {
            logger.error("An error occurred whilst getting a single topic", e);
        }
        return null;
    }

    private Table getCollection() {
        return database.getTable("Topics");
    }
}
