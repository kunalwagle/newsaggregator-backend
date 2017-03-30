package com.newsaggregator.ml.summarisation.Extractive;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Node {

    private String sentence;
    private double sentencePosition;
    private int identifier;

    public Node(String sentence, double sentencePosition, int identifier) {
        this.sentence = sentence;
        this.sentencePosition = sentencePosition;
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getSentence() {
        return sentence;
    }

    public double getSentencePosition() {
        return sentencePosition;
    }
}
