package com.newsaggregator.base;

/**
 * Created by kunalwagle on 31/01/2017.
 */
public class Article {
    protected String title;
    protected String source;

    public Article(String source, String title) {
        this.source = source;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
