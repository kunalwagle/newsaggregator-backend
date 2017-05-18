package com.newsaggregator.api;

import com.newsaggregator.Utils;
import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.Outlink;
import com.newsaggregator.base.WikipediaArticle;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.LabelHolder;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Wikipedia {

    public static List<WikipediaArticle> getArticles(String searchTerm, int total) {
//        ArrayList<WikipediaArticle> articles = queryAndParseArticles(searchTerm, null);
        ArrayList<String> titles = titles(searchTerm);
        List<WikipediaArticle> articles = titles.stream().limit(total).collect(Collectors.toList()).stream().map(Wikipedia::convertToArticle).filter(Objects::nonNull).collect(Collectors.toList());
        Topics topicManager = new Topics(Utils.getDatabase());
        for (WikipediaArticle article : articles) {
            LabelHolder labelHolder = topicManager.getTopic(article.getTitle());
            if (labelHolder == null) {
                labelHolder = topicManager.createBlankTopic(article.getTitle());
            }
            if (labelHolder.getImageUrl() == null) {
                labelHolder.setImageUrl(article.getImageUrl());
                topicManager.saveTopic(labelHolder);
            }
            article.set_id(labelHolder.get_id().toString());
        }
        return articles;
    }

    public static String getNearMatchArticle(String searchTerm) {
        List<String> articles = titles(searchTerm);
        if (articles.size() > 0) {
            return articles.get(0);
        }
        return null;
    }

    public static List<Outlink> getOutlinksAndCategories(String searchTerm) {
        try {
            searchTerm = searchTerm.replace(' ', '+');
            searchTerm = searchTerm.replace("%20", "+");
            URL wikipediaURL = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&generator=links&prop=categories&gpllimit=20&cllimit=max&redirects=1&titles=" + searchTerm);
            URLConnection wikipediaURLConnection = wikipediaURL.openConnection();
            wikipediaURLConnection.connect();
            JSONObject response = new JSONObject(IOUtils.toString(wikipediaURLConnection.getInputStream(), Charset.forName("UTF-8")));
            boolean finished = false;
            List<Outlink> outlinks = new ArrayList<>();
            do {
                outlinks.addAll(getOutlinks(response.getJSONObject("query").getJSONObject("pages")));
                if (response.has("continue")) {
                    wikipediaURL = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&generator=links&prop=categories&gpllimit=20&redirects=1&cllimit=max&titles=" + searchTerm + "&gplcontinue=" + response.getJSONObject("continue").getString("gplcontinue"));
                    wikipediaURLConnection = wikipediaURL.openConnection();
                    wikipediaURLConnection.connect();
                    response = new JSONObject(IOUtils.toString(wikipediaURLConnection.getInputStream(), Charset.forName("UTF-8")));
                } else {
                    finished = true;
                }
            } while (!finished);
            return outlinks;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return null;
    }

    private static List<Outlink> getOutlinks(JSONObject response) {
        List<Outlink> outlinks = new ArrayList<>();
        for (String key : response.keySet()) {
            try {
                JSONObject outlink = response.getJSONObject(key);
                String title = outlink.getString("title");
                JSONArray categories = outlink.getJSONArray("categories");
                List<String> cats = new ArrayList<>();
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject cat = categories.getJSONObject(i);
                    cats.add(cat.getString("title"));
                }
                outlinks.add(new Outlink(title, cats));
            } catch (JSONException e) {

            }
        }
        return outlinks;
    }

    private static ArrayList<String> titles(String searchTerm) {
        ArrayList<String> result = new ArrayList<>();
        try {
            searchTerm = searchTerm.replace(' ', '+');
            searchTerm = searchTerm.replace("%20", "+");
            URL wikipediaURL = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&indexpageids=1&srlimit=25&srsearch=" + searchTerm);
            URLConnection wikipediaURLConnection = wikipediaURL.openConnection();
            wikipediaURLConnection.connect();
            JSONArray response = new JSONObject(IOUtils.toString(wikipediaURLConnection.getInputStream(), Charset.forName("UTF-8"))).getJSONObject("query").getJSONArray("search");
            for (int i = 0; i < response.length(); i++) {
                JSONObject searchResult = response.getJSONObject(i);
                result.add(searchResult.getString("title"));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }

    private static WikipediaArticle convertToArticle(String title) {
        try {
            title = title.replace(' ', '+');
            title = title.replace("%20", "+");
            URL wikipediaURL = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts%7Cpageimages&exchars=300&exintro=1&explaintext=1&piprop=original&titles=" + title);
            URLConnection wikipediaURLConnection = wikipediaURL.openConnection();
            wikipediaURLConnection.connect();
            JSONObject response = new JSONObject(IOUtils.toString(wikipediaURLConnection.getInputStream(), Charset.forName("UTF-8"))).getJSONObject("query").getJSONObject("pages");
            WikipediaArticle article = null;
            for (String key : response.keySet()) {
                article = extractArticleFromJSON(response.getJSONObject(key));
            }
            return article;
        } catch (Exception e) {
            Logger.getLogger(Wikipedia.class).error("An error in Wikipedia", e);
//            e.printStackTrace();
        }
        return null;
    }

    private static WikipediaArticle extractArticleFromJSON(JSONObject article) {
        String title = article.getString("title");
        String extract = article.getString("extract");
        String imageUrl = "http://larics.fer.hr/wp-content/uploads/2016/04/default-placeholder.png";
        try {
            imageUrl = article.getJSONObject("thumbnail").getString("original");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        if (!title.contains("(disambiguation)")) {
            return new WikipediaArticle(Outlet.Wikipedia.getSourceString(), title, extract, imageUrl);
        }
        return null;
    }

    private static ArrayList<WikipediaArticle> queryAndParseArticles(String searchTerm, String searchType) {
        ArrayList<WikipediaArticle> articles = new ArrayList<>();
        try {
            searchTerm = searchTerm.replace(' ', '+');
            searchTerm = searchTerm.replace("%20", "+");
            if (searchType != null) {
                searchTerm = searchTerm + "&gsrwhat=" + searchType;
            }
            URL wikipediaURL = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&generator=search&gsrnamespace=0&gsrlimit=10&prop=pageimages|extracts&piprop=original&pilimit=max&exintro&explaintext&exsentences=3&exlimit=max&gsrsearch=intitle:" + searchTerm);
            URLConnection wikipediaURLConnection = wikipediaURL.openConnection();
            wikipediaURLConnection.connect();
            JSONObject response = new JSONObject(IOUtils.toString(wikipediaURLConnection.getInputStream(), Charset.forName("UTF-8"))).getJSONObject("query").getJSONObject("pages");
            for (String key : response.keySet()) {
                WikipediaArticle article = extractArticleFromJSON(response.getJSONObject(key));
                if (article != null) {
                    articles.add(article);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return articles;
    }

}
