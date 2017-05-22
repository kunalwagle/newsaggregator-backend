package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class Bloomberg extends NewsAPI {

    public Bloomberg() {
        super(Outlet.Bloomberg);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        Elements articleBody = page.getElementsByClass("body-copy").get(0).children();
        Iterator<Element> elementIterator = articleBody.iterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            if (!element.tagName().equals("p")) {
                elementIterator.remove();
            }
            Elements child = element.getElementsByAttributeValue("itemprop", "StoryLink");
            if (child.size() > 0) {
                elementIterator.remove();
            }
        }
        return articleBody.text();
    }
}
