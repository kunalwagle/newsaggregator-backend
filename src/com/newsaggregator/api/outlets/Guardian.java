package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.OutletArticle;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Guardian {

    public static List<OutletArticle> getArticles() {
        List<OutletArticle> articles = new ArrayList<>();
        try {
            int totalPages = getTotalPages();
            for (int i=0; i<totalPages; i++) {
                articles.addAll(fetchBatchArticles(i+1));
            }
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return articles;
    }

    private static List<OutletArticle> fetchBatchArticles(int pageNumber) throws IOException {
        List<OutletArticle> articles = new ArrayList<>();
        JSONObject articleBatch = getResponse(pageNumber);
        JSONArray articleJSONArray = articleBatch.getJSONArray("results");
        for (int i = 0; i < articleJSONArray.length(); i++) {
            JSONObject articleJSON = articleJSONArray.getJSONObject(i);
            String title = articleJSON.getString("webTitle");
            String articleURL = articleJSON.getString("webUrl");
            String body = Jsoup.parse(articleJSON.getJSONObject("fields").getString("body")).text();
            articles.add(new OutletArticle(title, body, null, articleURL, Outlet.Guardian.getSourceString()));
        }
        return articles;
    }

    private static JSONObject getResponse(int pageNumber) throws IOException {
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayAsString = simpleDateFormat.format(today);
        URL guardianURL = new URL("https://content.guardianapis.com/search?api-key=f180eada-2588-4c97-8182-13e772acb587&page-size=50&format=json&show-fields=body&from-date=" + todayAsString + "&page=" + pageNumber);
        URLConnection guardianURLConnection = guardianURL.openConnection();
        guardianURLConnection.connect();
        return new JSONObject(IOUtils.toString(guardianURLConnection.getInputStream(), Charset.forName("UTF-8"))).getJSONObject("response");
    }

    private static int getTotalPages() throws IOException {
        JSONObject response = getResponse(1);
        return response.getInt("pages");
    }

}
