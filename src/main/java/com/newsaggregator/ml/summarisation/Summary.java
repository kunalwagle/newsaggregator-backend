package com.newsaggregator.ml.summarisation;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Node;

import java.util.List;

/**
 * Created by kunalwagle on 30/03/2017.
 */
public class Summary {

    private List<Node> nodes;
    private String text;
    private List<OutletArticle> articles;

    public Summary(List<Node> nodes, String text, List<OutletArticle> articles) {
        this.nodes = nodes;
        this.text = text;
        this.articles = articles;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public String getText() {
        return text;
    }

    public List<OutletArticle> getArticles() {
        return articles;
    }

    @Override
    public boolean equals(Object o) {
        List<OutletArticle> others = ((Summary) o).getArticles();
        if (others.size() == articles.size()) {
            for (OutletArticle article : articles) {
                if (others.stream().noneMatch(art -> art.getArticleUrl().equals(article.getArticleUrl()))) {
                    return false;
                }
            }
        }
        return true;
    }
}
