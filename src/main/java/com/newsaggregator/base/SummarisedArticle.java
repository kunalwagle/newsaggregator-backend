package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class SummarisedArticle extends Article {

    private List<String> originalArticles;
    private String articleBody;

    public SummarisedArticle(String title, String imageUrl, String articleBody, List<String> originalArticles) {
        super("Summarised", title, imageUrl);
        this.originalArticles = originalArticles;
        this.articleBody = articleBody;
    }


}
