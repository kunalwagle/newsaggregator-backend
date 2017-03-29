package com.newsaggregator.ml.summarisation;

import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public interface Summarisation {

    String summarise(List<String> texts);

}
