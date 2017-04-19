package com.newsaggregator.server;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Summary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class ClusterHolder {

    private List<OutletArticle> articles;
    private Summary summary;
    private List<String> labels = new ArrayList<>();

    public ClusterHolder(List<OutletArticle> articles) {
        this.articles = articles;
    }

    public List<OutletArticle> getArticles() {
        return articles;
    }

    public Summary getSummary() {
        return summary;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public boolean sameCluster(ClusterHolder holder) {
        List<String> otherUrls = holder.getArticles().stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
        List<String> theseUrls = articles.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
        return otherUrls.size() == theseUrls.size() && theseUrls.stream().allMatch(otherUrls::contains);
    }

}
