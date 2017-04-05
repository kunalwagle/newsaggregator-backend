package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by kunalwagle on 02/02/2017.
 */
public class Telegraph extends NewsAPI {

    public Telegraph() {
        super(Outlet.Telegraph);
    }

    @Override
    protected String extractArticleText(Document page) throws IndexOutOfBoundsException {
        Elements elements = page.getElementsByClass("articleBodyText");
        String result = "";
        for (Element content : elements) {
            Elements articleBody = content.getElementsByTag("p");
            result += articleBody.text() + " ";
        }

        return result;
    }
}
