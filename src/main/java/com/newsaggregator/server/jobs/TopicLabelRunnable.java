package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class TopicLabelRunnable implements Runnable {

    @Override
    public void run() {

        Logger logger = Logger.getLogger(getClass());

        MongoDatabase db = Utils.getDatabase();
        Articles articles = new Articles(db);
        Topics topics = new Topics(db);

        try {
            TopicModelling topicModelling = new TopicModelling();

            List<OutletArticle> unlabelledArticles = articles.getUnlabelledArticles().stream().limit(15).collect(Collectors.toList());

            int counter = 1;

            for (OutletArticle article : unlabelledArticles) {
                if (article.getBody() == null || article.getBody().length() == 0) {
                    article.setLabelled(true);
                    articles.updateArticles(Lists.newArrayList(article));
                    continue;
                }
                logger.info("Labelling " + counter + " of " + unlabelledArticles.size());
                logger.info(article.getId());
                try {
                    Topic topic = topicModelling.getModel(article);
                    List<String> topicLabels = TopicLabelling.generateTopicLabel(topic, article);
                    logger.info("Labelling " + counter + " of " + unlabelledArticles.size());
                    if (topicLabels != null) {
                        for (String topicLabel : topicLabels) {
                            try {
                                LabelHolder labelHolder = topics.getTopic(topicLabel);
                                if (labelHolder == null) {
                                    labelHolder = new LabelHolder(topicLabel);
                                }
                                labelHolder.addArticle(article);
                                labelHolder.setNeedsClustering(true);
                                article.setLabelled(true);
                                topics.saveTopic(labelHolder);
                                articles.updateArticles(Lists.newArrayList(article));
                            } catch (Exception e) {
                                logger.error("An error in the saving part of a topic label. Moving on", e);
                            }
                        }
                    }
                    logger.info("Labelling " + counter + " of " + unlabelledArticles.size());
                } catch (Exception e) {
                    logger.error("An error in the labelling part of a topic. Moving on", e);
                }
                counter++;
            }

        } catch (Exception e) {
            logger.error("An Error in the Topic Label Runnable", e);
        }

    }
}
