package com.newsaggregator.ml.summarisation.Abstractive;

import edu.mit.jwi.item.ISynset;

/**
 * Created by kunalwagle on 13/04/2017.
 */
public class RSWord {

    private String lemma;
    private ISynset wordSense;
    private int numSenses;

    public RSWord(String lemma, ISynset wordSense, int numSenses) {
        this.lemma = lemma;
        this.wordSense = wordSense;
        this.numSenses = numSenses;
    }
}
