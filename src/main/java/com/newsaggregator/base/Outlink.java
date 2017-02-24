package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 24/02/2017.
 */
public class Outlink {

    private String outlink;
    private List<String> categories;

    public Outlink(String outlink, List<String> categories) {
        this.outlink = outlink;
        this.categories = categories;
    }
}
