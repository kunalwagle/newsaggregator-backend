package com.newsaggregator.base;

/**
 * Created by kunalwagle on 30/01/2017.
 */

public enum Outlet {
    NYT("New York Times"),
    Guardian("Guardian"), Wikipedia("Wikipedia");

    private final String sourceString;

    Outlet(String sourceString) {
        this.sourceString = sourceString;
    }
}
