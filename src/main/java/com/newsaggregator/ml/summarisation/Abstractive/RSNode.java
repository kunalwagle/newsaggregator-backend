package com.newsaggregator.ml.summarisation.Abstractive;

import java.util.List;

/**
 * Created by kunalwagle on 10/04/2017.
 */
public class RSNode {

    private List<RSWord> words;
    private double calculation;

    public RSNode(List<RSWord> words) {
        this.words = words;
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
