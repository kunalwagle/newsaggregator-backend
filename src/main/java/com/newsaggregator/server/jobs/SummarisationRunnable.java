package com.newsaggregator.server.jobs;

import com.google.common.collect.Sets;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.ClusterHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 16/05/2017.
 */
public class SummarisationRunnable implements Runnable {

    @Override
    public void run() {

        try {

            MongoDatabase db = Utils.getDatabase();
            Summaries summaries = new Summaries(db);

            List<ClusterHolder> clusterHolders = summaries.getUnsummarisedClusters();

            for (ClusterHolder clusterHolder : clusterHolders) {
                List<OutletArticle> articles = clusterHolder.getArticles();
                Set<OutletArticle> clusterArticles = new HashSet<>(articles);
                Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
                List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
                List<Summary> summs = extractives.parallelStream().map(Extractive::summarise).collect(Collectors.toList());
                clusterHolder.setSummary(summs.stream().map(Summary::getNodes).collect(Collectors.toList()));
            }

            summaries.updateSummaries(clusterHolders);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
