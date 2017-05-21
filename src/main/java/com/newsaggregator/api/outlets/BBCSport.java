package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class BBCSport extends NewsAPI {

    public BBCSport() {
        super(Outlet.BBCSport);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        //TODO Implement
        return null;
    }
}
