package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class Newsweek extends NewsAPI {

    public Newsweek() {
        super(Outlet.Newsweek);
    }

    @Override
    protected String extractArticleText(Document page) {
        Elements elements = page.getElementsByAttributeValue("itemprop", "articleBody");
        Element content = elements.get(0);
        Elements articleBody = content.getElementsByTag("p");
        return articleBody.text();
    }
}
