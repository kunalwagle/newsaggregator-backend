package com.newsaggregator.server.jobs;

import com.newsaggregator.Utils;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.LabelHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 26/04/2017.
 */
public class RemoveDuplicatesRunnable implements Runnable {

    @Override
    public void run() {
        Topics topicManager = new Topics(Utils.getDatabase());
        List<LabelHolder> topics = topicManager.getAllTopics();
        topics = topics.stream().filter(this::sizesAreZero).collect(Collectors.toList());
        topicManager.removeTopics(topics);
    }

    private boolean sizesAreZero(LabelHolder labelHolder) {
        return labelHolder.getClusters().size() == 0 && labelHolder.getArticles().size() == 0;
    }

}
