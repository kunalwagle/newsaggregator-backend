package com.newsaggregator.server;

import org.restlet.service.TaskService;

/**
 * Created by kunalwagle on 19/05/2017.
 */
public class TaskServiceSingleton {

    private static TaskService taskService;

    public static TaskService getInstance() {
        if (taskService == null) {
            taskService = new TaskService(12);
        }
        return taskService;
    }


}
