package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class CNBC extends NewsAPI {

    public CNBC() {
        super(Outlet.CNBC);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {

        Elements articleBody;
        Element article = page.getElementById("article_body");
        if (article != null) {
            articleBody = article.getElementsByTag("p");

            Iterator<Element> elementIterator = articleBody.iterator();
            while (elementIterator.hasNext()) {
                Element element = elementIterator.next();
                if (element.getElementsByTag("img").size() > 0) {
                    elementIterator.remove();
                } else if (element.getElementsByTag("em").size() > 0) {
                    elementIterator.remove();
                }
            }
            return articleBody.text();
        } else {
            return "";
        }
    }
}
