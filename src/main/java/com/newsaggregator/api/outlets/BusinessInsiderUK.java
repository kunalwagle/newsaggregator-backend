package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class BusinessInsiderUK extends NewsAPI {

    public BusinessInsiderUK() {
        super(Outlet.BusinessInsiderUK);
    }

    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        Elements articleBodyElements = page.getElementsByClass("post-content");
        Element articleBody = articleBodyElements.get(0);
        return articleBody.text();
    }

}
