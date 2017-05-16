package com.newsaggregator.server.jobs;

import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.server.ClusterHolder;

import java.util.List;

/**
 * Created by kunalwagle on 16/05/2017.
 */
public class SummarisationRunnable implements Runnable {

    @Override
    public void run() {

        MongoDatabase db = Utils.getDatabase();
        Summaries summaries = new Summaries(db);

        List<ClusterHolder> clusterHolders = summaries.getUnsummarisedClusters();

    }

}
