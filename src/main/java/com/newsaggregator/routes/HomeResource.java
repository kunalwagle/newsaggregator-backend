package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.Utils;
import com.newsaggregator.base.ArticleHolder;
import com.newsaggregator.base.DigestHolder;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Topics;
import com.newsaggregator.db.Users;
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
        if (!userId.equals("none")) {
            User user = new Users(Utils.getDatabase()).getSingleUser(userId);
            DigestHolder digestHolder = new DigestHolder(user, false);
            articleHolders.addAll(digestHolder.getArticleHolders());
        }
        if (articleHolders.size() == 0) {
            notLoggedIn(summaries, topics, objectMapper, articleHolders);
        }
        try {
            return objectMapper.writeValueAsString(articleHolders);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error writing article holders", e);
            return null;
        }
    }

    private void notLoggedIn(Summaries summaries, Topics topics, ObjectMapper objectMapper, List<ArticleHolder> articleHolders) {
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

}
