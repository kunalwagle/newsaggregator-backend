package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.Utils;
import com.newsaggregator.base.ArticleHolder;
import com.newsaggregator.db.HomeArticles;
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
        List<ArticleHolder> articleHolders = new ArrayList<>();
        String userId = (String) getRequestAttributes().get("userId");
        ObjectMapper objectMapper = new ObjectMapper();
//        if (!userId.equals("none")) {
//            User user = new Users(Utils.getDatabase()).getSingleUser(userId);
//            DigestHolder digestHolder = new DigestHolder(user, false);
//            articleHolders.addAll(digestHolder.getArticleHolders());
//        }
        if (articleHolders.size() == 0) {
            HomeArticles articles = new HomeArticles(Utils.getDatabase());
            articleHolders = articles.getArticles();
        }
        try {
            return objectMapper.writeValueAsString(articleHolders);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error writing article holders", e);
            return null;
        }
    }

}
