package com.newsaggregator.server;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * Created by kunalwagle on 16/05/2017.
 */
public enum Connection {
    MONGO;
    private MongoClient client = null;

    private Connection() {
        try {
            client = new MongoClient(new ServerAddress("178.62.27.53", 27017));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MongoClient getClient() {
        if (client == null)
            throw new RuntimeException();
        return client;
    }
}
