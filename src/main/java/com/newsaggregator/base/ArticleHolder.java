package com.newsaggregator.base;

import com.newsaggregator.server.ClusterString;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class ArticleHolder {

    private ObjectId _id;
    private String topicId;
    private String articleId;
    private String title;
    private String imageUrl;
    private String lastPublished;

    public ArticleHolder() {

    }

    public ArticleHolder(String topicId, ClusterString clusterHolder) {
        this.topicId = topicId;
        this.articleId = clusterHolder.getId();
        this.title = clusterHolder.getTitle();
        this.imageUrl = clusterHolder.getImageUrl();
        this.lastPublished = clusterHolder.getLastPublished();
    }

    public ArticleHolder(String topicId, String articleId, String title, String imageUrl, String lastPublished) {
        this.topicId = topicId;
        this.articleId = articleId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.lastPublished = lastPublished;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLastPublished() {
        return lastPublished;
    }

    public Document createDocument() {
        Document document = new Document();
        if (_id == null) {
            this._id = new ObjectId(articleId);
        }
        document.put("_id", _id);
        document.put("topicId", topicId);
        document.put("articleId", articleId);
        document.put("title", title);
        document.put("imageUrl", imageUrl);
        document.put("lastPublished", lastPublished);
        return document;
    }
}
