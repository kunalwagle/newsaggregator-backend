package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Topics;
import com.newsaggregator.db.Users;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by kunalwagle on 14/05/2017.
 */
public class TopicUserResource extends ServerResource {

    private Logger logger = Logger.getLogger(getClass());

    @Get("json")
    public String topic() {
        String searchTerm = (String) getRequestAttributes().get("topic");
        String user = (String) getRequestAttributes().get("user");
        try {
            MongoDatabase db = Utils.getDatabase();
            Topics topicManager = new Topics(db);
            LabelHolder labelHolder = topicManager.getTopicById(searchTerm);
            if (user != null) {
                Users userManager = new Users(db);
                User currentUser = userManager.getSingleUser(user);
                labelHolder.setSubscribed(currentUser.getTopicIds().stream().anyMatch(sub -> sub.getTopicId().equals(searchTerm)));
            }
            return new ObjectMapper().writeValueAsString(labelHolder);
        } catch (Exception e) {
            logger.error("An error occurred whilst retrieving a topic", e);
            return null;
        }
    }

}
