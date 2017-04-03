package com.newsaggregator.ml.summarisation.Abstractive;

import com.newsaggregator.ml.nlp.ExtractSentenceTypes;
import com.newsaggregator.ml.nlp.SentenceDetection;
import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Extractive.Graph;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import com.newsaggregator.ml.summarisation.Summarisation;
import com.newsaggregator.ml.summarisation.Summary;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Abstractive implements Summarisation {

    private Summary initialSummary;

    public Abstractive(Summary initialSummary) {
        this.initialSummary = initialSummary;
    }

    @Override
    public Summary summarise() {
        List<String> strippedSentences = stripClausesAndSentences();
        preProcessPronouns(strippedSentences);
        return createSummary(strippedSentences);
    }

    private void preProcessPronouns(List<String> sentences) {
        Graph graph = createNewGraphFromSentences(sentences);
    }

    private Graph createNewGraphFromSentences(List<String> sentences) {

        return null;
    }

    private Summary createSummary(List<String> strippedSentences) {
        return new Summary(null, Combiner.combineStrings(strippedSentences), initialSummary.getArticles());
    }

    private List<String> stripClausesAndSentences() {
        Graph graph = initialSummary.getGraph();
        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
        Iterator<Node> nodeIterator = graph.getNodes().iterator();
        SentenceDetection sentenceDetection = new SentenceDetection();
        List<String> summaryStrings = new LinkedList<>(Arrays.asList(sentenceDetection.detectSentences(initialSummary.getText())));
        while (nodeIterator.hasNext()) {
            Node firstNode = nodeIterator.next();
            boolean removeNode = false;
            for (Node secondNode : graph.getNodes()) {
                if (summaryStrings.contains(firstNode.getSentence())
                        && summaryStrings.contains(secondNode.getSentence())
                        && firstNode.getIdentifier() != secondNode.getIdentifier()) {
                    List<String> firstSentence = extractSentenceTypes.allWords(firstNode.getSentence());
                    List<String> secondSentence = extractSentenceTypes.allWords(secondNode.getSentence());
                    double total = 0.0;
                    for (String noun : firstSentence) {
                        for (String secondNoun : secondSentence) {
                            if (noun.toLowerCase().equals(secondNoun.toLowerCase())) {
                                total++;
                            }
                        }
                    }
                    double similarity = total / ((firstSentence.size() + secondSentence.size()) / 2);
                    if (similarity > 0.8) {
                        Iterator<String> stringIterator = summaryStrings.iterator();
                        while (stringIterator.hasNext()) {
                            if (stringIterator.next().equals(firstNode.getSentence())) {
                                stringIterator.remove();
                            }
                        }
                        removeNode = true;
                    }
                }
            }
            if (removeNode) {
                nodeIterator.remove();
            }
        }
        return summaryStrings;
    }

}
