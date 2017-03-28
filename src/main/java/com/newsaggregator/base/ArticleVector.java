package com.newsaggregator.base;

/**
 * Created by kunalwagle on 28/03/2017.
 */
public class ArticleVector implements ClusterItem {

    private OutletArticle article;
    private VectorScore vectorScore;

    public ArticleVector(OutletArticle article, VectorScore vectorScore) {
        this.article = article;
        this.vectorScore = vectorScore;
    }

    @Override
    public double getSimilarityScore(ClusterItem otherItem) {
        return 0;
    }

}
