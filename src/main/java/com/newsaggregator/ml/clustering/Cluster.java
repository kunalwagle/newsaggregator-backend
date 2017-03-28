package com.newsaggregator.ml.clustering;

import com.newsaggregator.base.ClusterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 28/03/2017.
 */
public class Cluster<E extends ClusterItem> {

    private List<E> clusterItems;

    public Cluster(E initialItem) {
        clusterItems = new ArrayList<>();
        clusterItems.add(initialItem);
    }

    public List<E> getClusterItems() {
        return clusterItems;
    }

    public double getSimilarity(Cluster<E> secondCluster) {
        double similarityScore = 0;
        List<E> secondClusterItems = secondCluster.getClusterItems();
        for (E clusterItem : clusterItems) {
            similarityScore += secondClusterItems.stream().mapToDouble(clusterItem::getSimilarityScore).sum();
        }
        return similarityScore;
    }

    public void combine(Cluster<E> secondCluster) {
        clusterItems.addAll(secondCluster.getClusterItems());
    }
}
