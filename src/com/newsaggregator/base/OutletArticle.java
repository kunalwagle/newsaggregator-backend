package com.newsaggregator.base;

import org.bson.Document;

/**
 * Created by kunalwagle on 30/01/2017.
 */
public class OutletArticle extends Article {

    private String body;
    private String imageUrl;
    private String articleUrl;

    public OutletArticle(String title, String body, String imageUrl, String articleUrl, String source) {
        super(source, title);
        this.body = body;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
    }

    public String getBody() {
        return body;
    }

    public String getImageUrl() {
        return imageUrl;
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
}
