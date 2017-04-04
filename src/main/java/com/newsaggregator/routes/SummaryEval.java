package com.newsaggregator.routes;

/**
 * Created by kunalwagle on 04/04/2017.
 */
public class SummaryEval {

    private String first;
    private String second;
    private String third;

    public SummaryEval() {

    }

    public SummaryEval(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }
}
