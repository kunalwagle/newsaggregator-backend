package com.newsaggregator.ml.summarisation.Abstractive;

import edu.stanford.nlp.trees.TypedDependency;

import java.util.Collection;
import java.util.List;

/**
 * Created by kunalwagle on 10/04/2017.
 */
public class RSNode {

    private List<RSWord> words;
    private Collection<TypedDependency> typedDependencies;
    private double calculation;

    public RSNode(List<RSWord> words, Collection<TypedDependency> typedDependencies) {
        this.words = words;
        this.typedDependencies = typedDependencies;
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

    public double getCalculation() {
        return calculation;
    }
}
