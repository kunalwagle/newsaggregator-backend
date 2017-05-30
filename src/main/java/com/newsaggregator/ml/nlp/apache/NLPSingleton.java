package com.newsaggregator.ml.nlp.apache;

/**
 * Created by kunalwagle on 23/05/2017.
 */
public class NLPSingleton {

    private static ThreadLocal<ExtractSentenceTypes> extractSentenceTypes = ThreadLocal.withInitial(ExtractSentenceTypes::new);

    public static synchronized ExtractSentenceTypes getInstance() {
        return extractSentenceTypes.get();
    }

    public static synchronized void removeInstance() {
        if (extractSentenceTypes != null) {
            extractSentenceTypes.remove();
        }
    }

}
