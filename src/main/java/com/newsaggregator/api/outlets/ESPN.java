package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class ESPN extends NewsAPI {

    public ESPN() {
        super(Outlet.ESPN);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        Elements elements = page.getElementsByClass("article-body").get(0).getElementsByTag("p");
        return elements.text();
    }
}
