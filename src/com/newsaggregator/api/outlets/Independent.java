package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 01/02/2017.
 */
public class Independent extends NewsAPI {


    public Independent() {
        super(Outlet.Independent);
    }

    @Override
    protected String extractArticleText(Document page) {
        Elements articleBodyElements = page.getElementsByAttributeValue("itemprop", "articleBody");
        Element articleBody = articleBodyElements.get(0);
        return articleBody.text();
    }
}
