package com.newsaggregator.routes;

import com.newsaggregator.base.Subscription;

import java.util.List;

/**
 * Created by kunalwagle on 02/05/2017.
 */
public class UserHolder {

    private String id;
    private String emailAddress;
    private List<Subscription> topics;

    public UserHolder(String id, String emailAddress, List<Subscription> topics) {
        this.id = id;
        this.topics = topics;
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getId() {
        return id;
    }

    public List<Subscription> getTopics() {
        return topics;
    }


}
