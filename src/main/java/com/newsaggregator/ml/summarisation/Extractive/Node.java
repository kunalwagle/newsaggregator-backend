package com.newsaggregator.ml.summarisation.Extractive;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Node {

    private String sentence;
    private int identifier;

    public Node(String sentence, int identifier) {
        this.sentence = sentence;
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getSentence() {
        return sentence;
    }
}
