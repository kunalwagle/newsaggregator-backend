package com.newsaggregator.ml.summarisation.Abstractive;

import edu.stanford.nlp.ie.util.RelationTriple;

import java.util.Collection;
import java.util.List;

/**
 * Created by kunalwagle on 10/04/2017.
 */
public class RSNode {

    private List<RSWord> words;
    private Collection<RelationTriple> relationTriples;
    private double calculation;

    public RSNode(List<RSWord> words, Collection<RelationTriple> relationTriples) {
        this.words = words;
        this.relationTriples = relationTriples;
        this.calculation = performCalculation(words);
    }

    private double performCalculation(List<RSWord> words) {
        double totalFrequency = words.stream().mapToDouble(RSWord::getFrequency).sum();
        return words.stream().mapToDouble(word -> 50 * ((word.getFrequency() / totalFrequency) / word.getNumSenses())).sum();
    }

    public void calculationFactor(double calculationFactor) {
        calculation /= calculationFactor;
    }

    public List<RSWord> getWords() {
        return words;
    }

    public Collection<RelationTriple> getRelationTriples() {
        return relationTriples;
    }

    public double getCalculation() {
        return calculation;
    }

    public void addWords(List<RSWord> allWords) {
        words.addAll(allWords);
    }
}
