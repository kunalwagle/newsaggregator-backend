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
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        Elements elements = page.getElementsByAttributeValue("itemprop", "articleBody");
        Element content = elements.get(0);
        Elements articleBody = content.getElementsByTag("p");
        for (Element element : articleBody) {
            element.getElementsByTag("strong").remove();
            element.getElementsByTag("a").remove();
            element.getElementsByAttributeValue("itemprop", "image").remove();
            element.getElementsByClass("videocontent-wrapper").remove();
        }
        return articleBody.text();
    }
}
