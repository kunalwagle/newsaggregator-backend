package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class TopicResource extends ServerResource {

    private Logger logger = Logger.getLogger(getClass());

    @Get("json")
    public String topic() {
        String searchTerm = (String) getRequestAttributes().get("topic");
        searchTerm = searchTerm.replace("%20", " ");
        try {
            MongoDatabase db = Utils.getDatabase();
            Topics topicManager = new Topics(db);
            LabelHolder labelHolder = topicManager.getTopic(searchTerm);
            return new ObjectMapper().writeValueAsString(labelHolder.getClusters());
        } catch (Exception e) {
            logger.error("An error occurred whilst retrieving a topic", e);
            return null;
        }
    }

}
