package com.newsaggregator.base;

/**
 * Created by kunalwagle on 31/01/2017.
 */
public class Article {
    protected String title;
    protected String source;
    protected String imageUrl;

    public Article(String source, String title, String imageUrl) {
        this.source = source;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public Article() {
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
