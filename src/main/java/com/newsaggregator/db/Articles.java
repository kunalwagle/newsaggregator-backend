package com.newsaggregator.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.OutletArticle;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        List<Document> documents = articles.stream().map(OutletArticle::createDocument).collect(Collectors.toList());
        collection.insertMany(documents);
    }

    public void updateArticles(List<OutletArticle> articles) {
        for (OutletArticle article : articles) {
            Document document = article.createDocument();
            collection.replaceOne(new BasicDBObject().append("_id", article.get_id()), document);
        }
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
                Document item = iterator.next();
                return new OutletArticle(item);
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single article", e);
        }
        return null;
    }

    public OutletArticle getArticleFromId(String id) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("_id", new ObjectId(id));
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            if (iterator.hasNext()) {
                Document item = iterator.next();
                return new OutletArticle(item);
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
                Document item = iterator.next();
                articles.add(new OutletArticle(item));
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

    public long count() {
        return collection.count();
    }

    public List<OutletArticle> getUnlabelledArticles() {

        BasicDBObject queryObject = new BasicDBObject().append("isLabelled", false);

        List<OutletArticle> articles = new ArrayList<>();

        try {
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            while (iterator.hasNext()) {
                Document item = iterator.next();
                articles.add(new OutletArticle(item));
            }

        } catch (Exception e) {
            logger.error("Caught an exception getting unlabelled articles", e);
        }

        return articles;
    }
}
