package com.newsaggregator.base;

/**
 * Created by kunalwagle on 31/01/2017.
 */
public class WikipediaArticle extends Article {

    private String extract;

    public WikipediaArticle(String source, String title, String extract, String imageUrl) {
        super(source, title, imageUrl);
        this.extract = extract;
    }

    public String getExtract() {
        return extract;
    }
}
