package com.newsaggregator.base;

/**
 * Created by kunalwagle on 30/01/2017.
 */
public class OutletArticle extends Article {

    private String body;
    private String imageUrl;
    private String articleUrl;

    public OutletArticle(String title, String body, String imageUrl, String articleUrl, Outlet source) {
        super(source, title);
        this.body = body;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
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
