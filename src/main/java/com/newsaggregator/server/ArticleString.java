package com.newsaggregator.server;

/**
 * Created by kunalwagle on 01/06/2017.
 */
public class ArticleString {

    public String id;
    public String articleUrl;
    public String source;

    public ArticleString(String id, String articleUrl, String source) {
        this.id = id;
        this.articleUrl = articleUrl;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getSource() {
        return source;
    }
}
