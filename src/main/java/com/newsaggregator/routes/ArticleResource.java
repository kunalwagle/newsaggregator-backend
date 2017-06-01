package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Subscription;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Users;
import com.newsaggregator.server.ClusterHolder;
import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 01/06/2017.
 */
public class ArticleResource extends ServerResource {

    @Get("json")
    public String getArticle() {
        String topicId = (String) getRequestAttributes().get("topicId");
        String articleId = (String) getRequestAttributes().get("articleId");
        String userId = (String) getRequestAttributes().get("user");

        ClusterHolder clusterHolder = new Summaries(Utils.getDatabase()).getSingleCluster(articleId);

        String defaultString = clusterHolder.getArticles().stream().map(OutletArticle::getArticleUrl).sorted().collect(Collectors.toList()).toString().replace(" ", "");

        if (!userId.equals("none")) {
            Users users = new Users(Utils.getDatabase());
            try {
                User user = users.getSingleUser(userId);
                Optional<Subscription> subOptional = user.getTopicIds().stream().filter(t -> t.getTopicId().equals(topicId)).findFirst();
                if (subOptional.isPresent()) {
                    Subscription subscription = subOptional.get();
                    List<String> sources = subscription.getSources();
                    List<OutletArticle> defaults = clusterHolder.getArticles().stream().filter(a -> sources.contains(a.getSource())).collect(Collectors.toList());
                    defaultString = defaults.stream().map(OutletArticle::getArticleUrl).sorted().collect(Collectors.toList()).toString().replace(" ", "");
                }
            } catch (Exception e) {
                Logger.getLogger(getClass()).error("Got an error retrieving an article", e);
            }
        }

        ClusterWithSettings clusterWithSettings = new ClusterWithSettings(clusterHolder, defaultString);

        try {
            return new ObjectMapper().writeValueAsString(clusterWithSettings);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error occurred whilst retrieving an article", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return null;
        }

    }

    private class ClusterWithSettings {

        private ClusterHolder clusterHolder;
        private String defaultString;

        public ClusterWithSettings(ClusterHolder clusterHolder, String defaultString) {
            this.clusterHolder = clusterHolder;
            this.defaultString = defaultString;
        }

        public ClusterHolder getClusterHolder() {
            return clusterHolder;
        }

        public String getDefaultString() {
            return defaultString;
        }
    }

}
