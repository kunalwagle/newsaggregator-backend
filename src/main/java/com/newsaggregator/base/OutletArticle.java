package com.newsaggregator.base;

import org.bson.Document;

/**
 * Created by kunalwagle on 30/01/2017.
 */
public class OutletArticle extends Article implements DatabaseStorage {

    private String _id;
    private String body;
    private String articleUrl;
    private String lastPublished;

    public OutletArticle(String title, String body, String imageUrl, String articleUrl, String source, String lastPublished) {
        super(source, title, imageUrl);
        this.body = body;
        this.articleUrl = articleUrl;
        this.lastPublished = lastPublished;
    }

    public OutletArticle() {
        //Dummy constructor for Jackson
    }

    public String getBody() {
        return body;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getLastPublished() {
        return lastPublished;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public Document createDocument() {
        Document doc = new Document();

        doc.put("Title", title);
        doc.put("Body", body);
        doc.put("ImageURL", imageUrl);
        doc.put("ArticleURL", articleUrl);
        doc.put("Source", source);

        return doc;
    }
}
