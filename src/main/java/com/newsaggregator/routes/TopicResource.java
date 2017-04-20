package com.newsaggregator.routes;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.ClusterString;
import com.newsaggregator.server.LabelString;
import org.apache.log4j.Logger;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
            Topics topicManager = new Topics(db);
            Articles articleManager = new Articles(db);
            LabelString labelString = topicManager.getTopic(searchTerm);
            List<ClusterString> clusterStrings = labelString.getClusters();
            List<ClusterHolder> clusterHolders = new ArrayList<>();
            for (ClusterString clusterString : clusterStrings) {
                try {
                    List<String> articles = clusterString.getCluster();
                    List<OutletArticle> arts = articles.stream().map(articleManager::getSingleArticle).collect(Collectors.toList());
                    clusterHolders.add(new ClusterHolder(arts, clusterString.getNodes()));
                } catch (Exception e) {
                }
            }
            return new ObjectMapper().writeValueAsString(clusterHolders);
        } catch (Exception e) {
            logger.error("An error occurred whilst retrieving a topic", e);
            return null;
        }
    }

}
