package com.newsaggregator.base;

/**
 * Created by kunalwagle on 30/01/2017.
 */

public enum Outlet {
    Guardian("the-guardian-uk", "latest"),
    Wikipedia("wikipedia", "latest"),
    Independent("independent", "top"),
    AssociatedPress("associated-press", "top"),
    Reuters("reuters", "latest"),
    BusinessInsiderUK("business-insider-uk", "latest"),
    DailyMail("daily-mail", "latest"),
    Cricinfo("espn-cric-info", "latest"),
    Metro("metro", "latest"),
    Mirror("mirror", "latest"),
    Newsweek("newsweek", "latest"),
    SkySportsNews("sky-sports-news", "latest"),
    Telegraph("the-telegraph", "latest"),
    TOI("the-times-of-india", "latest"),
    BBCNews("bbc-news", "top"),
    BBCSport("bbc-sport", "top"),
    Bloomberg("bloomberg", "top"),
    CNN("cnn", "top"),
    CNBC("cnbc", "top"),
    ESPN("espn", "top"),
    FourFourTwo("four-four-two", "latest"),
    WashingtonPost("the-washington-post", "top"),
    WSJ("the-wall-street-journal", "top");


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
