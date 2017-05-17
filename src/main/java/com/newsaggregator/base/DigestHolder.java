package com.newsaggregator.base;

import com.newsaggregator.Utils;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class DigestHolder implements DatabaseStorage {

    private ObjectId _id;
    private String id;
    private String emailAddress;
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
        this.articleHolders = articleHolders;
        this.emailAddress = user.getEmailAddress();
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
        }
        document.put("_id", _id);
        document.put("id", _id.toHexString());
        document.put("articleHolders", articleHolders);
        document.put("emailAddress", emailAddress);
        return document;
    }


    private class ArticleHolder {

        private String topicId;
        private ClusterHolder clusterHolder;

        public ArticleHolder(String topicId, ClusterHolder clusterHolder) {
            this.topicId = topicId;
            this.clusterHolder = clusterHolder;
        }

        public String getTopicId() {
            return topicId;
        }

        public ClusterHolder getClusterHolder() {
            return clusterHolder;
        }
    }
}
