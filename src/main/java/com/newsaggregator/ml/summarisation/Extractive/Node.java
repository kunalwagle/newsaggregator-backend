package com.newsaggregator.ml.summarisation.Extractive;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Node {

    private String sentence;
    private int sentencePosition;
    private int identifier;

    public Node(String sentence, int sentencePosition, int identifier) {
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

    public int getSentencePosition() {
        return sentencePosition;
    }
}
