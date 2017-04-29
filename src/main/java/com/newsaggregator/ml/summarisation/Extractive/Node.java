package com.newsaggregator.ml.summarisation.Extractive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Node {

    private String sentence;
    private double sentencePosition;
    private int absoluteSentencePosition;
    private int identifier;
    private List<Node> relatedNodes = new ArrayList<>();
    private String source;

    public Node(String sentence, double sentencePosition, int absoluteSentencePosition, int identifier, String source) {
        this.sentence = sentence;
        this.sentencePosition = sentencePosition;
        this.absoluteSentencePosition = absoluteSentencePosition;
        this.identifier = identifier;
        this.source = source;
    }

    public Node() {
        //Dummy constructor for Jackson
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

    public String getSource() {
        return source;
    }

    public int getAbsoluteSentencePosition() {
        return absoluteSentencePosition;
    }

    public List<Node> getRelatedNodes() {
        return relatedNodes;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setAbsoluteSentencePosition(int absoluteSentencePosition) {
        this.absoluteSentencePosition = absoluteSentencePosition;
    }

    public void addNode(Node node) {
        relatedNodes.add(node);
    }
}
