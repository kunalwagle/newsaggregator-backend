package com.newsaggregator.ml.summarisation;

import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Abstractive implements Summarisation {

    private Summary initialSummary;

    public Abstractive(Summary initialSummary) {
        this.initialSummary = initialSummary;
    }

    @Override
    public String summarise(List<String> text) {
        return text.get(0);
    }

}
