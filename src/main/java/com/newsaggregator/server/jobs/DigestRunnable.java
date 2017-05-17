package com.newsaggregator.server.jobs;

import com.newsaggregator.Utils;
import com.newsaggregator.base.DigestHolder;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Users;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class DigestRunnable implements Runnable {


    @Override
    public void run() {

        Users userManager = new Users(Utils.getDatabase());

        List<User> users = userManager.getAllUsers();

        List<DigestHolder> digestHolders = users.stream().map(DigestHolder::new).filter(d -> d.getTopicCount() > 0).collect(Collectors.toList());

        System.out.println("Hello");

        //TODO: Save to database and trigger emails

    }
}
