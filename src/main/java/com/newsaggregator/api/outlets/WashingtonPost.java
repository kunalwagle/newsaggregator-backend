package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class WashingtonPost extends NewsAPI {

    public WashingtonPost() {
        super(Outlet.WashingtonPost);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        Elements articleBody = page.getElementsByTag("article").get(0).getElementsByTag("p");
        Iterator<Element> elementIterator = articleBody.iterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            if (element.hasClass("interstitial-link")) {
                elementIterator.remove();
            }
        }
        return articleBody.text();
    }
}
