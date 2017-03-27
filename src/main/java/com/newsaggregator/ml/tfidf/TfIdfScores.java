package com.newsaggregator.ml.tfidf;

/**
 * Created by kunalwagle on 27/03/2017.
 */

public class TfIdfScores {

    private String label;
    private double calculation;

    public TfIdfScores(String label, double calculation) {
        this.label = label;
        this.calculation = calculation;
    }

    public String getLabel() {
        return label;
    }

    public double getCalculation() {
        return calculation;
    }
}
