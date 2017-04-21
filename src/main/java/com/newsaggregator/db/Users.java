package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.apache.log4j.Logger;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class Users {

    private final Table table;
    private DynamoDB database;
    private Logger logger = Logger.getLogger(getClass());

    public Users(DynamoDB database) {
        this.database = database;
        this.table = getCollection();
    }

    private Table getCollection() {
        return database.getTable("Users");
    }


}
