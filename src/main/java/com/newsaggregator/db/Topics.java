package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.ClusterString;
import com.newsaggregator.server.LabelHolder;
import com.newsaggregator.server.LabelString;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

public class Topics {

    private final MongoCollection<Document> collection;
    private MongoDatabase database;
    private Logger logger = Logger.getLogger(getClass());

    public Topics(MongoDatabase database) {
        this.database = database;
        this.collection = getCollection();
    }

    public void saveTopics(Map<String, LabelHolder> topics) {
        int counter = 1;
        for (Map.Entry<String, LabelHolder> topic : topics.entrySet()) {
            try {
                logger.info("Saving topic " + counter + " out of " + topics.size());
                LabelHolder labelHolder = topic.getValue();

                List<String> articleUrls = labelHolder.getArticles().stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());

                ObjectMapper objectMapper = new ObjectMapper();

                List<ClusterString> clusters = labelHolder.getClusters().stream().map(ClusterHolder::convertToClusterString).collect(Collectors.toList());

                List<String> clusterStrings = new ArrayList<>();

                for (ClusterString clusterString : clusters) {
                    try {
                        clusterStrings.add(objectMapper.writeValueAsString(clusterString));
                    } catch (Exception e) {
                        logger.error("Logged an error adding clusters to topic. Fine to continue", e);
                    }
                }

                table.putItem(new Item().withPrimaryKey("Label", topic.getKey())
                        .withList("Articles", articleUrls)
                        .withList("Clusters", clusterStrings)
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
                    List<ClusterString> finalClusters = new ArrayList<>();
                    ObjectMapper objectMapper = new ObjectMapper();
                    for (String cluster : clusters) {
                        finalClusters.add(objectMapper.readValue(cluster, ClusterString.class));
                    }

                    LabelString labelString = new LabelString(articles, finalClusters);
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
                    .withKeyConditionExpression("Label = :v_id")
                    .withValueMap(new ValueMap()
                            .withString(":v_id", label));

            ItemCollection<QueryOutcome> items = table.query(spec);

            Iterator<Item> iterator = items.iterator();
            Item item;
            while (iterator.hasNext()) {
                item = iterator.next();
                List<String> articles = (List<String>) item.get("Articles");
                List<String> clusters = (List<String>) item.get("Clusters");
                List<ClusterString> finalClusters = new ArrayList<>();
                ObjectMapper objectMapper = new ObjectMapper();
                for (String cluster : clusters) {
                    finalClusters.add(objectMapper.readValue(cluster, ClusterString.class));
                }
                return new LabelString(articles, finalClusters);
            }
        } catch (Exception e) {
            logger.error("An error occurred whilst getting a single topic", e);
        }
        return null;
    }

    private MongoCollection<Document> getCollection() {
        MongoCollection<Document> topicCollection = database.getCollection("topics", Document.class);

        if (topicCollection == null) {
            database.createCollection("topics");
            topicCollection = database.getCollection("topics", Document.class);
        }
        return topicCollection;
    }
}
