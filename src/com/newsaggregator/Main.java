package com.newsaggregator;

import com.newsaggregator.api.outlets.Telegraph;
import com.newsaggregator.base.OutletArticle;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            Telegraph ap = new Telegraph();
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
