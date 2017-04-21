package com.newsaggregator.server;

import java.util.List;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class TopicHolder {

    private String topic;
    private List<ClusterHolder> clusterHolder;

    public TopicHolder(String topic, List<ClusterHolder> clusterHolder) {
        this.topic = topic;
        this.clusterHolder = clusterHolder;
    }
}
