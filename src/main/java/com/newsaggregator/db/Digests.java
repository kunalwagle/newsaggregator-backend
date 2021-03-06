package com.newsaggregator.db;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.DigestHolder;
import com.newsaggregator.base.DigestString;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class Digests {

    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private Logger logger = Logger.getLogger(getClass());

    public Digests(MongoDatabase database) {
        this.database = database;
        this.collection = getCollection();
    }

    private MongoCollection<Document> getCollection() {
        MongoCollection<Document> articleCollection = database.getCollection("digests", Document.class);

        if (articleCollection == null) {
            database.createCollection("digests");
            articleCollection = database.getCollection("digests", Document.class);
        }
        return articleCollection;
    }

    public long count() {
        return collection.count();
    }

    public void saveDigests(List<DigestHolder> digests) {
        for (DigestHolder digestHolder : digests) {
            try {
                Document document = digestHolder.createDocument();
                collection.insertOne(document);
            } catch (MongoWriteException e) {
                updateDigest(digestHolder);
            } catch (Exception e) {
                logger.error("Writing digest error", e);
            }
        }
    }

    private void updateDigest(DigestHolder digestHolder) {
        try {
            Document document = digestHolder.createDocument();
            collection.replaceOne(new BasicDBObject().append("_id", digestHolder.get_id()), document);
        } catch (Exception e) {
            logger.error("Updating user error", e);
        }
    }

    public DigestString getSingleDigest(String id) {
        try {
            BasicDBObject queryObject = new BasicDBObject().append("_id", new ObjectId(id));
            MongoCursor<Document> iterator = collection.find(queryObject).iterator();
            if (iterator.hasNext()) {
                Document document = iterator.next();
                String address = document.getString("emailAddress");
                DigestString digestHolder = new DigestString();
                digestHolder.setEmailAddress(address);
                digestHolder.setArticleHolders(document.getString("articleHolders"));
                digestHolder.setId(document.getObjectId("_id").toHexString());
                return digestHolder;
            }
        } catch (Exception e) {
            logger.error("An error occurred retrieving a single user", e);
        }
        return null;
    }

}
