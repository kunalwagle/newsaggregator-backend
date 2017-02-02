package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class Telegraph extends NewsAPI {

    public Telegraph() {
        super(Outlet.Telegraph);
    }

    @Override
    protected String extractArticleText(Document page) {
        Elements elements = page.getElementsByClass("articleBodyText");
        Element content = elements.get(0);
        Elements articleBody = content.getElementsByTag("p");
        return articleBody.text();
    }
}
