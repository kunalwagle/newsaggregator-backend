package com.newsaggregator.base;

/**
 * Created by kunalwagle on 18/05/2017.
 */
public class DigestString {

    private String id;
    private String emailAddress;
    private int topicCount;
    private String articleHolders;

    public DigestString() {
    }

    public DigestString(String id, String emailAddress, int topicCount, String articleHolders) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.topicCount = topicCount;
        this.articleHolders = articleHolders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }

    public String getArticleHolders() {
        return articleHolders;
    }

    public void setArticleHolders(String articleHolders) {
        this.articleHolders = articleHolders;
    }
}
