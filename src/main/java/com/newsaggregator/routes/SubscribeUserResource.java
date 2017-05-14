package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.Subscription;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Users;
import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;

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
            User response = userManager.getSingleUser(user);
            List<Subscription> topics = response.getTopicIds();
            return new ObjectMapper().writeValueAsString(new UserHolder(response.getId(), response.getEmailAddress(), topics));
        } catch (Exception e) {
            logger.error("Exception occurred subscribing user", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        return null;
    }
}
