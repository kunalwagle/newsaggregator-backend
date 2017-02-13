package com.newsaggregator.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.OutletArticle;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 04/02/2017.
 */
public class Articles {

    private MongoDatabase database;

    public Articles(MongoDatabase database) {
        this.database = database;
    }

    public void saveArticles(List<OutletArticle> articles) {
        MongoCollection<Document> articleCollection = getCollection();
        List<Document> documents = articles.stream().map(OutletArticle::outletArticleToDoc).collect(Collectors.toList());
        articleCollection.insertMany(documents);
    }

    public List<OutletArticle> getAllArticles() {
        MongoCollection<Document> articleCollection = getCollection();
        FindIterable<Document> articles = articleCollection.find();
        ArrayList<OutletArticle> result = new ArrayList<>();
        for (Document article : articles) {
            result.add(new OutletArticle(article.getString("Title"), article.getString("Body"), article.getString("ImageURL"), article.getString("ArticleURL"), article.getString("Source")));
        }
        return result;
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
