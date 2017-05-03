package com.newsaggregator.routes;

import com.newsaggregator.Utils;
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
    private List<LabelHolder> topics;

    public UserHolder(String id, String emailAddress, List<String> topics) {
        this.id = id;
        Topics topicManager = new Topics(Utils.getDatabase());
        List<LabelHolder> labelHolders = new ArrayList<>();
        for (String topicId : topics) {
            LabelHolder labelHolder = topicManager.getTopicById(topicId);
            if (labelHolder != null) {
                labelHolders.add(labelHolder);
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

    public List<LabelHolder> getTopics() {
        return topics;
    }
}
