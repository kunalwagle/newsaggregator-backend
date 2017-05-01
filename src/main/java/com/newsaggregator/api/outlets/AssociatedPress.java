package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
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
        Elements articleBodyElements = page.getElementsByClass("field-name-body");
        return articleBodyElements.text();
    }
}
