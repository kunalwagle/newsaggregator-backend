package com.newsaggregator.api;

import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.WikipediaArticle;
import org.apache.commons.io.IOUtils;
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
