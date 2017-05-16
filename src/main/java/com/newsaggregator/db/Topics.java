package com.newsaggregator.db;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
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
            LabelHolder labelHolder = getLabelHolderFromIterator(iterator, articleManager, summaryManager);
            if (labelHolder != null) return labelHolder;
        } catch (Exception e) {
            logger.error("An error occurred whilst getting a single topic", e);
        }
        return null;
    }

    public LabelHolder getTopicById(String id) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("_id", new ObjectId(id));
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            Articles articleManager = new Articles(database);
            Summaries summaryManager = new Summaries(database);
            LabelHolder labelHolder = getLabelHolderFromIterator(iterator, articleManager, summaryManager);
            if (labelHolder != null) return labelHolder;
        } catch (Exception e) {
            logger.error("An error occurred whilst getting a single topic", e);
        }
        return null;
    }

    private LabelHolder getLabelHolderFromIterator(MongoCursor<Document> iterator, Articles articleManager, Summaries summaryManager) {
        if (iterator.hasNext()) {
            Document document = iterator.next();
            String item = document.toJson();
            JSONObject jsonObject = new JSONObject(item);
            List<OutletArticle> articles = new ArrayList<>();
            try {
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getJSONObject(i).getString("$oid");
                    articles.add(articleManager.getArticleFromId(articleId));
                }
            } catch (Exception e) {
                logger.error("An error occurred whilst getting articles for a single topic", e);
            }
            List<ClusterHolder> clusters = new ArrayList<>();
            try {
                JSONArray summaryIds = jsonObject.getJSONArray("Clusters");
                for (int i = 0; i < summaryIds.length(); i++) {
                    String summaryId = summaryIds.getJSONObject(i).getString("$oid");
                    clusters.add(summaryManager.getSingleCluster(summaryId));
                }
            } catch (Exception e) {
                logger.error("An error occurred whilst getting clusters for a single topic", e);
            }
            LabelHolder labelHolder = new LabelHolder(jsonObject.getString("Label"), articles, clusters);
            labelHolder.set_id(document.getObjectId("_id"));
            labelHolder.setImageUrl(document.getString("imageUrl"));
            return labelHolder;
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

    public LabelHolder createBlankTopic(String title) {
        LabelHolder labelHolder = new LabelHolder(title, new ArrayList<>(), new ArrayList<>());
        Document document = labelHolder.createDocument();
        collection.insertOne(document);
        return labelHolder;
    }

    public void removeTopics(List<LabelHolder> topics) {
        List<ObjectId> objectIds = topics.stream().map(LabelHolder::get_id).collect(Collectors.toList());
        BasicDBList list = new BasicDBList();
        list.addAll(objectIds);
        BasicDBObject inClause = new BasicDBObject("$in", list);
        BasicDBObject query = new BasicDBObject("_id", inClause);
        collection.deleteMany(query);
    }

    public void saveTopic(LabelHolder labelHolder) {
        try {
            Document document = labelHolder.createDocument();
            collection.replaceOne(new BasicDBObject().append("_id", labelHolder.get_id()), document);
        } catch (Exception e) {
            logger.error("Updating user error", e);
        }
    }

    public List<LabelHolder> getClusteringTopics() {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("NeedsClustering", true);
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            Articles articleManager = new Articles(database);
            Summaries summaryManager = new Summaries(database);
            List<LabelHolder> labelHolders = new ArrayList<>();
            while (iterator.hasNext()) {
                Document document = iterator.next();
                String item = document.toJson();
                JSONObject jsonObject = new JSONObject(item);
                List<OutletArticle> articles = new ArrayList<>();
                try {
                    JSONArray articleIds = jsonObject.getJSONArray("Articles");
                    for (int i = 0; i < articleIds.length(); i++) {
                        String articleId = articleIds.getJSONObject(i).getString("$oid");
                        articles.add(articleManager.getArticleFromId(articleId));
                    }
                } catch (Exception e) {
                    logger.error("An error occurred whilst getting articles for a single topic", e);
                }
                List<ClusterHolder> clusters = new ArrayList<>();
                try {
                    JSONArray summaryIds = jsonObject.getJSONArray("Clusters");
                    for (int i = 0; i < summaryIds.length(); i++) {
                        String summaryId = summaryIds.getJSONObject(i).getString("$oid");
                        clusters.add(summaryManager.getSingleCluster(summaryId));
                    }
                } catch (Exception e) {
                    logger.error("An error occurred whilst getting clusters for a single topic", e);
                }
                LabelHolder labelHolder = new LabelHolder(jsonObject.getString("Label"), articles, clusters);
                labelHolder.set_id(document.getObjectId("_id"));
                labelHolders.add(labelHolder);
            }
            return labelHolders;
        } catch (Exception e) {
            logger.error("An error occurred whilst getting a single topic", e);
        }
        return null;
    }

    public void updateTopics(List<LabelHolder> labelHolders) {
        for (LabelHolder labelHolder : labelHolders) {
            Document document = labelHolder.createDocument();
            collection.replaceOne(new BasicDBObject().append("_id", labelHolder.get_id()), document);
        }
    }
}
