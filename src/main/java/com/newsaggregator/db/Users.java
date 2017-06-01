package com.newsaggregator.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.Subscription;
import com.newsaggregator.base.User;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class Users {

    private final MongoCollection<Document> collection;
    private MongoDatabase database;
    private Logger logger = Logger.getLogger(getClass());

    public Users(MongoDatabase database) {
        this.database = database;
        this.collection = getCollection();
    }

    public void addTopic(String email, String topicId) {
        User user = getSingleUser(email);
        if (user == null) {
            user = new User(email, new ArrayList<>());
        }
        if (user.getTopicIds().stream().noneMatch(sub -> sub.getTopicId().equals(topicId))) {
            user.getTopicIds().add(new Subscription(topicId));
        }
        writeUser(user);
    }

    public void writeUser(User user) {
        try {
            Document document = user.createDocument();
            collection.insertOne(document);
        } catch (MongoWriteException e) {
            updateUser(user);
        } catch (Exception e) {
            logger.error("Writing user error", e);
        }
    }

    private void updateUser(User user) {
        try {
            Document document = user.createDocument();
            collection.replaceOne(new BasicDBObject().append("_id", user.get_id()), document);
        } catch (Exception e) {
            logger.error("Updating user error", e);
        }
    }

    public User getSingleUser(String email) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("emailAddress", email);
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            if (iterator.hasNext()) {
                Document document = iterator.next();
                String address = document.getString("emailAddress");
                String topicIds = document.getString("topicIds");
                ObjectMapper objectMapper = new ObjectMapper();
                List<Subscription> subs = objectMapper.readValue(topicIds, objectMapper.getTypeFactory().constructCollectionType(List.class, Subscription.class));
                User user = new User(address, subs);
                user.set_id(document.getObjectId("_id"));
                user.setId(user.get_id().toHexString());
                return user;
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single user", e);
        }
        return null;
    }

    public User getUserFromId(String id) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("_id", id);
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            if (iterator.hasNext()) {
                String item = iterator.next().toJson();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(item, User.class);
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single user", e);
        }
        return null;
    }

    public List<User> getAllUsers() {

        MongoCursor<Document> iterator = collection.find().iterator();

        List<User> users = new ArrayList<>();

        while (iterator.hasNext()) {
            try {
                Document document = iterator.next();
                String address = document.getString("emailAddress");
                String topicIds = document.getString("topicIds");
                ObjectMapper objectMapper = new ObjectMapper();
                List<Subscription> subs = objectMapper.readValue(topicIds, objectMapper.getTypeFactory().constructCollectionType(List.class, Subscription.class));
                User user = new User(address, subs);
                user.set_id(document.getObjectId("_id"));
                user.setId(user.get_id().toHexString());
                users.add(user);
            } catch (Exception e) {
                logger.error("An error occurred retrieving a single user", e);
            }
        }

        return users;
    }

    public void purgeUsers() {
        List<User> users = getAllUsers();
        users.forEach(user -> user.setTopicIds(new ArrayList<>()));
        users.forEach(this::updateUser);
    }


    private MongoCollection<Document> getCollection() {
        return database.getCollection("Users");
    }


    public void removeTopic(String user, String topic) {
        User thisUser = getSingleUser(user);
        thisUser.removeTopic(topic);
        writeUser(thisUser);
    }
}
