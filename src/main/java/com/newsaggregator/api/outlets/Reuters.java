package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;


/**
 * Created by kunalwagle on 02/02/2017.
 */
public class Reuters extends NewsAPI {

    public Reuters() {
        super(Outlet.Reuters);
    }

    @Override
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        Elements articleBody = page.getElementById("article-text").children();
        Iterator<Element> iterator = articleBody.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (element.text().contains("Additional reporting") || element.id().equals("article-byline")) {
                iterator.remove();
            }
        }
        return articleBody.text();
    }
}
