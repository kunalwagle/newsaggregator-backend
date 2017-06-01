package com.newsaggregator.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.DatabaseStorage;
import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import com.newsaggregator.ml.summarisation.Summary;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class ClusterHolder implements DatabaseStorage {

    private ObjectId _id;
    private String id;
    private String title;
    private String lastPublished;
    private String imageUrl;
    private List<OutletArticle> articles;
    private Map<String, List<Node>> summaryMap = new HashMap<>();
    private List<String> labels = new ArrayList<>();

    public ClusterHolder() {

    }

    public ClusterHolder(List<OutletArticle> articles) {
        this.articles = articles;
        initialise();
    }

    public ClusterHolder(List<OutletArticle> articles, List<Summary> summaries) {
        this.articles = articles;
        for (Summary summary : summaries) {
            List<String> sources = summary.getArticles().stream().map(OutletArticle::getSource).filter(Objects::nonNull).collect(Collectors.toList());
            String source = sources.stream().sorted().collect(Collectors.toList()).toString().replace(" ", "");
            summaryMap.put(source, summary.getNodes());
        }
        initialise();
    }

    private void initialise() {
        if (articles.stream().anyMatch(article -> article.getSource().equals(Outlet.AssociatedPress.getSourceString()))) {
            this.title = articles.stream().filter(article -> article.getSource().equals(Outlet.AssociatedPress.getSourceString())).findFirst().get().getTitle();
        } else if (articles.stream().anyMatch(article -> article.getSource().equals(Outlet.Reuters.getSourceString()))) {
            this.title = articles.stream().filter(article -> article.getSource().equals(Outlet.Reuters.getSourceString())).findFirst().get().getTitle();
        } else if (articles.stream().anyMatch(article -> article.getSource().equals(Outlet.BBCNews.getSourceString()))) {
            this.title = articles.stream().filter(article -> article.getSource().equals(Outlet.BBCNews.getSourceString())).findFirst().get().getTitle();
        } else {
            if (articles.size() > 0) {
                this.title = articles.get(0).getTitle();
            }
        }
        if (articles.size() > 0) {
            this.lastPublished = articles.get(0).getLastPublished();
        }
        try {
            this.imageUrl = articles.stream().filter(article -> article.getImageUrl() != null).findAny().get().getImageUrl();
        } catch (Exception e) {
            this.imageUrl = null;
        }
    }

    public Map<String, List<Node>> getSummaryMap() {
        return summaryMap;
    }

    public void setSummaryMap(Map<String, List<Node>> summaryMap) {
        this.summaryMap = summaryMap;
    }

    public List<OutletArticle> getArticles() {
        return articles;
    }

    public List<List<Node>> getSummary() {
        return new ArrayList<>(summaryMap.values());
    }

    public void setSummary(List<Summary> summaries) {
        for (Summary summary : summaries) {
            List<String> articleUrls = summary.getArticles().stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
            String source = articleUrls.stream().sorted().collect(Collectors.toList()).toString().replace(" ", "");
            summaryMap.put(source, summary.getNodes());
        }
    }

    public List<String> getLabels() {
        return labels;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public boolean sameCluster(List<OutletArticle> holder) {
        if (holder != null) {
            List<String> otherUrls = holder.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
            List<String> theseUrls = articles.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
            return otherUrls.size() == theseUrls.size() && theseUrls.stream().allMatch(otherUrls::contains);
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
        this.id = _id.toHexString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastPublished() {
        return lastPublished;
    }

    public void setLastPublished(String lastPublished) {
        this.lastPublished = lastPublished;
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
                this.id = this._id.toHexString();
            }
            document.put("_id", _id);
            document.put("Articles", articles.stream().map(OutletArticle::get_id).filter(Objects::nonNull).collect(Collectors.toList()));
            ObjectMapper objectMapper = new ObjectMapper();
            document.put("Summaries", objectMapper.writeValueAsString(summaryMap));
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("Got exception creating cluster doc, but it's fine", e);
            return null;
        }
        return document;
    }

    public ClusterString getClusterString() {
        return new ClusterString(id, title, imageUrl);
    }
}
