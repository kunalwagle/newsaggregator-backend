package com.newsaggregator.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.newsaggregator.server.ArticleString;
import com.newsaggregator.server.ClusterString;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
        List<Document> documents = topics.stream().map(LabelHolder::createDocument).filter(Objects::nonNull).collect(Collectors.toList());
        collection.insertMany(documents);
    }

    public List<LabelHolder> getAllTopics() {
        List<LabelHolder> labelHolders = new ArrayList<>();
        Articles articleManager = new Articles(database);
        Summaries summaryManager = new Summaries(database);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                Document document = iterator.next();
                String item = document.toJson();
                JSONObject jsonObject = new JSONObject(item);
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                List<ArticleString> articles = new ArrayList<>();
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getJSONObject(i).getString("$oid");
                    articles.add(articleManager.getArticleFromId(articleId).getArticleString());
                }
                JSONArray summaryIds = jsonObject.getJSONArray("Clusters");
                List<ClusterString> clusters = new ArrayList<>();
                for (int i = 0; i < summaryIds.length(); i++) {
                    String summary = summaryIds.getString(i);
                    clusters.add(objectMapper.readValue(summary, ClusterString.class));
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

    public String topicExists(String label) {
        try {
            BasicDBObject match = new BasicDBObject("Label", label);
            Document doc = collection.find(match).projection(Projections.include("_id")).limit(1).first();
            if (doc != null) {
                return doc.getObjectId("_id").toHexString();
            }
        } catch (Exception e) {
            logger.error("An error occurred whilst getting a blank topic", e);
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

    public LabelHolder getPaginatedTopic(String id, String page) {
        try {
            List<ClusterString> clusterStrings = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            int total = getArticleCount(id);
            int start = Integer.parseInt(page) * 10 - 9;
            int end = 10;
            if (start + end > total) {
                end = total;
            }
            BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("_id", new ObjectId(id)));
            BasicDBList slice = new BasicDBList();
            slice.add("$Clusters");
            slice.add(start);
            slice.add(end);
            BasicDBObject projection = new BasicDBObject("_id", 0).append("Label", 1).append("Clusters", new BasicDBObject("$slice", slice));
            BasicDBObject project = new BasicDBObject("$project", projection);
            List<BasicDBObject> dbObjects = Lists.newArrayList(match, project);
            MongoCursor<Document> docs = collection.aggregate(dbObjects).iterator();
            String label = "";
            while (docs.hasNext()) {
                Document document = docs.next();
                label = document.getString("Label");
                JSONArray summaryIds = new JSONObject(document.toJson()).getJSONArray("Clusters");
                for (int i = 0; i < summaryIds.length(); i++) {
                    String summary = summaryIds.getString(i);
                    clusterStrings.add(objectMapper.readValue(summary, ClusterString.class));
                }
            }
            return new LabelHolder(label, clusterStrings, total);
        } catch (Exception e) {
            logger.error("An error occurred whilst getting clusters for a single topic", e);
        }
        return null;
    }

    public int getArticleCount(String id) {
        try {
            BasicDBObject firstMatch = new BasicDBObject("$match", new BasicDBObject("_id", new ObjectId(id)));
            BasicDBObject project = new BasicDBObject("$project", new BasicDBObject("clusters", new BasicDBObject("$size", "$Clusters")));
            List<BasicDBObject> dbObjects = Lists.newArrayList(firstMatch, project);
            Document output = collection.aggregate(dbObjects).first();
            return output.getInteger("clusters");
        } catch (Exception e) {
            logger.error("An error occurred whilst getting a single topic", e);
        }
        return 0;
    }

    private LabelHolder getLabelHolderFromIterator(MongoCursor<Document> iterator, Articles articleManager, Summaries summaryManager) {
        if (iterator.hasNext()) {
            Document document = iterator.next();
            String item = document.toJson();
            JSONObject jsonObject = new JSONObject(item);
            ObjectMapper objectMapper = new ObjectMapper();
            List<ArticleString> articles = new ArrayList<>();
            boolean needsClustering = jsonObject.getBoolean("NeedsClustering");
            try {
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getString(i);
                    articles.add(articleManager.getArticleFromId(articleId).getArticleString());
                }

            } catch (Exception e) {
                logger.error("An error occurred whilst getting articles for a single topic", e);
            }
            List<ClusterString> clusters = new ArrayList<>();
            try {
                JSONArray summaryIds = jsonObject.getJSONArray("Clusters");
                for (int i = 0; i < summaryIds.length(); i++) {
                    String summary = summaryIds.getString(i);
                    clusters.add(objectMapper.readValue(summary, ClusterString.class));
                }
            } catch (Exception e) {
                logger.error("An error occurred whilst getting clusters for a single topic", e);
            }
            LabelHolder labelHolder = new LabelHolder(jsonObject.getString("Label"), articles, clusters);
            labelHolder.setNeedsClustering(needsClustering);
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
        List<ObjectId> objectIds = topics.stream().map(LabelHolder::get_id).filter(Objects::nonNull).collect(Collectors.toList());
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

    public long clusteringCount() {
        BasicDBObject queryObject = new BasicDBObject().append("NeedsClustering", true);
        return collection.count(queryObject);
    }

    public List<LabelHolder> getClusteringTopics() {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("NeedsClustering", true);
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            Articles articleManager = new Articles(database);
            List<LabelHolder> labelHolders = new ArrayList<>();
            while (iterator.hasNext()) {
                Document document = iterator.next();
                String item = document.toJson();
                JSONObject jsonObject = new JSONObject(item);
                ObjectMapper objectMapper = new ObjectMapper();
                List<ArticleString> articles = new ArrayList<>();
                try {
                    JSONArray articleIds = jsonObject.getJSONArray("Articles");
                    for (int i = 0; i < articleIds.length(); i++) {
                        String articleId = articleIds.getJSONObject(i).getString("$oid");
                        articles.add(articleManager.getArticleFromId(articleId).getArticleString());
                    }

                } catch (Exception e) {
                    logger.error("An error occurred whilst getting articles for a single topic", e);
                }
                List<ClusterString> clusters = new ArrayList<>();
                try {
                    JSONArray summaryIds = jsonObject.getJSONArray("Clusters");
                    for (int i = 0; i < summaryIds.length(); i++) {
                        String summary = summaryIds.getString(i);
                        clusters.add(objectMapper.readValue(summary, ClusterString.class));
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
            try {
                Document document = labelHolder.createDocument();
                collection.replaceOne(new BasicDBObject().append("_id", labelHolder.get_id()), document);
            } catch (Exception e) {
                logger.error("Failed whilst trying to update a topic", e);
            }
        }
    }

    public String getTopicFromCluster(String id) {
        BasicDBObject basicDBObject = new BasicDBObject("Clusters", id);
        MongoCursor<Document> iterator = collection.find(basicDBObject).iterator();
        LabelHolder labelHolder = getLabelHolderFromIterator(iterator, new Articles(database), null);
        return labelHolder != null ? labelHolder.getId() : null;
    }

    public String getTopicName(String topicId) {
        BasicDBObject match = new BasicDBObject("_id", new ObjectId(topicId));
        Document doc = collection.find(match).projection(Projections.include("Label")).limit(1).first();
        if (doc != null) {
            return doc.getString("Label");
        } else {
            return null;
        }
    }
}
