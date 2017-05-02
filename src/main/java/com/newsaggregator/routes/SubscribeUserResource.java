package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.db.Users;
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
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(userManager.getSingleUser(user));
        } catch (Exception e) {
            logger.error("Exception occurred subscribing user", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        return null;
    }
}
