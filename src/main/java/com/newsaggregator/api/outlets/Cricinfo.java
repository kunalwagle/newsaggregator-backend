package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class Cricinfo extends NewsAPI {

    public Cricinfo() {
        super(Outlet.Cricinfo);
    }


    @Override
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        Elements elements = page.getElementsByClass("story-content-main");
        Element articleBody = elements.get(0);
        articleBody.getElementsByClass("fixed-social").remove();
        articleBody.getElementsByClass("video-section").remove();
        articleBody.getElementsByClass("end-credit").remove();
        articleBody.getElementsByClass("end-copyright").remove();
        String article = articleBody.text();
        if (article.contains("ball-by-ball details")) {
            article = article.substring(article.indexOf("ball-by-ball details") + 21);
        }
        return article;
    }
}
