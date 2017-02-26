package com.newsaggregator.api;

import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.Outlink;
import com.newsaggregator.base.WikipediaArticle;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Wikipedia {

    public static ArrayList<WikipediaArticle> getArticles(String searchTerm) {
        return queryAndParseArticles(searchTerm, null);
    }

    public static WikipediaArticle getNearMatchArticle(String searchTerm) {
        List<WikipediaArticle> articles = queryAndParseArticles(searchTerm, "nearmatch");
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
            e.printStackTrace();
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

    private static ArrayList<WikipediaArticle> queryAndParseArticles(String searchTerm, String searchType) {
        ArrayList<WikipediaArticle> articles = new ArrayList<>();
        try {
            searchTerm = searchTerm.replace(' ', '+');
            searchTerm = searchTerm.replace("%20", "+");
            if (searchType != null) {
                searchTerm = searchTerm + "&gsrwhat=" + searchType;
            }
            URL wikipediaURL = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&generator=search&gsrnamespace=0&gsrlimit=10&prop=pageimages|extracts&piprop=original&pilimit=max&exintro&explaintext&exsentences=3&exlimit=max&gsrsearch=" + searchTerm);
            URLConnection wikipediaURLConnection = wikipediaURL.openConnection();
            wikipediaURLConnection.connect();
            JSONObject response = new JSONObject(IOUtils.toString(wikipediaURLConnection.getInputStream(), Charset.forName("UTF-8"))).getJSONObject("query").getJSONObject("pages");
            for (String key : response.keySet()) {
                JSONObject article = response.getJSONObject(key);
                String title = article.getString("title");
                String extract = article.getString("extract");
                String imageUrl = "http://larics.fer.hr/wp-content/uploads/2016/04/default-placeholder.png";
                try {
                    imageUrl = article.getJSONObject("thumbnail").getString("original");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!title.contains("(disambiguation)")) {
                    articles.add(new WikipediaArticle(Outlet.Wikipedia.getSourceString(), title, extract, imageUrl));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

}
