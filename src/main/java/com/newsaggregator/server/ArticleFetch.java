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
        List<OutletArticle> ap = fetchAPArticles();
        List<OutletArticle> bi = fetchBusinessInsiderArticles();
        List<OutletArticle> c = fetchCricinfoArticles();
        List<OutletArticle> dm = fetchDailyMailArticles();
        List<OutletArticle> g = fetchGuardianArticles();
        List<OutletArticle> i = fetchIndependentArticles();
        List<OutletArticle> me = fetchMetroArticles();
        List<OutletArticle> mi = fetchMirrorArticles();
        List<OutletArticle> nw = fetchNewsweekArticles();
        List<OutletArticle> r = fetchReutersArticles();
        List<OutletArticle> te = fetchTelegraphArticles();
        List<OutletArticle> toi = fetchTOIArticles();
        if (ap != null) {
            articles.addAll(ap);
        }
        if (bi != null) {
            articles.addAll(bi);
        }
        if (c != null) {
            articles.addAll(c);
        }
        if (dm != null) {
            articles.addAll(dm);
        }
        if (g != null) {
            articles.addAll(g);
        }
        if (i != null) {
            articles.addAll(i);
        }
        if (me != null) {
            articles.addAll(me);
        }
        if (mi != null) {
            articles.addAll(mi);
        }
        if (nw != null) {
            articles.addAll(nw);
        }
        if (r != null) {
            articles.addAll(r);
        }
        if (te != null) {
            articles.addAll(te);
        }
        if (toi != null) {
            articles.addAll(toi);
        }
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
