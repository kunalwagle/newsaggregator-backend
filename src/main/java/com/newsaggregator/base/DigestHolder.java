package com.newsaggregator.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.Utils;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class DigestHolder implements DatabaseStorage {

    private ObjectId _id;
    private String id;
    private String emailAddress;
    private int topicCount;
    private List<ArticleHolder> articleHolders;

    public DigestHolder() {

    }

    public DigestHolder(User user) {
        List<Subscription> subs = user.getTopicIds().stream().filter(Subscription::isDigests).collect(Collectors.toList());
        List<ArticleHolder> articleHolders = new ArrayList<>();
        Topics topics = new Topics(Utils.getDatabase());
        for (Subscription sub : subs) {
            String topicId = sub.getTopicId();
            LabelHolder labelHolder = topics.getTopicById(topicId);
            List<ArticleHolder> articles = labelHolder.getClusters().stream().map(c -> new ArticleHolder(topicId, c)).collect(Collectors.toList());
            if (articles != null && articles.size() > 0) {
                articleHolders.addAll(articles);
            }
        }


        this.articleHolders = articleHolders.stream().sorted(byLastPublished).limit(10).collect(Collectors.toList());
        this.emailAddress = user.getEmailAddress();
        this.topicCount = subs.size();
    }

    public int getTopicCount() {
        return topicCount;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ArticleHolder> getArticleHolders() {
        return articleHolders;
    }

    public void setArticleHolders(List<ArticleHolder> articleHolders) {
        this.articleHolders = articleHolders;
    }

    @Override
    public Document createDocument() {
        Document document = new Document();
        if (_id == null) {
            this._id = new ObjectId();
            this.id = _id.toHexString();
        }
        document.put("_id", _id);
        document.put("id", id);
        ObjectMapper objectMapper = new ObjectMapper();
        String ah = "[]";
        try {
            ah = objectMapper.writeValueAsString(articleHolders);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        document.put("articleHolders", ah);
        document.put("emailAddress", emailAddress);
        return document;
    }

    Comparator<ArticleHolder> byLastPublished = (left, right) -> {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {

            Date leftDate = formatter.parse(left.getClusterHolder().getLastPublished().replaceAll("Z$", "+0000"));
            Date rightDate = formatter.parse(right.getClusterHolder().getLastPublished().replaceAll("Z$", "+0000"));

            return leftDate.compareTo(rightDate);

        } catch (ParseException e) {
            Logger.getLogger(getClass()).error("Error in last published", e);
        }
        return 1;
    };
}
