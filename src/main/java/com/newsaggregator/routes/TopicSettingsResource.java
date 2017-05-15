package com.newsaggregator.routes;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.Subscription;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Users;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.List;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class TopicSettingsResource extends ServerResource {

    private Logger logger = Logger.getLogger(getClass());

    @Post
    public String topicSettings(Representation representation) {
        try {
            JSONObject jsonObject = new JSONObject(getRequest().getEntityAsText());
            MongoDatabase db = Utils.getDatabase();
            Users userManager = new Users(db);
            String user = jsonObject.getString("user");
            String topic = jsonObject.getString("topicId");
            boolean digest = jsonObject.getBoolean("digest");
            List<String> sources = Lists.newArrayList();
            JSONArray sourcesJSON = jsonObject.getJSONArray("sources");
            for (int i = 0; i < sourcesJSON.length(); i++) {
                sources.add(sourcesJSON.getString(i));
            }
            User currentUser = userManager.getSingleUser(user);
            Subscription subscription = currentUser.getTopicIds().stream().filter(sub -> sub.getTopicId().equals(topic)).findFirst().get();
            subscription.setDigests(digest);
            subscription.setSources(sources);
            userManager.writeUser(currentUser);
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
            return null;
        } catch (Exception e) {
            logger.error("An error occurred whilst retrieving a topic", e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return null;
        }
    }

}
