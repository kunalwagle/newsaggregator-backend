package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.base.TopicLabel;
import com.newsaggregator.base.TopicWord;
import com.newsaggregator.server.LabelHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Topics {

    private final Table table;
    private DynamoDB database;

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

                String articleString = objectMapper.writeValueAsString(labelHolder.getArticles());

//                List<Map> words = topic.getTopic().generateWordMap();
//                table.putItem(new Item().withPrimaryKey("Label", topic.getLabel())
//                        .withList("Words", words));
            } catch (Exception e) {
                //e.printStackTrace();
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
