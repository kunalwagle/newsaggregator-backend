package com.newsaggregator.base;

import com.newsaggregator.Utils;

import java.util.List;

/**
 * Created by kunalwagle on 14/05/2017.
 */
public class Subscription {

    private String topicId;
    private List<String> sources;
    private boolean digests;

    public Subscription(String topicId, List<String> sources, boolean digests) {
        this.topicId = topicId;
        this.sources = sources;
        this.digests = digests;
    }

    public Subscription() {
    }

    public Subscription(String topicId) {
        this.topicId = topicId;
        this.sources = Utils.allSources();
        this.digests = false;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public void setDigests(boolean digests) {
        this.digests = digests;
    }

    public String getTopicId() {
        if (topicId == null) {
            return "";
        }
        return topicId;
    }

    public List<String> getSources() {
        return sources;
    }

    public boolean isDigests() {
        return digests;
    }
}
