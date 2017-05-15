package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.api.outlets.Guardian;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.server.LabelHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class TopicLabelRunnable implements Runnable {

    @Override
    public void run() {

        MongoDatabase db = Utils.getDatabase();
        Articles articles = new Articles(db);
        Topics topics = new Topics(db);

        try {
            TopicModelling topicModelling = new TopicModelling();

            topicModelling.trainTopics(Guardian.getArticles());

            List<OutletArticle> unlabelledArticles = articles.getUnlabelledArticles();

            Map<String, List<OutletArticle>> articleTopicMap = new HashMap<>();

            for (OutletArticle article : unlabelledArticles) {
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
            }

            for (Map.Entry<String, List<OutletArticle>> labelPair : articleTopicMap.entrySet()) {
                try {
                    String label = labelPair.getKey();
                    LabelHolder labelHolder = topics.getTopic(label);
                    if (labelHolder == null) {
                        labelHolder = new LabelHolder(label, labelPair.getValue(), new ArrayList<>());
                        labelHolder.setNeedsClustering(true);
                        topics.saveTopics(Lists.newArrayList(labelHolder));
                        break;
                    }
                    labelHolder.addArticles(labelPair.getValue());
                    labelHolder.setNeedsClustering(true);
                    topics.saveTopic(labelHolder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
