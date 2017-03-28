package com.newsaggregator.ml.clustering;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 28/03/2017.
 */
public class Cluster<E> {

    private List<E> clusterItems;

    public Cluster(E initialItem) {
        clusterItems = new ArrayList<>();
        clusterItems.add(initialItem);
    }

}
