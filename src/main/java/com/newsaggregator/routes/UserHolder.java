package com.newsaggregator.routes;

import com.newsaggregator.Utils;
import com.newsaggregator.base.Subscription;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.LabelHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 02/05/2017.
 */
public class UserHolder {

    private String id;
    private String emailAddress;
    private List<LabelHolderWithSettings> topics;

    public UserHolder(String id, String emailAddress, List<Subscription> topics) {
        this.id = id;
        Topics topicManager = new Topics(Utils.getDatabase());
        List<LabelHolderWithSettings> labelHolders = new ArrayList<>();
        for (Subscription topic : topics) {
            LabelHolder labelHolder = topicManager.getTopicById(topic.getTopicId());
            if (labelHolder != null) {
                labelHolders.add(new LabelHolderWithSettings(labelHolder, 1, topic.isDigests(), topic.getSources()));
            }
        }
        this.topics = labelHolders;
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getId() {
        return id;
    }

    public List<LabelHolderWithSettings> getTopics() {
        return topics;
    }


}
