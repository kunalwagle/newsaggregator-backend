package com.newsaggregator.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.DatabaseStorage;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class ClusterHolder implements DatabaseStorage {

    private ObjectId _id;
    private String id;
    private List<OutletArticle> articles;
    private List<List<Node>> summaries = new ArrayList<>();
    private List<String> labels = new ArrayList<>();

    public ClusterHolder(List<OutletArticle> articles) {
        this.articles = articles;
    }

    public ClusterHolder(List<OutletArticle> articles, List<List<Node>> summaries) {
        this.articles = articles;
        this.summaries = summaries;
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
