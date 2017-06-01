package com.newsaggregator.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import com.newsaggregator.server.ClusterHolder;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 24/04/2017.
 */
public class Summaries {

    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private Logger logger = Logger.getLogger(getClass());

    public Summaries(MongoDatabase database) {
        this.database = database;
        this.collection = getCollection();
    }

    public void saveSummaries(List<ClusterHolder> clusterHolders) {
        MongoCollection<Document> articleCollection = getCollection();
        List<Document> documents = clusterHolders.stream().map(ClusterHolder::createDocument).filter(Objects::nonNull).collect(Collectors.toList());
        documents = documents.stream().filter(Objects::nonNull).collect(Collectors.toList());
        articleCollection.insertMany(documents);
    }

    public ClusterHolder getSingleCluster(String _id) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("_id", new ObjectId(_id));
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            Articles articleManager = new Articles(database);
            ObjectMapper objectMapper = new ObjectMapper();
            if (iterator.hasNext()) {
                Document document = iterator.next();
                String item = document.toJson();
                JSONObject jsonObject = new JSONObject(item);
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                List<OutletArticle> articles = new ArrayList<>();
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getJSONObject(i).getString("$oid");
                    articles.add(articleManager.getArticleFromId(articleId));
                }
                String summs = jsonObject.getString("Summaries");
                ClusterHolder clusterHolder = new ClusterHolder(articles);
                clusterHolder.setSummaryMap(objectMapper.readValue(summs, new TypeReference<Map<String, List<Node>>>() {
                }));
                clusterHolder.set_id(document.getObjectId("_id"));
                return clusterHolder;
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single cluster", e);
        }
        return null;
    }

    public List<ClusterHolder> getAllClusters() {
        List<ClusterHolder> clusters = new ArrayList<>();
        MongoCursor<Document> iterator = collection.find().iterator();
        return getClusterHolders(clusters, iterator);
    }

    private List<ClusterHolder> getClusterHolders(List<ClusterHolder> clusters, MongoCursor<Document> iterator) {
        Articles articleManager = new Articles(database);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
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
                String summs = jsonObject.getString("Summaries");
                ClusterHolder clusterHolder = new ClusterHolder(articles);
                clusterHolder.setSummaryMap(objectMapper.readValue(summs, new TypeReference<Map<String, List<Node>>>() {
                }));
                clusterHolder.set_id(document.getObjectId("_id"));
                clusters.add(clusterHolder);
            }
        } catch (Exception e) {
            logger.error("Caught an exception", e);
        }
        return clusters;
    }

    private MongoCollection<Document> getCollection() {
        MongoCollection<Document> summaryCollection = database.getCollection("summaries", Document.class);

        if (summaryCollection == null) {
            database.createCollection("summaries");
            summaryCollection = database.getCollection("summaries", Document.class);
        }
        return summaryCollection;
    }

    public long count() {
        return collection.count();
    }

    public void updateSummaries(List<ClusterHolder> clusters) {
        for (ClusterHolder clusterHolder : clusters) {
            try {
                Document document = clusterHolder.createDocument();
                collection.replaceOne(new BasicDBObject().append("_id", clusterHolder.get_id()), document);
            } catch (Exception e) {
                logger.error("Failed whilst trying to update a cluster", e);
            }
        }
    }

    public long unsummarisedCount() {
        BasicDBObject queryObject = new BasicDBObject().append("Summaries", new Document("$eq", "{}"));
        return collection.count(queryObject);
    }

    public List<ClusterHolder> getUnsummarisedClusters() {
        BasicDBObject queryObject = new BasicDBObject().append("Summaries", new Document("$eq", "{}"));
        MongoCursor<Document> iterator = collection.find(queryObject).iterator();

        return getClusterHolders(new ArrayList<>(), iterator);
    }

    public HashMap<String, ClusterHolder> getClusters(List<String> clusters) {
        BasicDBObject dbObject = new BasicDBObject("_id", new BasicDBObject("$in", clusters.stream().map(ObjectId::new).collect(Collectors.toList())));
        MongoCursor<Document> results = collection.find(dbObject).iterator();
        HashMap<String, ClusterHolder> hashMap = new HashMap<>();
        getClusterHolders(new ArrayList<>(), results).forEach(c -> hashMap.put(c.getId(), c));
        return hashMap;
    }
}
