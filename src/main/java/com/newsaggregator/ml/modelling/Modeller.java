package com.newsaggregator.ml.modelling;

import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Articles;
import com.newsaggregator.server.ArticleFetch;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by kunalwagle on 18/05/2017.
 */
public class Modeller {

    private static TopicModelling topicModelling;

    public static TopicModelling getInstance() {
        if (topicModelling == null) {
            try {
                topicModelling = new TopicModelling();
                MongoDatabase db = Utils.getDatabase();
                Articles articles = new Articles(db);
                List<OutletArticle> modelArticles = articles.getAllArticles();

                if (modelArticles.size() == 0) {
                    modelArticles = ArticleFetch.fetchArticles();
                }

                topicModelling.trainTopics(modelArticles);

            } catch (Exception e) {
                Logger.getLogger(Modeller.class).error("Error getting topic modeller", e);
            }
        }
        return topicModelling;
    }

}
