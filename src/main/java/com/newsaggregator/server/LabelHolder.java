package com.newsaggregator.server;

import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.clustering.Cluster;
import com.newsaggregator.ml.summarisation.Summary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 17/04/2017.
 */
public class LabelHolder {

    private String label;
    private List<Summary> summaries;
    private List<OutletArticle> articles;
    private List<List<OutletArticle>> clusters;

    public LabelHolder(String label) {
        this.label = label;
    }

    public void setArticles(List<OutletArticle> articles) {
        this.articles = articles;
    }

    public void setSummaries(List<Summary> summaries) {
        this.summaries = summaries;
    }

    public void addArticle(OutletArticle article) {
        if (articles == null) {
            articles = new ArrayList<>();
        }
        if (articles.stream().anyMatch(art -> art.getArticleUrl().equals(article.getArticleUrl()))) {
            OutletArticle oldArticle = articles.stream().filter(art -> art.getArticleUrl().equals(article.getArticleUrl())).findFirst().get();
            articles.remove(oldArticle);
        }
        articles.add(article);
    }

    public void addSummary(Summary summary) {
        if (summaries == null) {
            summaries = new ArrayList<>();
        }
        if (summaries.stream().anyMatch(summary::equals)) {
            Summary oldSummary = summaries.stream().filter(summary::equals).findFirst().get();
            summaries.remove(oldSummary);
        }
        summaries.add(summary);
    }

    public boolean clusterExists(Cluster<ArticleVector> otherCluster) {
        List<OutletArticle> otherArticles = otherCluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
        for (List<OutletArticle> articles : clusters) {
            if (articles.size() == otherArticles.size() && articles.stream().allMatch(article -> articleInList(otherArticles, article))) {
                return true;
            }
        }
        return false;
    }

    public List<OutletArticle> getArticles() {
        return articles;
    }

    public List<List<OutletArticle>> getClusters() {
        return clusters;
    }

    public List<Summary> getSummaries() {
        return summaries;
    }

    private boolean articleInList(List<OutletArticle> otherArticles, OutletArticle article) {
        for (OutletArticle art : otherArticles) {
            if (art.getArticleUrl().equals(article.getArticleUrl())) {
                return true;
            }
        }
        return false;
    }
}
