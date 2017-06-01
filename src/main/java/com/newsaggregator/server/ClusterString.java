package com.newsaggregator.server;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class ClusterString {

    private String id;
    private String title;
    private String imageUrl;
    private String lastPublished;

    public ClusterString(String id, String title, String imageUrl, String lastPublished) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.lastPublished = lastPublished;
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

    public String getLastPublished() {
        return lastPublished;
    }
}
