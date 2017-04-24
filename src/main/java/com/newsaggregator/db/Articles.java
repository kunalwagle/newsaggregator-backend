package com.newsaggregator.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.OutletArticle;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 04/02/2017.
 */
public class Articles {

    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private Logger logger = Logger.getLogger(getClass());

    public Articles(MongoDatabase database) {
        this.database = database;
        this.collection = getCollection();
    }

    public void saveArticles(List<OutletArticle> articles) {
        MongoCollection<Document> articleCollection = getCollection();
        List<Document> documents = articles.stream().map(OutletArticle::createDocument).collect(Collectors.toList());
        articleCollection.insertMany(documents);
    }

    public List<OutletArticle> articlesToAdd(List<OutletArticle> articles) {
        return articles.stream().filter(article -> !articleExists(article)).collect(Collectors.toList());
    }

    private boolean articleExists(OutletArticle article) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("ArticleUrl", article.getArticleUrl());
            return collection.count(queryObject) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public OutletArticle getSingleArticle(String url) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("ArticleUrl", url);
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            if (iterator.hasNext()) {
                String item = iterator.next().toJson();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(item, OutletArticle.class);
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single article", e);
        }
        return null;
    }

    public List<OutletArticle> getAllArticles() {
        List<OutletArticle> articles = new ArrayList<>();
        try {
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                String item = iterator.next().toJson();
                ObjectMapper objectMapper = new ObjectMapper();
                articles.add(objectMapper.readValue(item, OutletArticle.class));
            }
        } catch (Exception e) {
            logger.error("Caught an exception", e);
        }
        logger.info("About to return articles");
        return articles;
    }

    private MongoCollection<Document> getCollection() {
        MongoCollection<Document> articleCollection = database.getCollection("articles", Document.class);

        if (articleCollection == null) {
            database.createCollection("articles");
            articleCollection = database.getCollection("articles", Document.class);
        }
        return articleCollection;
    }

}
