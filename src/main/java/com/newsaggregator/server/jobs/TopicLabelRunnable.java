package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.Modeller;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.ml.nlp.apache.NLPSingleton;
import com.newsaggregator.server.LabelHolder;
import com.newsaggregator.server.TaskServiceSingleton;
import org.apache.log4j.Logger;
import org.restlet.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class TopicLabelRunnable implements Runnable {

    private List<OutletArticle> articleList;
    private boolean oldArticles;

    public TopicLabelRunnable(List<OutletArticle> articleList, boolean oldArticles) {
        this.articleList = articleList;
        this.oldArticles = oldArticles;
    }

    @Override
    public void run() {

        Logger logger = Logger.getLogger(getClass());

        Utils.printActiveThreads();

        logger.info("Starting up the Topic Labelling runnable");

        MongoDatabase db = Utils.getDatabase();
        Articles articleManager = new Articles(db);
        Topics topics = new Topics(db);

        List<String> labelStrings = new ArrayList<>();

        try {

            int counter = 0;

            TopicModelling modelling = Modeller.getInstance();

            for (OutletArticle article : articleList) {
                counter++;
                logger.info("Labelling " + counter + " of " + articleList.size());
                logger.info("Article id: " + article.getId());
                if (article.getBody() == null || article.getBody().length() == 0) {
                    article.setLabelled(true);
                    articleManager.updateArticles(Lists.newArrayList(article));
                    continue;
                }

                Topic topic = modelling.getModel(article);
                List<String> topicLabels = TopicLabelling.generateTopicLabel(topic, article);

                if (topicLabels != null) {
                    int number = 0;
                    for (String topicLabel : topicLabels) {
                        try {
                            number++;
                            logger.info("Setting " + number + " of " + topicLabels.size());
                            LabelHolder labelHolder = topics.getTopic(topicLabel);
                            if (labelHolder == null) {
                                labelHolder = topics.createBlankTopic(topicLabel);
                            }
                            labelHolder.addArticle(article);
                            labelHolder.setNeedsClustering(true);
                            article.setLabelled(true);
                            topics.saveTopic(labelHolder);
                            articleManager.updateArticles(Lists.newArrayList(article));
                            labelStrings.add(topicLabel);
                        } catch (Exception e) {
                            logger.error("An error in the saving part of a topic label. Moving on", e);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Caught an exception labelling new articles", e);
        }

        TaskService taskService = TaskServiceSingleton.getInstance();

        NLPSingleton.removeInstance();

        if (labelStrings.size() > 0) {
            taskService.schedule(new ClusteringRunnable(labelStrings, articleList, oldArticles), 1L, TimeUnit.SECONDS);
        }

    }
}
