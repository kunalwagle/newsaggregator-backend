package com.newsaggregator;

import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.api.outlets.*;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.WikipediaArticle;
import com.newsaggregator.ml.modelling.TopicModelling;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            BusinessInsiderUK ap = new BusinessInsiderUK();
            List<OutletArticle> articles = new ArrayList<>(ap.getArticles());
            for (OutletArticle article : articles) {
                System.out.println(article.getBody());
            }
//            articles.addAll(Guardian.getArticles());
//            articles.addAll(new Independent().getArticles());
//            List<WikipediaArticle> wikipediaArticleList = new ArrayList<>(Wikipedia.getArticles("Tim Cook"));
//            wikipediaArticleList.stream().forEach(wikipediaArticle -> System.out.println(wikipediaArticle.getTitle() + ": " + wikipediaArticle.getExtract()));
            //TopicModelling.trainTopics(articles);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
