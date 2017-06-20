package com.newsaggregator.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.ArticleHolder;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.ClusterString;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class HomeArticles {

    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private Logger logger = Logger.getLogger(getClass());

    public HomeArticles(MongoDatabase database) {
        this.database = database;
        this.collection = getCollection();
    }

    private MongoCollection<Document> getCollection() {
        MongoCollection<Document> articleCollection = database.getCollection("home", Document.class);

        if (articleCollection == null) {
            database.createCollection("home");
            articleCollection = database.getCollection("home", Document.class);
        }
        return articleCollection;
    }

    public void initialise() {
        Summaries summaries = new Summaries(database);
        Topics topics = new Topics(database);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ArticleHolder> articleHolders = new ArrayList<>();
        try {
            List<ClusterHolder> clusterHolderList = summaries.getMostRecent();
            for (ClusterHolder clusterHolder : clusterHolderList) {
                ClusterString clusterString = clusterHolder.getClusterString();
                String topicId = topics.getTopicFromCluster(objectMapper.writeValueAsString(clusterString));
                articleHolders.add(new ArticleHolder(topicId, clusterString));
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error in the Home Resource", e);
        }
        saveHomeArticles(articleHolders);
    }

    public long count() {
        return collection.count();
    }

    public void saveHomeArticles(List<ArticleHolder> articles) {
        for (ArticleHolder articleHolder : articles) {
            try {
                Document document = articleHolder.createDocument();
                collection.insertOne(document);
            } catch (MongoWriteException e) {
                logger.info("This article is already in the collection");
            } catch (Exception e) {
                logger.error("Writing home articles error", e);
            }
        }
        limitCollection();
    }

    private void limitCollection() {
        if (count() > 10) {
            long numberToRemove = count() - 10;
            MongoCursor<Document> docs = collection.find().iterator();
            List<ObjectId> objectIds = new ArrayList<>();
            while (docs.hasNext() && numberToRemove > 0) {
                objectIds.add(docs.next().getObjectId("_id"));
            }
            try {
                collection.deleteMany(new BasicDBObject("_id", new BasicDBObject("$in", objectIds)));
            } catch (Exception e) {
                logger.error("Deleting home articles error", e);
            }
        }
    }

    public List<ArticleHolder> getArticles() {
        List<ArticleHolder> articleHolders = new ArrayList<>();
        for (Document document : collection.find()) {
            try {
                String articleId = document.getString("articleId");
                String topicId = document.getString("topicId");
                String lastPublished = document.getString("lastPublished");
                String title = document.getString("title");
                String imageUrl = document.getString("imageUrl");
                articleHolders.add(new ArticleHolder(topicId, articleId, title, imageUrl, lastPublished));
            } catch (Exception e) {
                logger.error("Getting home article error", e);
            }
        }
        return articleHolders;
    }

}
