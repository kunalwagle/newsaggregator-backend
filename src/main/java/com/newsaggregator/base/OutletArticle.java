package com.newsaggregator.base;

import com.newsaggregator.server.ArticleString;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by kunalwagle on 30/01/2017.
 */
public class OutletArticle extends Article implements DatabaseStorage {

    private ObjectId _id;
    private String body;
    private String articleUrl;
    private String lastPublished;
    private boolean isLabelled;
    private String id;

    public OutletArticle(String title, String body, String imageUrl, String articleUrl, String source, String lastPublished) {
        super(source, title, imageUrl);
        this.body = body;
        this.articleUrl = articleUrl;
        this.lastPublished = lastPublished;
    }

    public OutletArticle() {
        //Dummy constructor for Jackson
    }

    public OutletArticle(Document item) {
        _id = (ObjectId) item.get("_id");
        title = (String) item.get("Title");
        body = (String) item.get("Body");
        imageUrl = (String) item.get("ImageURL");
        articleUrl = (String) item.get("ArticleURL");
        source = (String) item.get("Source");
        lastPublished = (String) item.get("LastPublished");
        id = _id.toHexString();
    }

    public String getBody() {
        return body;
    }

    public String getArticleUrl() {
        if (articleUrl == null) {
            return "";
        }
        return articleUrl;
    }

    public String getLastPublished() {
        return lastPublished;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLabelled() {
        return isLabelled;
    }

    public void setLabelled(boolean labelled) {
        isLabelled = labelled;
    }

    public ArticleString getArticleString() {
        return new ArticleString(id, articleUrl, source);
    }

    @Override
    public Document createDocument() {
        try {
            Document doc = new Document();
            if (_id == null) {
                this._id = new ObjectId();
                this.id = _id.toHexString();
            }
            doc.put("_id", _id);
            doc.put("Title", title);
            doc.put("Body", body);
            doc.put("ImageURL", imageUrl);
            doc.put("ArticleURL", articleUrl);
            doc.put("Source", source);
            doc.put("LastPublished", lastPublished);
            doc.put("isLabelled", isLabelled);

            return doc;
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("Got exception creating article doc, but it's fine", e);
            return null;
        }
    }
}
