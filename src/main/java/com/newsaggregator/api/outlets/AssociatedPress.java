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
        String articleText = articleBodyElements.text();
        articleText = articleText.substring(articleText.indexOf("\u2014") + 2);
        if (articleText.contains("___")) {
            articleText = articleText.substring(0, articleText.indexOf("___"));
        }
        return articleText;
    }
}
