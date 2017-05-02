package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Topics;
import com.newsaggregator.db.Users;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;
import com.newsaggregator.server.TopicHolder;
import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;

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
            List<String> topics = response.getTopicIds();
            Topics topicManager = new Topics(db);
            List<TopicHolder> topicHolders = new ArrayList<>();
            for (String topic : topics) {
                LabelHolder labelHolder = topicManager.getTopicById(topic);
                List<ClusterHolder> clusterHolders = new ArrayList<>();
                if (labelHolder != null) {
                    clusterHolders = labelHolder.getClusters();
                }
                topicHolders.add(new TopicHolder(topic, clusterHolders));
            }
            return new ObjectMapper().writeValueAsString(new UserHolder(response.getId(), response.getEmailAddress(), topicHolders));
        } catch (Exception e) {
            logger.error("Exception occurred subscribing user", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        return null;
    }

    private class UserHolder {

        private String id;
        private String emailAddress;
        private List<TopicHolder> topics;

        public UserHolder(String id, String emailAddress, List<TopicHolder> topics) {
            this.id = id;
            this.topics = topics;
            this.emailAddress = emailAddress;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getId() {
            return id;
        }

        public List<TopicHolder> getTopics() {
            return topics;
        }
    }
}
