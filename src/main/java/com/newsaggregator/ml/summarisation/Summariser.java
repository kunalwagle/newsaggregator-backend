package com.newsaggregator.ml.summarisation;

import com.newsaggregator.base.ClusterItem;
import com.newsaggregator.base.SummarisedArticle;
import com.newsaggregator.ml.clustering.Cluster;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Summariser {

    public static SummarisedArticle summarise(Cluster<ClusterItem> cluster) {
        String text = Combiner.combineArticleText(cluster);
        String extractiveText = new Extractive().summarise(text);
        String abstractiveText = new Abstractive().summarise(extractiveText);
        String title = generateTitleFromArticles(cluster);
        return generateNewSummarisedArticle(abstractiveText, title, cluster);
    }

    private static String generateTitleFromArticles(Cluster<ClusterItem> cluster) {
        return "A title";
    }

    private static SummarisedArticle generateNewSummarisedArticle(String abstractiveText, String title, Cluster<ClusterItem> cluster) {
        List<String> articleIdentifiers = cluster.getClusterItems().stream().map(ClusterItem::getIdentifier).collect(Collectors.toList());
        return new SummarisedArticle(title, null, abstractiveText, articleIdentifiers);
    }

}
