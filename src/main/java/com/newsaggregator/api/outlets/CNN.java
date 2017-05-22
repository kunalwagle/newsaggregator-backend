package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class CNN extends NewsAPI {

    public CNN() {
        super(Outlet.CNN);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        String result = page.getElementsByClass("zn-body__paragraph").text();
        if (result.contains("(CNN)")) {
            result = result.substring(result.indexOf("(CNN)") + 5);
        }

        return result;

    }
}
