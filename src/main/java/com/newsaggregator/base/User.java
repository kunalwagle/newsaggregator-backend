package com.newsaggregator.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 21/04/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements DatabaseStorage {

    private String emailAddress;
    private List<Subscription> topicIds;
    private ObjectId _id;
    private String id;

    public User(String emailAddress, List<Subscription> topicIds) {
        this.emailAddress = emailAddress.toLowerCase();
        this.topicIds = topicIds;
    }

    public User() {
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Subscription> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Subscription> topicIds) {
        this.topicIds = topicIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Document createDocument() {
        Document doc = new Document();
        if (_id == null) {
            this._id = new ObjectId();
        }
        doc.put("_id", _id);
        doc.put("emailAddress", emailAddress);
        ObjectMapper objectMapper = new ObjectMapper();
        String topics = "[]";
        try {
            topics = objectMapper.writeValueAsString(topicIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        doc.put("topicIds", topics);

        return doc;
    }

    public void removeTopic(String topic) {
        this.topicIds = this.topicIds.stream().filter(sub -> !sub.getTopicId().equals(topic)).collect(Collectors.toList());
    }
}
