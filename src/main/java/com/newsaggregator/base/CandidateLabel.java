package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 24/02/2017.
 */
public class CandidateLabel {

    private String label;
    private List<Outlink> outlinks;
    private String articleBody;

    public CandidateLabel(String label, WikipediaArticle wikipediaArticle, List<Outlink> outlinks) {
        this.label = label;
        this.outlinks = outlinks;
        this.articleBody = wikipediaArticle.getExtract();
    }

    public CandidateLabel(String label, WikipediaArticle wikipediaArticle) {
        this.label = label;
        this.articleBody = wikipediaArticle.getExtract();
    }

    public List<Outlink> getOutlinks() {
        return outlinks;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public String getLabel() {
        return label;
    }
}
