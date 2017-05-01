package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class Metro extends NewsAPI {

    public Metro() {
        super(Outlet.Metro);
    }

    @Override
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        Elements elements = page.getElementsByClass("article-body");
        Elements articleBody = elements.get(0).getElementsByTag("p");
        return articleBody.text();
    }
}
