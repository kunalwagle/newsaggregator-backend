package com.newsaggregator.server;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class ClusterString {

    private String id;
    private String title;
    private String imageUrl;

    public ClusterString(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public ClusterString() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
