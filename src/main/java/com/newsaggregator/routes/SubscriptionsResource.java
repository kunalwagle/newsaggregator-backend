package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Users;
import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class SubscriptionsResource extends ServerResource {

    private Logger logger = Logger.getLogger(getClass());

    @Get
    public String userSubscriptions() {
        String user = (String) getRequestAttributes().get("user");
        MongoDatabase db = Utils.getDatabase();
        Users userManager = new Users(db);
        try {
            User response = userManager.getSingleUser(user);
            if (response == null) {
                response = new User(user, new ArrayList<>());
                userManager.writeUser(response);
                response = userManager.getSingleUser(user);
            }
//            List<String> topics = response.getTopicIds();
//            Topics topicManager = new Topics(db);
//            List<TopicHolder> topicHolders = new ArrayList<>();
//            for (String topic : topics) {
//                LabelHolder labelHolder = topicManager.getTopicById(topic);
//                List<ClusterHolder> clusterHolders = new ArrayList<>();
//                if (labelHolder != null) {
//                    clusterHolders = labelHolder.getClusters();
//                }
//                topicHolders.add(new TopicHolder(topic, clusterHolders));
//            }
//            return new ObjectMapper().writeValueAsString(topicHolders);
            return new ObjectMapper().writeValueAsString(response);
        } catch (Exception e) {
            logger.error("Exception occurred subscribing user", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        return null;
    }
}
