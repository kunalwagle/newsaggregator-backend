package com.newsaggregator.api.outlets;

import com.newsaggregator.base.Outlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * Created by kunalwagle on 21/05/2017.
 */
public class BBCNews extends NewsAPI {

    public BBCNews() {
        super(Outlet.BBCNews);
    }


    @Override
    protected String extractArticleText(Document page) throws NullPointerException {
        Elements articleBody = page.getElementsByClass("story-body__inner").get(0).getElementsByTag("p");
        Iterator<Element> iterator = articleBody.iterator();
        boolean hasHaveYourSay = false;
        while (iterator.hasNext()) {
            Element element = iterator.next();
            Elements strong = element.getElementsByTag("strong");
            strong.remove();
            strong = element.getElementsByClass("icon email");
            if (strong.size() > 0) {
                iterator.remove();
                hasHaveYourSay = true;
            }
        }

        if (hasHaveYourSay) {
            articleBody.remove(articleBody.size() - 1);
        }

        return articleBody.text();
    }
}
