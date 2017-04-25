package com.newsaggregator.db;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
        List<Document> documents = clusterHolders.stream().map(ClusterHolder::createDocument).collect(Collectors.toList());
        articleCollection.insertMany(documents);
    }

    public ClusterHolder getSingleCluster(String _id) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("_id", _id);
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            Articles articleManager = new Articles(database);
            ObjectMapper objectMapper = new ObjectMapper();
            if (iterator.hasNext()) {
                String item = iterator.next().toJson();
                JSONObject jsonObject = new JSONObject(item);
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                List<OutletArticle> articles = new ArrayList<>();
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getString(i);
                    articles.add(articleManager.getSingleArticle(articleId));
                }
                String nodes = jsonObject.getJSONArray("Summary").toString();
                List<Node> summary = objectMapper.readValue(nodes, List.class);
                return new ClusterHolder(articles, summary);
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single cluster", e);
        }
        return null;
    }

    public List<ClusterHolder> getAllClusters() {
        List<ClusterHolder> clusters = new ArrayList<>();
        Articles articleManager = new Articles(database);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                String item = iterator.next().toJson();
                JSONObject jsonObject = new JSONObject(item);
                JSONArray articleIds = jsonObject.getJSONArray("Articles");
                List<OutletArticle> articles = new ArrayList<>();
                for (int i = 0; i < articleIds.length(); i++) {
                    String articleId = articleIds.getString(i);
                    articles.add(articleManager.getSingleArticle(articleId));
                }
                String nodes = jsonObject.getJSONArray("Summary").toString();
                List<Node> summary = objectMapper.readValue(nodes, List.class);
                clusters.add(new ClusterHolder(articles, summary));
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
}
