package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 01/02/2017.
 */
public class AssociatedPress extends NewsAPI {


    public AssociatedPress() {
        super(Outlet.AssociatedPress);
    }

    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        Elements articleBodyElements = page.getElementsByClass("field-items");
        Element articleBody = articleBodyElements.get(2);
        return articleBody.text();
    }
}
