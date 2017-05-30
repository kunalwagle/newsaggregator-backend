package com.newsaggregator.base;

import com.newsaggregator.Utils;
import com.newsaggregator.db.Topics;

/**
 * Created by kunalwagle on 31/01/2017.
 */
public class WikipediaArticle extends Article {

    private String extract;
    private String _id;
    private int articleCount;

    public WikipediaArticle(String source, String title, String extract, String imageUrl) {
        super(source, title, imageUrl);
        this.extract = extract;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
        Topics topics = new Topics(Utils.getDatabase());
        articleCount = topics.getArticleCount(_id);
    }

    public String getExtract() {
        return extract;
    }

    public int getArticleCount() {
        return articleCount;
    }
}
