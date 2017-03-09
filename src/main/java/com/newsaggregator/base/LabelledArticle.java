package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 02/03/2017.
 */
public class LabelledArticle {

    private List<String> labels;
    private OutletArticle article;

    public LabelledArticle(List<String> labels, OutletArticle article) {
        this.labels = labels;
        this.article = article;
    }

    public List<String> getLabels() {
        return labels;
    }

    public OutletArticle getArticle() {
        return article;
    }
}
