package com.newsaggregator;

import com.newsaggregator.api.outlets.Guardian;
import com.newsaggregator.ml.modelling.TopicModelling;

public class Main {

    public static void main(String[] args) {
        try {
            TopicModelling.trainTopics(Guardian.getArticles());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
