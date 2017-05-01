package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class DailyMail extends NewsAPI {

    public DailyMail() {
        super(Outlet.DailyMail);
    }

    @Override
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        Elements articleBodyElements = page.getElementsByClass("mol-para-with-font");
        return articleBodyElements.text();
    }

}
