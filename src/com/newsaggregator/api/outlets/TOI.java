package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class TOI extends NewsAPI {

    public TOI() {
        super(Outlet.TOI);
    }

    @Override
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        return page.getElementsByAttributeValue("itemprop", "articleBody").text();
    }
}
