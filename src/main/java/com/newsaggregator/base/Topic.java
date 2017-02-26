package com.newsaggregator.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunalwagle on 09/02/2017.
 */
public class Topic {

    private List<TopicWord> topWords;

    public Topic(List<TopicWord> topWords) {
        this.topWords = topWords;
    }

    public List<TopicWord> getTopWords() {
        return topWords;
    }

    public List<Map> generateWordMap() {
        List<Map> wordMap = new ArrayList<>();
        for (TopicWord word : topWords) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("word", word.getWord());
            map.put("distribution", word.getDistribution());
            wordMap.add(map);
        }
        return wordMap;
    }
}
