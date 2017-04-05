package com.newsaggregator.ml.summarisation;

import com.newsaggregator.base.ClusterItem;
import com.newsaggregator.ml.clustering.Cluster;

import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Combiner {

    public static String combineArticleText(Cluster<ClusterItem> cluster) {
        String combinedString = "";
        List<ClusterItem> clusterItems = cluster.getClusterItems();
        for (ClusterItem item : clusterItems) {
            combinedString += item.getText();
            combinedString += " ";
        }
        return combinedString;
    }

    public static String combineStrings(List<String> strings) {
        String combinedString = "";
        for (String string : strings) {
            combinedString += string;
            if (!(string.length() == 1 && !Character.isLetter(string.charAt(0)))) {
                combinedString += " ";
            }
        }
        return combinedString;
    }

}
