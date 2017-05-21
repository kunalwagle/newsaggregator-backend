package com.newsaggregator.server.jobs;

import com.newsaggregator.server.TaskServiceSingleton;
import org.restlet.service.TaskService;

import java.util.concurrent.TimeUnit;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class ArticleRunnable implements Runnable {
    @Override
    public void run() {
        TaskService taskService = TaskServiceSingleton.getInstance();
        taskService.schedule(new ArticleFetchRunnable(), 1L, TimeUnit.SECONDS);
    }
}
