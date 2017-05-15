package com.newsaggregator.server.jobs;

import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class ClusteringRunnable implements Runnable {


    @Override
    public void run() {

        MongoDatabase db = Utils.getDatabase();
        Summaries summaries = new Summaries(db);
        Topics topics = new Topics(db);

        List<LabelHolder> labelHolders = topics.getClusteringTopics();

        List<String> clusterIds = labelHolders.stream().map(lh -> lh.getClusters().stream().map(ClusterHolder::getId).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());


    }

}

