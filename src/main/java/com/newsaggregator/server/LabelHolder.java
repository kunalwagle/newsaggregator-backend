package com.newsaggregator.server;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private List<ArticleString> articles = new ArrayList<>();
    private List<ClusterString> clusters = new ArrayList<>();
    private String imageUrl;
    private int articleCount;
    private boolean isSubscribed = false;
    private boolean needsClustering;

    public LabelHolder(String label) {
        this.label = label;
    }

    public LabelHolder(String label, List<ArticleString> articles, List<ClusterString> clusters) {
        this.label = label;
        this.articles = articles;
        this.clusters = clusters;
    }

    public LabelHolder(String label, List<ClusterString> clusterStrings, int total) {
        this.label = label;
        this.clusters = clusterStrings;
        this.articleCount = total;
    }

    public void addArticle(OutletArticle article) {
        if (articles == null) {
            articles = new ArrayList<>();
        }
        ArticleString articleString = new ArticleString(article.getId(), article.getArticleUrl(), article.getSource());
        if (articles.stream().anyMatch(art -> art.getArticleUrl().equals(article.getArticleUrl()))) {
            ArticleString oldArticle = articles.stream().filter(art -> art.getArticleUrl().equals(article.getArticleUrl())).findFirst().get();
            articles.remove(oldArticle);
        }
        articles.add(articleString);
    }

    private boolean clusterExists(ClusterString otherCluster) {
        return clusters.stream().anyMatch(cs -> cs.getId().equals(otherCluster.getId()));
    }

    public List<ArticleString> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleString> articles) {
        this.articles = articles;
    }

    public List<ClusterString> getClusters() {
        return clusters;
    }

    public void setClusters(List<ClusterString> clusters) {
        this.clusters = clusters;
    }

    public void addCluster(ClusterHolder articlesForSummary) {
        if (clusters == null) {
            clusters = new ArrayList<>();
        }
        ClusterString clusterString = articlesForSummary.getClusterString();
        if (!clusterExists(clusterString)) {
            clusters.add(0, clusterString);
        }
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
        this.id = _id.toHexString();
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
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
            document.put("Articles", articles.stream().map(ArticleString::getId).filter(Objects::nonNull).collect(Collectors.toList()));
            document.put("Clusters", getClusterList());
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
            addArticle(art);
        }
    }

    public boolean getNeedsClustering() {
        return needsClustering;
    }

    public void setNeedsClustering(boolean needsClustering) {
        this.needsClustering = needsClustering;
    }

    public void addClusters(List<ClusterHolder> brandNewClusters) {
        for (ClusterHolder clusterHolder : brandNewClusters) {
            addCluster(clusterHolder);
        }
    }

    public List<String> getClusterList() {
        List<String> strings = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (ClusterString clusterString : clusters) {
            try {
                strings.add(objectMapper.writeValueAsString(clusterString));
            } catch (Exception e) {
                Logger.getLogger(getClass()).error("Unable to parse JSON", e);
            }
        }
        return strings;
    }
}
