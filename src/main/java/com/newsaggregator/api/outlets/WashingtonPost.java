package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class WashingtonPost extends NewsAPI {

    public WashingtonPost() {
        super(Outlet.WashingtonPost);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        //TODO Implement
        return null;
    }
}
