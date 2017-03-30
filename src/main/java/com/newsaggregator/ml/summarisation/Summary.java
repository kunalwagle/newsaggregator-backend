package com.newsaggregator.ml.summarisation;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Graph;

import java.util.List;

/**
 * Created by kunalwagle on 30/03/2017.
 */
public class Summary {

    private Graph graph;
    private String text;
    private List<OutletArticle> articles;

    public Summary(Graph graph, String text, List<OutletArticle> articles) {
        this.graph = graph;
        this.text = text;
        this.articles = articles;

    }

}
