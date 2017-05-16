package com.newsaggregator.server.jobs;

import com.google.common.collect.Sets;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.ClusterHolder;

import java.text.SimpleDateFormat;
import java.util.*;
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

            int counter = 1;
            int total = 0;
            for (ClusterHolder clusterHolder : clusterHolders) {
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                System.out.println(timeStamp + " Summarising " + counter + " of " + clusterHolders.size() + ". Done " + total + " summaries so far");
                List<OutletArticle> articles = clusterHolder.getArticles();
                Set<OutletArticle> clusterArticles = new HashSet<>(articles);
                Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
                List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
                List<Summary> summs = extractives.parallelStream().map(Extractive::summarise).collect(Collectors.toList());
                clusterHolder.setSummary(summs.stream().map(Summary::getNodes).collect(Collectors.toList()));
                total += summs.size();
                counter++;
            }

            summaries.updateSummaries(clusterHolders);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
