package com.newsaggregator.ml.nlp.apache;

/**
 * Created by kunalwagle on 23/05/2017.
 */
public class NLPSingleton {

    private static ExtractSentenceTypes extractSentenceTypes;

    public static ExtractSentenceTypes getInstance() {
        if (extractSentenceTypes == null) {
            extractSentenceTypes = new ExtractSentenceTypes();
        }
        return extractSentenceTypes;
    }

}
