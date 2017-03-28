package com.newsaggregator.server;

import com.newsaggregator.api.outlets.*;
import com.newsaggregator.base.OutletArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 04/02/2017.
 */
public class ArticleFetch {

    public static List<OutletArticle> fetchArticles() {
        List<OutletArticle> articles = new ArrayList<>();
        articles.addAll(fetchAPArticles());
        articles.addAll(fetchBusinessInsiderArticles());
        articles.addAll(fetchCricinfoArticles());
        articles.addAll(fetchDailyMailArticles());
//        articles.addAll(fetchGuardianArticles());
        articles.addAll(fetchIndependentArticles());
        articles.addAll(fetchMetroArticles());
        articles.addAll(fetchMirrorArticles());
        articles.addAll(fetchNewsweekArticles());
        articles.addAll(fetchReutersArticles());
        articles.addAll(fetchSSNArticles());
        articles.addAll(fetchTelegraphArticles());
        articles.addAll(fetchTOIArticles());
        return articles;
    }

    private static List<OutletArticle> fetchAPArticles() {
        return new AssociatedPress().getArticles();
    }

    private static List<OutletArticle> fetchBusinessInsiderArticles() {
        return new BusinessInsiderUK().getArticles();
    }

    private static List<OutletArticle> fetchCricinfoArticles() {
        return new Cricinfo().getArticles();
    }

    private static List<OutletArticle> fetchDailyMailArticles() {
        return new DailyMail().getArticles();
    }

    private static List<OutletArticle> fetchGuardianArticles() {
        return Guardian.getArticles();
    }

    private static List<OutletArticle> fetchIndependentArticles() {
        return new Independent().getArticles();
    }

    private static List<OutletArticle> fetchMetroArticles() {
        return new Metro().getArticles();
    }

    private static List<OutletArticle> fetchMirrorArticles() {
        return new Mirror().getArticles();
    }

    private static List<OutletArticle> fetchNewsweekArticles() {
        return new Newsweek().getArticles();
    }

    private static List<OutletArticle> fetchReutersArticles() {
        return new Reuters().getArticles();
    }

    private static List<OutletArticle> fetchSSNArticles() {
        return new SkySportsNews().getArticles();
    }

    private static List<OutletArticle> fetchTelegraphArticles() {
        return new Telegraph().getArticles();
    }

    private static List<OutletArticle> fetchTOIArticles() {
        return new TOI().getArticles();
    }

}
