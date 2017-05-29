package com.newsaggregator.ml.modelling;

import org.apache.log4j.Logger;

/**
 * Created by kunalwagle on 18/05/2017.
 */
public class Modeller {

    private static TopicModelling topicModelling;

    public static TopicModelling getInstance() {
        if (topicModelling == null) {
            try {
                topicModelling = new TopicModelling();
            } catch (Exception e) {
                Logger.getLogger(Modeller.class).error("Error getting topic modeller", e);
            }
        }
        return topicModelling;
    }

}
