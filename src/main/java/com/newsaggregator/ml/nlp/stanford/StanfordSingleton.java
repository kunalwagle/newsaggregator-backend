package com.newsaggregator.ml.nlp.stanford;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * Created by kunalwagle on 23/05/2017.
 */
public class StanfordSingleton {

    private static StanfordCoreNLP stanfordCoreNLP;

    public static StanfordCoreNLP getInstance() {
        if (stanfordCoreNLP == null) {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,ner");
            stanfordCoreNLP = new StanfordCoreNLP(props);
        }
        return stanfordCoreNLP;
    }

}

