package com.newsaggregator.server;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class ClusterHolder {

    private List<OutletArticle> articles;
    private List<Node> summary;
    private List<String> labels = new ArrayList<>();

    public ClusterHolder(List<OutletArticle> articles) {
        this.articles = articles;
    }

    public ClusterHolder(List<OutletArticle> articles, List<Node> summary) {
        this.articles = articles;
        this.summary = summary;
    }

    public List<OutletArticle> getArticles() {
        return articles;
    }

    public List<Node> getSummary() {
        return summary;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setSummary(List<Node> summary) {
        this.summary = summary;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public boolean sameCluster(List<OutletArticle> holder) {
        List<String> otherUrls = holder.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
        List<String> theseUrls = articles.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
        return otherUrls.size() == theseUrls.size() && theseUrls.stream().allMatch(otherUrls::contains);
    }

    public ClusterString convertToClusterString() {
        List<String> articleUrls = articles.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
        return new ClusterString(articleUrls, summary);
    }

}
