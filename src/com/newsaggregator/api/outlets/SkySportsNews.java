package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class SkySportsNews extends NewsAPI {

    public SkySportsNews() {
        super(Outlet.SkySportsNews);
    }

    @Override
    protected String extractArticleText(Document page) {
        Elements elements = page.getElementsByClass("article__body");
        Element content = elements.get(0);
        Elements articleBody = content.getElementsByTag("p");
        return articleBody.text();
    }

}
