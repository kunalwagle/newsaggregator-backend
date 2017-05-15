package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Topics;
import com.newsaggregator.db.Users;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class SubscribeUserResource extends ServerResource {

    private Logger logger = Logger.getLogger(getClass());

    @Get
    public String subscribeUser() {
        String topic = (String) getRequestAttributes().get("topic");
        String user = (String) getRequestAttributes().get("user");
        MongoDatabase db = Utils.getDatabase();
        Users userManager = new Users(db);
        try {
            userManager.addTopic(user, topic);
            Topics topicManager = new Topics(db);
            LabelHolder labelHolder = topicManager.getTopicById(topic);
            if (user != null) {
                User currentUser = userManager.getSingleUser(user);
                labelHolder.setSubscribed(currentUser.getTopicIds().stream().anyMatch(sub -> sub.getTopicId().equals(topic)));
            }
            return new ObjectMapper().writeValueAsString(labelHolder);
        } catch (Exception e) {
            logger.error("Exception occurred subscribing user", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        return null;
    }
}
