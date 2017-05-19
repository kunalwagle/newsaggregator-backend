package com.newsaggregator.server.jobs;

import com.newsaggregator.server.TaskServiceSingleton;

import java.util.concurrent.TimeUnit;

/**
 * Created by kunalwagle on 19/05/2017.
 */
public class SummarisingScheduleRunnable implements Runnable {
    @Override
    public void run() {
        TaskServiceSingleton.getInstance().schedule(new SummarisationRunnable(), 1L, TimeUnit.SECONDS);
    }
}
