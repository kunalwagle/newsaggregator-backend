package com.newsaggregator.ml.summarisation;

import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Abstractive implements Summarisation {

    @Override
    public String summarise(List<String> text) {
        return text.get(0);
    }

}
