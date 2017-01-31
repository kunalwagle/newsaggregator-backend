package com.newsaggregator.base;

/**
 * Created by kunalwagle on 31/01/2017.
 */
public class WikipediaArticle extends Article {

    private String extract;

    public WikipediaArticle(Outlet source, String title, String extract) {
        super(source, title);
        this.extract = extract;
    }

    public String getExtract() {
        return extract;
    }
}
