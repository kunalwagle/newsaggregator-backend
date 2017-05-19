package com.newsaggregator.server;

import com.newsaggregator.base.DatabaseStorage;
import com.newsaggregator.base.OutletArticle;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private String imageUrl;
    private boolean isSubscribed = false;
    private boolean needsClustering;

    public LabelHolder(String label) {
        this.label = label;
    }

    public LabelHolder(String label, List<OutletArticle> articles, List<ClusterHolder> clusters) {
        this.label = label;
        this.articles = articles;
        this.clusters = clusters;
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

    private boolean clusterExists(ClusterHolder otherCluster) {
        List<OutletArticle> otherArticles = otherCluster.getArticles().stream().filter(Objects::nonNull).collect(Collectors.toList());
        return clusters.stream().anyMatch(ch -> ch.sameCluster(otherArticles));
    }

    public List<OutletArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<OutletArticle> articles) {
        this.articles = articles;
    }

    public List<ClusterHolder> getClusters() {
        return clusters;
    }

    public void setClusters(List<ClusterHolder> clusters) {
        this.clusters = clusters;
    }

    public void addCluster(ClusterHolder articlesForSummary) {
        if (clusters == null) {
            clusters = new ArrayList<>();
        }
        if (!clusterExists(articlesForSummary)) {
            clusters.add(articlesForSummary);
        }
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
        this.id = _id.toHexString();
    }

    public String getId() {
        return id;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Document createDocument() {
        Document document = new Document();
        try {
            if (_id == null) {
                this._id = new ObjectId();
            }
            document.put("_id", _id);
            document.put("Label", label);
            document.put("Articles", articles.stream().map(OutletArticle::get_id).filter(Objects::nonNull).collect(Collectors.toList()));
            document.put("Clusters", clusters.stream().map(ClusterHolder::get_id).filter(Objects::nonNull).collect(Collectors.toList()));
            document.put("NeedsClustering", needsClustering);
            if (imageUrl != null) {
                document.put("imageUrl", imageUrl);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("Got exception creating topic doc, but it's fine", e);
        }
        return document;
    }

    public String getLabel() {
        return label;
    }

    public void addArticles(List<OutletArticle> value) {
        if (articles == null) {
            articles = new ArrayList<>();
        }
        for (OutletArticle art : value) {
            if (articles.stream().noneMatch(a -> a.getArticleUrl().equals(art.getArticleUrl()))) {
                articles.add(art);
            }
        }
    }

    public boolean getNeedsClustering() {
        return needsClustering;
    }

    public void setNeedsClustering(boolean needsClustering) {
        this.needsClustering = needsClustering;
    }
}
