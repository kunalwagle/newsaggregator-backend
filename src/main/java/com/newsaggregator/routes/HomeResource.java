package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.Utils;
import com.newsaggregator.base.ArticleHolder;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.ClusterString;
import org.apache.log4j.Logger;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 01/06/2017.
 */
public class HomeResource extends ServerResource {

    @Get("json")
    public String getMostRecent() {
        Summaries summaries = new Summaries(Utils.getDatabase());
        Topics topics = new Topics(Utils.getDatabase());
        ObjectMapper objectMapper = new ObjectMapper();
        List<ArticleHolder> articleHolders = new ArrayList<>();
        String userId = (String) getRequestAttributes().get("userId");
        if (userId.equals("none")) {
            try {
                List<ClusterHolder> clusterHolderList = summaries.getMostRecent();
                for (ClusterHolder clusterHolder : clusterHolderList) {
                    ClusterString clusterString = clusterHolder.getClusterString();
                    String topicId = topics.getTopicFromCluster(objectMapper.writeValueAsString(clusterString));
                    articleHolders.add(new ArticleHolder(topicId, clusterString));
                }
            } catch (Exception e) {
                Logger.getLogger(getClass()).error("An error in the Home Resource", e);
            }
        }
        try {
            return objectMapper.writeValueAsString(articleHolders);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error writing article holders", e);
            return null;
        }
    }

}
