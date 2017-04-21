package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.newsaggregator.base.User;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public void addTopic(String email, String topic) {
        User user = getSingleUser(email);
        if (user == null) {
            user = new User(email, new ArrayList<>());
        }
        user.getTopics().add(topic);
        writeUser(user);
    }

    private void writeUser(User user) {
        try {
            Item item = new Item()
                    .withPrimaryKey("emailAddress", user.getEmailAddress())
                    .withList("Topics", user.getTopics());
            table.putItem(item);
        } catch (Exception e) {
            logger.error("Writing user error", e);
        }
    }

    public User getSingleUser(String email) {
        try {
            QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression("emailAddress = :email")
                    .withValueMap(new ValueMap()
                            .withString(":email", email));
            ItemCollection<QueryOutcome> items = table.query(spec);
            Iterator<Item> iterator = items.iterator();
            if (iterator.hasNext()) {
                Map<String, Object> item = iterator.next().asMap();
                List<String> topics = (List<String>) item.get("Topics");
                return new User(email, topics);
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single user", e);
        }
        return null;
    }

    private Table getCollection() {
        return database.getTable("Users");
    }


}
