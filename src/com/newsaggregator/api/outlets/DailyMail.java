package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class DailyMail extends NewsAPI {

    public DailyMail() {
        super(Outlet.DailyMail);
    }

    @Override
    protected String extractArticleText(Document page) {
        Elements articleBodyElements = page.getElementsByAttributeValue("itemprop", "articleBody");
        Element articleBody = articleBodyElements.get(0);
        return articleBody.text();
    }

}
