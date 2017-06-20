package com.newsaggregator.base;

import com.newsaggregator.Utils;

import java.util.List;

/**
 * Created by kunalwagle on 14/05/2017.
 */
public class Subscription {

    private String topicId;
    private String labelName;
    private List<String> sources;
    private boolean digests;

    public Subscription(String topicId, String labelName, List<String> sources, boolean digests) {
        this.topicId = topicId;
        this.labelName = labelName;
        this.sources = sources;
        this.digests = digests;
    }

    public Subscription() {
    }

    public Subscription(String topicId, String labelName) {
        this.topicId = topicId;
        this.labelName = labelName;
        this.sources = Utils.allSources();
        this.digests = false;
    }

    public String getTopicId() {
        if (topicId == null) {
            return "";
        }
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public boolean isDigests() {
        return digests;
    }

    public void setDigests(boolean digests) {
        this.digests = digests;
    }
}
