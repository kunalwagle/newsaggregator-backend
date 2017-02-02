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
    protected String extractArticleText(Document page) {
        Elements elements = page.getElementsByClass("story-content-main");
        Element articleBody = elements.get(0);
        return articleBody.text();
    }
}
