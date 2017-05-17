package com.newsaggregator.server.jobs;

import com.google.common.collect.Sets;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import com.newsaggregator.server.ClusterHolder;
import org.apache.log4j.Logger;

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

        Logger logger = Logger.getLogger(getClass());

        try {

            MongoDatabase db = Utils.getDatabase();
            Summaries summaries = new Summaries(db);

            List<ClusterHolder> clusterHolders = summaries.getUnsummarisedClusters().stream().limit(25).collect(Collectors.toList());

            int counter = 1;
            int total = 0;
            for (ClusterHolder clusterHolder : clusterHolders) {
                logger.info("Summarising " + counter + " of " + clusterHolders.size() + ". Done " + total + " summaries so far");
                List<OutletArticle> articles = clusterHolder.getArticles();
                Set<OutletArticle> clusterArticles = new HashSet<>(articles);
                logger.info("Summarising " + counter + " of " + clusterHolders.size() + ". Done " + total + " summaries so far");
                Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
                List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
                logger.info("Summarising " + counter + " of " + clusterHolders.size() + ". Done " + total + " summaries so far");
                List<Summary> summs = extractives.parallelStream().map(Extractive::summarise).collect(Collectors.toList());
                clusterHolder.setSummary(summs);
                total += summs.size();
                logger.info("Summarising " + counter + " of " + clusterHolders.size() + ". Done " + total + " summaries so far");
                counter++;
            }

            summaries.updateSummaries(clusterHolders);

        } catch (Exception e) {
            logger.error("An Error in the Summarisation Runnable", e);
        }

    }

}
