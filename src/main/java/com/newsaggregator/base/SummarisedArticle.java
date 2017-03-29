package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class SummarisedArticle extends Article {

    private List<String> originalArticles;

    public SummarisedArticle(String title, String imageUrl, List<String> originalArticles) {
        super("Summarised", title, imageUrl);
        this.originalArticles = originalArticles;
    }


}
