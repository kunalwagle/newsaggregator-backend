package com.newsaggregator.base;

/**
 * Created by kunalwagle on 13/02/2017.
 */
public class TopicLabel {

    private String label;
    private Topic topic;

    public TopicLabel(String label, Topic topic) {
        this.label = label;
        this.topic = topic;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        if (label == null) {
            return "";
        }
        return label;
    }

    public Topic getTopic() {
        return topic;
    }
}
