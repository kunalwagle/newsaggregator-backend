package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class User {

    private String emailAddress;
    private List<String> topics;

    public User(String emailAddress, List<String> topics) {
        this.emailAddress = emailAddress;
        this.topics = topics;
    }

    public User() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
