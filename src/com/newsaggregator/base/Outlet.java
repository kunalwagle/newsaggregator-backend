package com.newsaggregator.base;

/**
 * Created by kunalwagle on 30/01/2017.
 */

public enum Outlet {
    NYT("the-new-york-times", "latest"),
    Guardian("the-guardian-uk", "latest"),
    Wikipedia("wikipedia", "latest"),
    Independent("independent", "top");

    private final String sourceString;
    private final String type;

    Outlet(String sourceString, String type) {
        this.sourceString = sourceString;
        this.type = type;
    }

    public String getSourceString() {
        return sourceString;
    }

    public String getType() {
        return type;
    }
}
