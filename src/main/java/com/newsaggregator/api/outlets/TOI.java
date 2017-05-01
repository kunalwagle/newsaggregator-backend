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
        String text = page.getElementsByAttributeValue("itemprop", "articleBody").text();
        if (text.contains("Stay updated on the go with Times")) {
            return text.substring(0, text.indexOf("Stay updated on the go with Times"));
        }
        return text;
    }
}
