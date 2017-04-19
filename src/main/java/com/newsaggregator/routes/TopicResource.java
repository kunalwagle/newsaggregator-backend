package com.newsaggregator.routes;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.newsaggregator.db.Topics;
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
        try {
            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
            Topics topicManager = new Topics(db);

            return null;
        } catch (Exception e) {
            logger.error("An error occurred whilst retrieving a topic", e);
            return null;
        }
    }

}
