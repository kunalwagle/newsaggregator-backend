package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.OutletArticle;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 01/02/2017.
 */
public abstract class NewsAPI {

    protected Outlet outletName;

    public NewsAPI(Outlet outletName) {
        this.outletName = outletName;
    }

    public List<OutletArticle> getArticles() {
        try {
            JSONArray response = fetchArticles();
            return parseAndCollectArticles(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<OutletArticle> parseAndCollectArticles(JSONArray response) throws IOException {
        List<OutletArticle> result = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject articleJSON = response.getJSONObject(i);
            String title = articleJSON.getString("title");
            String articleURL = articleJSON.getString("url");
            String imageURL;
            try {
                imageURL = articleJSON.getString("urlToImage");
            } catch (JSONException e) {
                imageURL = "";
            }
            result.add(getArticle(title, articleURL, imageURL));
        }
        return result;
    }

    private OutletArticle getArticle(String title, String articleURL, String imageURL) throws IOException {
        Document document = Jsoup.connect(articleURL).get();
        String articleBody = extractArticleText(document);
        return new OutletArticle(title, articleBody, imageURL, articleURL, outletName);
    }

    private JSONArray fetchArticles() throws IOException {
        String sourceString = outletName.getSourceString();
        String type = outletName.getType();
        URL apiCall = new URL("https://newsapi.org/v1/articles?source=" + sourceString + "&sortBy=" + type + "&apiKey=51bd090ca7d4487b803844184ef880c0");
        URLConnection newsApiConnection = apiCall.openConnection();
        newsApiConnection.connect();
        return new JSONObject(IOUtils.toString(newsApiConnection.getInputStream(), Charset.forName("UTF-8"))).getJSONArray("articles");
    }

    protected abstract String extractArticleText(Document page);
}
