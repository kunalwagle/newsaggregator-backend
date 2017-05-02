package com.newsaggregator.base;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class User implements DatabaseStorage {

    //TODO: Create id field and put _id.toString in it

    private String emailAddress;
    private List<String> topicIds;
    private ObjectId _id;

    public User(String emailAddress, List<String> topicIds) {
        this.emailAddress = emailAddress;
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

    public List<String> getTopicIds() {
        return topicIds;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setTopicIds(List<String> topicIds) {
        this.topicIds = topicIds;
    }

    @Override
    public Document createDocument() {
        Document doc = new Document();
        if (_id == null) {
            this._id = new ObjectId();
        }
        doc.put("_id", _id);
        doc.put("emailAddress", emailAddress);
        doc.put("topicIds", topicIds);

        return doc;
    }
}
