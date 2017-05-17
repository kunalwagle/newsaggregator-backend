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
import com.newsaggregator.server.ArticleFetch;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            List<OutletArticle> modelArticles = articles.getAllArticles();

            if (modelArticles.size() == 0) {
                modelArticles = ArticleFetch.fetchArticles();
            }

            topicModelling.trainTopics(modelArticles);

            List<OutletArticle> unlabelledArticles = articles.getUnlabelledArticles().stream().limit(15).collect(Collectors.toList());

            Map<String, List<OutletArticle>> articleTopicMap = new HashMap<>();

            int counter = 1;

            for (OutletArticle article : unlabelledArticles) {
                logger.info("Labelling " + counter + " of " + unlabelledArticles.size());
                Topic topic = topicModelling.getModel(article);
                List<String> topicLabels = TopicLabelling.generateTopicLabel(topic, article);
                for (String topicLabel : topicLabels) {
                    List<OutletArticle> arts = articleTopicMap.get(topicLabel);
                    if (arts == null) {
                        arts = new ArrayList<>();
                    }
                    arts.add(article);
                    articleTopicMap.put(topicLabel, arts);
                }
                counter++;
            }

            counter = 1;
            for (Map.Entry<String, List<OutletArticle>> labelPair : articleTopicMap.entrySet()) {
                logger.info("Saving " + counter + " of " + articleTopicMap.entrySet().size());
                try {
                    String label = labelPair.getKey();
                    LabelHolder labelHolder = topics.getTopic(label);
                    if (labelHolder == null) {
                        logger.info(" 1 Saving " + counter + " of " + articleTopicMap.entrySet().size());
                        labelHolder = new LabelHolder(label, labelPair.getValue(), new ArrayList<>());
                        labelHolder.setNeedsClustering(true);
                        topics.saveTopics(Lists.newArrayList(labelHolder));
                        counter++;
                        continue;
                    }
                    logger.info("2 Saving " + counter + " of " + articleTopicMap.entrySet().size());
                    labelHolder.addArticles(labelPair.getValue());
                    labelHolder.setNeedsClustering(true);
                    topics.saveTopic(labelHolder);
                } catch (Exception e) {
                    logger.error("An Error in the Topic Label Runnable", e);
                }
                counter++;
            }

            unlabelledArticles.forEach(article -> article.setLabelled(true));

            articles.updateArticles(unlabelledArticles);

        } catch (Exception e) {
            logger.error("An Error in the Topic Label Runnable", e);
        }

    }
}
