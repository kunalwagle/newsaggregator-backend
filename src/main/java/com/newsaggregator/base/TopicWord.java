package com.newsaggregator.base;

/**
 * Created by kunalwagle on 13/02/2017.
 */
public class TopicWord {

    private String word;
    private double distribution;

    public TopicWord(String word, double distribution) {
        this.word = word;
        this.distribution = distribution;
    }

    public String getWord() {
        return word;
    }

    public double getDistribution() {
        return distribution;
    }

    public void setDistribution(double multiplyingFactor) {
        distribution *= multiplyingFactor;
    }
}
