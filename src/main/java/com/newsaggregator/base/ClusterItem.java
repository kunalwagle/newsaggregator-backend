package com.newsaggregator.base;

/**
 * Created by kunalwagle on 28/03/2017.
 */
public interface ClusterItem {

    double getSimilarityScore(ClusterItem otherItem);

    VectorScore getVector();

}
