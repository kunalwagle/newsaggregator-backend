package com.newsaggregator.server.jobs;

import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Articles;
import com.newsaggregator.server.TaskServiceSingleton;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kunalwagle on 19/05/2017.
 */
public class LabellingRunnable implements Runnable {


    @Override
    public void run() {
        Articles articles = new Articles(Utils.getDatabase());
        List<OutletArticle> articleList = articles.getUnlabelledArticles();
        TaskServiceSingleton.getInstance().schedule(new TopicLabelRunnable(articleList), 1L, TimeUnit.SECONDS);
    }

}

