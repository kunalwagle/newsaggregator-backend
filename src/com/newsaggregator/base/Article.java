package com.newsaggregator.base;

/**
 * Created by kunalwagle on 30/01/2017.
 */
public class Article {

    private String title;
    private String body;
    private String imageUrl;
    private String articleUrl;
    private Outlet source;

    public Article(String title, String body, String imageUrl, String articleUrl, Outlet source) {
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public Outlet getSource() {
        return source;
    }
}
