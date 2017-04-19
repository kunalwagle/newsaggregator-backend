package com.newsaggregator.server;

import java.util.List;

/**
 * Created by kunalwagle on 17/04/2017.
 */
public class LabelString {

    private List<String> articles;
    private List<ClusterString> clusters;

    public LabelString(List<String> articles, List<ClusterString> clusters) {
        this.articles = articles;
        this.clusters = clusters;
    }

    public List<String> getArticles() {
        return articles;
    }

    public List<ClusterString> getClusters() {
        return clusters;
    }

}
