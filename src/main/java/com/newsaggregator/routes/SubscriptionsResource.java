package com.newsaggregator.routes;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Topics;
import com.newsaggregator.db.Users;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.ClusterString;
import com.newsaggregator.server.LabelString;
import com.newsaggregator.server.TopicHolder;
import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 21/04/2017.
 */
public class SubscriptionsResource extends ServerResource {

    private Logger logger = Logger.getLogger(getClass());

    @Get
    public String userSubscriptions() {
        String user = (String) getRequestAttributes().get("user");
        DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
        Users userManager = new Users(db);
        try {
            User response = userManager.getSingleUser(user);
            List<String> topics = response.getTopicIds();
            Topics topicManager = new Topics(db);
            Articles articleManager = new Articles(db);
            List<TopicHolder> topicHolders = new ArrayList<>();
            for (String topic : topics) {
                LabelString labelString = topicManager.getTopic(topic);
                List<ClusterHolder> clusterHolders = new ArrayList<>();
                if (labelString != null) {
                    List<ClusterString> clusterStrings = labelString.getClusters();
                    for (ClusterString clusterString : clusterStrings) {
                        try {
                            List<String> articles = clusterString.getCluster();
                            List<OutletArticle> arts = articles.stream().map(articleManager::getSingleArticle).collect(Collectors.toList());
                            clusterHolders.add(new ClusterHolder(arts, clusterString.getNodes()));
                        } catch (Exception e) {
                            logger.error("Internal exception generating cluster holder, can continue", e);
                        }
                    }
                }
                topicHolders.add(new TopicHolder(topic, clusterHolders));
            }
            return new ObjectMapper().writeValueAsString(topicHolders);
        } catch (Exception e) {
            logger.error("Exception occurred subscribing user", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        return null;
    }
}
