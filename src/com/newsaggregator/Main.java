package com.newsaggregator;

import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.api.outlets.Guardian;
import com.newsaggregator.api.outlets.Independent;
import com.newsaggregator.base.WikipediaArticle;
import com.newsaggregator.ml.modelling.TopicModelling;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            Independent independent = new Independent();
            independent.getArticles();
//            List<WikipediaArticle> wikipediaArticleList = new ArrayList<>(Wikipedia.getArticles("Tim Cook"));
//            wikipediaArticleList.stream().forEach(wikipediaArticle -> System.out.println(wikipediaArticle.getTitle() + ": " + wikipediaArticle.getExtract()));
//            TopicModelling.trainTopics(Guardian.getArticles());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
