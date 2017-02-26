package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.*;
import com.newsaggregator.base.Topic;
import com.newsaggregator.base.TopicLabel;
import com.newsaggregator.base.TopicWord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Topics {

    private final Table table;
    private DynamoDB database;

    public Topics(DynamoDB database) {
        this.database = database;
        this.table = getCollection();
    }

    public void saveTopics(List<TopicLabel> topics) {
        for (TopicLabel topic : topics) {
            try {
                List<Map> words = topic.getTopic().generateWordMap();
                table.putItem(new Item().withPrimaryKey("Label", topic.getLabel())
                        .withList("Words", words));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<TopicLabel> getAllTopics() {
        List<TopicLabel> topicLabels = new ArrayList<>();
        ItemCollection<ScanOutcome> items = table.scan();
        for (Item nextItem : items) {
            try {
                Map<String, Object> item = nextItem.asMap();
                String label = (String) item.get("Label");
                List<Map<String, Object>> words = (List<Map<String, Object>>) item.get("Words");
                List<TopicWord> wordList = new ArrayList<>();
                for (Map word : words) {
                    BigDecimal decimal = (BigDecimal) word.get("distribution");
                    wordList.add(new TopicWord((String) word.get("word"), decimal.doubleValue()));
                }
                topicLabels.add(new TopicLabel(label, new Topic(wordList)));
            } catch (Exception e) {
                System.out.println("Caught an exception, but still chugging along");
            }
        }
        return topicLabels;
    }

    private Table getCollection() {
        return database.getTable("Topics");
    }
}
