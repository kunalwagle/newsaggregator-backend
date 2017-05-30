package com.newsaggregator.base;

import com.newsaggregator.Utils;
import com.newsaggregator.db.Topics;
import com.newsaggregator.server.LabelHolder;

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
        LabelHolder labelHolder = topics.getTopicById(_id);
        articleCount = labelHolder.getClusters().size();
    }

    public String getExtract() {
        return extract;
    }

    public int getArticleCount() {
        return articleCount;
    }
}
