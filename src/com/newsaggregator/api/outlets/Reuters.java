package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class Reuters extends NewsAPI {

    public Reuters() {
        super(Outlet.Reuters);
    }

    @Override
    protected String extractArticleText(Document page) {
        Element articleBody = page.getElementById("article-text");
        return articleBody.text();
    }
}
