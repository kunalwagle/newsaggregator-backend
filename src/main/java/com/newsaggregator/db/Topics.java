package com.newsaggregator.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Topics {

    private final MongoCollection<Document> collection;
    private MongoDatabase database;
    private Logger logger = Logger.getLogger(getClass());

    public Topics(MongoDatabase database) {
        this.database = database;
        this.collection = getCollection();
    }

    public void saveTopics(Collection<LabelHolder> topics) {
        List<Document> documents = topics.stream().map(LabelHolder::createDocument).collect(Collectors.toList());
        collection.insertMany(documents);
    }

    public List<LabelHolder> getAllTopics() {
        List<LabelHolder> labelHolders = new ArrayList<>();
        Articles articleManager = new Articles(database);
        Summaries summaryManager = new Summaries(database);
        try {
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                Document document = iterator.next();
                String item = document.toJson();
                JSONObject jsonObject = new JSONObject(item);
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                List<OutletArticle> articles = new ArrayList<>();
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getJSONObject(i).getString("$oid");
                    articles.add(articleManager.getArticleFromId(articleId));
                }
                JSONArray summaryIds = jsonObject.getJSONArray("Clusters");
                List<ClusterHolder> clusters = new ArrayList<>();
                for (int i = 0; i < summaryIds.length(); i++) {
                    String summaryId = summaryIds.getJSONObject(i).getString("$oid");
                    clusters.add(summaryManager.getSingleCluster(summaryId));
                }
                LabelHolder labelHolder = new LabelHolder(jsonObject.getString("Label"), articles, clusters);
                labelHolder.set_id(document.getObjectId("_id"));
                labelHolders.add(labelHolder);
            }
        } catch (Exception e) {
            logger.error("Caught an exception", e);
        }
        logger.info("About to return topics");
        return labelHolders;
    }

    public LabelHolder getTopic(String label) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("Label", label);
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            Articles articleManager = new Articles(database);
            Summaries summaryManager = new Summaries(database);
            if (iterator.hasNext()) {
                Document document = iterator.next();
                String item = document.toJson();
                JSONObject jsonObject = new JSONObject(item);
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                List<OutletArticle> articles = new ArrayList<>();
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getString(i);
                    articles.add(articleManager.getArticleFromId(articleId));
                }
                JSONArray summaryIds = jsonObject.getJSONArray("Clusters");
                List<ClusterHolder> clusters = new ArrayList<>();
                for (int i = 0; i < summaryIds.length(); i++) {
                    String summaryId = summaryIds.getString(i);
                    clusters.add(summaryManager.getSingleCluster(summaryId));
                }
                LabelHolder labelHolder = new LabelHolder(jsonObject.getString("Label"), articles, clusters);
                labelHolder.set_id(document.getObjectId("_id"));
                return labelHolder;
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

    public long count() {
        return collection.count();
    }
}
