package com.newsaggregator.ml.summarisation.Abstractive;

import com.newsaggregator.ml.nlp.wordnet.Wordnet;
import edu.mit.jwi.item.IWord;

/**
 * Created by kunalwagle on 13/04/2017.
 */
public class RSWord {

    private String lemma;
    private String synonym;
    private IWord wordSense;
    private int numSenses;
    private Wordnet wordnet;

    public RSWord(String lemma, IWord wordSense, int numSenses, Wordnet wordnet) {
        this.lemma = lemma;
        this.wordSense = wordSense;
        this.numSenses = numSenses;
        this.wordnet = wordnet;
        this.synonym = lemma;
    }

    public String getLemma() {
        return lemma;
    }

    public IWord getWordSense() {
        return wordSense;
    }

    public int getNumSenses() {
        return numSenses;
    }

    public int getFrequency() {
        return wordnet.getFrequency(wordSense);
    }

    public String getSynonym() {
        return synonym;
    }

    public Wordnet getWordnet() {
        return wordnet;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }
}
