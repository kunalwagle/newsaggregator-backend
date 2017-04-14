package com.newsaggregator.ml.summarisation.Abstractive;

import edu.mit.jwi.item.POS;
import edu.stanford.nlp.trees.TypedDependency;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<RSWord> getNounSubject() {
        List<String> list = typedDependencies.stream().filter(dep -> dep.reln().getShortName().equals("nsubj")).map(dep -> dep.dep().lemma()).collect(Collectors.toList());
        return words.stream().filter(word -> list.contains(word.getLemma())).collect(Collectors.toList());
    }

    public List<RSWord> getNounObject() {
        List<String> list = typedDependencies.stream().filter(dep -> dep.reln().getShortName().equals("dobj")).map(dep -> dep.dep().lemma()).collect(Collectors.toList());
        return words.stream().filter(word -> list.contains(word.getLemma())).collect(Collectors.toList());
    }

    public List<RSWord> getVerb() {
        return words.stream().filter(word -> word.getWordSense().getPOS().equals(POS.VERB)).collect(Collectors.toList());
    }


    public double getCalculation() {
        return calculation;
    }
}
