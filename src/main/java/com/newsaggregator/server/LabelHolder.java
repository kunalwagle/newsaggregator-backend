package com.newsaggregator.server;

import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.DatabaseStorage;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.clustering.Cluster;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 17/04/2017.
 */
public class LabelHolder implements DatabaseStorage {

    private ObjectId _id;
    private String id;
    private String label;
    private List<OutletArticle> articles = new ArrayList<>();
    private List<ClusterHolder> clusters = new ArrayList<>();
    private boolean isSubscribed = false;

    public LabelHolder(String label) {
        this.label = label;
    }

    public LabelHolder(String label, List<OutletArticle> articles, List<ClusterHolder> clusters) {
        this.label = label;
        this.articles = articles;
        this.clusters = clusters;
    }

    public void setArticles(List<OutletArticle> articles) {
        this.articles = articles;
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

    public boolean clusterExists(Cluster<ArticleVector> otherCluster) {
        List<OutletArticle> otherArticles = otherCluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
        return clusters.stream().anyMatch(ch -> ch.sameCluster(otherArticles));
    }

    public List<OutletArticle> getArticles() {
        return articles;
    }

    public List<ClusterHolder> getClusters() {
        return clusters;
    }

    public void addCluster(ClusterHolder articlesForSummary) {
        clusters.add(articlesForSummary);
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getId() {
        return id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
        this.id = _id.toHexString();
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    @Override
    public Document createDocument() {
        Document document = new Document();
        if (_id == null) {
            this._id = new ObjectId();
        }
        document.put("_id", _id);
        document.put("Label", label);
        document.put("Articles", articles.stream().map(OutletArticle::get_id).collect(Collectors.toList()));
        document.put("Clusters", clusters.stream().map(ClusterHolder::get_id).collect(Collectors.toList()));
        return document;
    }

    public String getLabel() {
        return label;
    }
}
