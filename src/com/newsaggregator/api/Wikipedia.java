package com.newsaggregator.api;

import com.newsaggregator.base.Article;
import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.WikipediaArticle;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Wikipedia {

    public static List<WikipediaArticle> getArticles(String searchTerm) {
        ArrayList<WikipediaArticle> articles = new ArrayList<>();
        try {
            searchTerm = searchTerm.replace(' ', '+');
            URL wikipediaURL = new URL("https://www.wikipedia.org/w/api.php?action=query&format=json&errorformat=raw&prop=extracts&list=&meta=&generator=search&utf8=1&exlimit=max&exintro=1&explaintext=1&exsectionformat=plain&excontinue=&gsrlimit=10&gsrsearch=" + searchTerm);
            URLConnection wikipediaURLConnection = wikipediaURL.openConnection();
            wikipediaURLConnection.connect();
            JSONObject response = new JSONObject(IOUtils.toString(wikipediaURLConnection.getInputStream(), Charset.forName("UTF-8"))).getJSONObject("query").getJSONObject("pages");
            for (String key : response.keySet()) {
                JSONObject article = response.getJSONObject(key);
                String title = article.getString("title");
                String extract = article.getString("extract");
                if (!title.contains("(disambiguation)")) {
                    articles.add(new WikipediaArticle(Outlet.Wikipedia, title, extract));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

}
