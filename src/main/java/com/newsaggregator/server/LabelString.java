package com.newsaggregator.server;

import com.newsaggregator.ml.summarisation.Summary;

import java.util.List;

/**
 * Created by kunalwagle on 17/04/2017.
 */
public class LabelString {

    private List<String> articles;
    private List<List<String>> clusters;
    private List<Summary> summaries;

    public LabelString(List<String> articles, List<List<String>> clusters, List<Summary> summaries) {
        this.articles = articles;
        this.clusters = clusters;
        this.summaries = summaries;
    }

    public List<String> getArticles() {
        return articles;
    }

    public List<List<String>> getClusters() {
        return clusters;
    }

    public List<Summary> getSummaries() {
        return summaries;
    }
}
