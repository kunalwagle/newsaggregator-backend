package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 09/02/2017.
 */
public class Topic {

    private List<TopicWord> topWords;

    public Topic(List<TopicWord> topWords) {
        this.topWords = topWords;
    }

}
