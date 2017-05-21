package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class Bloomberg extends NewsAPI {

    public Bloomberg() {
        super(Outlet.Bloomberg);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        //TODO Implement
        return null;
    }
}
