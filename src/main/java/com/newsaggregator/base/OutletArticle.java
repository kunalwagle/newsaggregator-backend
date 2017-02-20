package com.newsaggregator.base;

import org.bson.Document;

import java.util.HashMap;

/**
 * Created by kunalwagle on 30/01/2017.
 */
public class OutletArticle extends Article implements DatabaseStorage {

    private String body;
    private String articleUrl;

    public OutletArticle(String title, String body, String imageUrl, String articleUrl, String source) {
        super(source, title, imageUrl);
        this.body = body;
        this.articleUrl = articleUrl;
    }

    public String getBody() {
        return body;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public Document outletArticleToDoc() {
        Document doc = new Document();

        doc.put("Title", title);
        doc.put("Body", body);
        doc.put("ImageURL", imageUrl);
        doc.put("ArticleURL", articleUrl);
        doc.put("Source", source);

        return doc;
    }

    @Override
    public HashMap<String, Object> createNonPrimaryHashMap() {
        HashMap<String, Object> doc = new HashMap<>();

        doc.put("Title", title);
        doc.put("Body", body);
        doc.put("ImageUrl", imageUrl);
        doc.put("Source", source);

        return doc;
    }
}
