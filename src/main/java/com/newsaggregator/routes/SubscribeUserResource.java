package com.newsaggregator.routes;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
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
        DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
        Users userManager = new Users(db);
        try {
            userManager.addTopic(user, topic);
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
        } catch (Exception e) {
            logger.error("Exception occurred subscribing user", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        return null;
    }
}
