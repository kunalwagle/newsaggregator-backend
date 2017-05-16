package com.newsaggregator.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.DatabaseStorage;
import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private List<List<Node>> summaries = new ArrayList<>();
    private Map<String, List<Node>> summaryMap = new HashMap<>();
    private List<String> labels = new ArrayList<>();

    public ClusterHolder(List<OutletArticle> articles) {
        this.articles = articles;
        initialise();
    }

    public ClusterHolder(List<OutletArticle> articles, List<List<Node>> summaries) {
        this.articles = articles;
        this.summaries = summaries;
        for (List<Node> nodes : summaries) {
            List<String> sources = nodes.stream().map(Node::getSource).distinct().collect(Collectors.toList());
            List<String> related = nodes.stream().flatMap(node -> node.getRelatedNodes().stream()).collect(Collectors.toList()).stream().map(Node::getSource).distinct().collect(Collectors.toList());
            sources.addAll(related);
            sources = sources.stream().distinct().collect(Collectors.toList());
            String source = sources.stream().sorted().collect(Collectors.toList()).toString().replace(" ", "");
            summaryMap.put(source, nodes);
        }
        initialise();
    }

    private void initialise() {
        if (articles.stream().anyMatch(article -> article.getSource().equals(Outlet.AssociatedPress.getSourceString()))) {
            this.title = articles.stream().filter(article -> article.getSource().equals(Outlet.AssociatedPress.getSourceString())).findFirst().get().getTitle();
        } else if (articles.stream().anyMatch(article -> article.getSource().equals(Outlet.Reuters.getSourceString()))) {
            this.title = articles.stream().filter(article -> article.getSource().equals(Outlet.Reuters.getSourceString())).findFirst().get().getTitle();
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

    public List<OutletArticle> getArticles() {
        return articles;
    }

    public List<List<Node>> getSummary() {
        return summaries;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setSummary(List<List<Node>> summaries) {
        this.summaries.addAll(summaries);
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public boolean sameCluster(List<OutletArticle> holder) {
        List<String> otherUrls = holder.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
        List<String> theseUrls = articles.stream().map(OutletArticle::getArticleUrl).collect(Collectors.toList());
        return otherUrls.size() == theseUrls.size() && theseUrls.stream().allMatch(otherUrls::contains);
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
        this.id = _id.toHexString();
    }

    public String getId() {
        return id;
    }

    public ObjectId get_id() {
        return _id;
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
            }
            document.put("_id", _id);
            document.put("Articles", articles.stream().map(OutletArticle::get_id).collect(Collectors.toList()));
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> sums = new ArrayList<>();
            for (List<Node> sum : summaries) {
                sums.add(objectMapper.writeValueAsString(sum));
            }
            document.put("Summaries", sums);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return document;
    }
}
