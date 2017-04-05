package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * Created by kunalwagle on 01/02/2017.
 */
public class Independent extends NewsAPI {


    public Independent() {
        super(Outlet.Independent);
    }

    @Override
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        Elements articleBodyElements = page.getElementsByAttributeValue("itemprop", "articleBody");
        Elements articleBody = articleBodyElements.get(0).children();
        Iterator<Element> iterator = articleBody.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (element.hasClass("inline-block") || element.hasClass("relatedlinkslist") || element.hasClass("inline-pipes-list") || element.hasClass("syndication-btn")) {
                iterator.remove();
            }
        }
        return articleBody.text();
    }
}
