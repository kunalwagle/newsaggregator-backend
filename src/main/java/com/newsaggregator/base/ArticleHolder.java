package com.newsaggregator.base;

import com.newsaggregator.server.ClusterString;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class ArticleHolder {

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
}
