package com.newsaggregator.base;

import com.newsaggregator.server.ClusterHolder;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class ArticleHolder {

    private String topicId;
    private ClusterHolder clusterHolder;

    public ArticleHolder() {

    }

    public ArticleHolder(String topicId, ClusterHolder clusterHolder) {
        this.topicId = topicId;
        this.clusterHolder = clusterHolder;
    }

    public String getTopicId() {
        return topicId;
    }

    public ClusterHolder getClusterHolder() {
        return clusterHolder;
    }

}
