package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class FourFourTwo extends NewsAPI {

    public FourFourTwo() {
        super(Outlet.FourFourTwo);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        Elements articleBody = page.getElementsByClass("node-content").get(0).getElementsByTag("p");
        Iterator<Element> articleElements = articleBody.iterator();
        while (articleElements.hasNext()) {
            Element element = articleElements.next();
            if (element.getElementsByTag("iframe").size() > 0) {
                articleElements.remove();
            }
            if (element.hasClass("crosshead")) {
                articleElements.remove();
            }
        }
        return articleBody.text();
    }
}
