package com.newsaggregator.ml.summarisation.Abstractive;

import com.newsaggregator.ml.nlp.ExtractSentenceTypes;
import com.newsaggregator.ml.nlp.SentenceDetection;
import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Extractive.Connection;
import com.newsaggregator.ml.summarisation.Extractive.Graph;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import com.newsaggregator.ml.summarisation.Summarisation;
import com.newsaggregator.ml.summarisation.Summary;

import java.util.Arrays;
import java.util.Iterator;
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
        preProcessPronouns();
        return createSummary(strippedSentences);
    }

    private void preProcessPronouns() {

    }

    private Summary createSummary(List<String> strippedSentences) {
        return new Summary(null, Combiner.combineStrings(strippedSentences), initialSummary.getArticles());
    }

    private List<String> stripClausesAndSentences() {
        Graph graph = initialSummary.getGraph();
        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
        Iterator<Connection> nodeIterator = graph.getConnections().iterator();
        SentenceDetection sentenceDetection = new SentenceDetection();
        List<String> summaryStrings = Arrays.asList(sentenceDetection.detectSentences(initialSummary.getText()));
        while (nodeIterator.hasNext()) {
            Connection connection = nodeIterator.next();
            Node firstNode = connection.getFirstNode();
            Node secondNode = connection.getSecondNode();
            if (summaryStrings.contains(firstNode.getSentence()) && summaryStrings.contains(secondNode.getSentence())) {
                List<String> firstSentence = extractSentenceTypes.individualNouns(firstNode.getSentence());
                List<String> secondSentence = extractSentenceTypes.individualNouns(secondNode.getSentence());
                double total = 0.0;
                for (String noun : firstSentence) {
                    for (String secondNoun : secondSentence) {
                        if (noun.toLowerCase().equals(secondNoun.toLowerCase())) {
                            total++;
                        }
                    }
                }
                double similarity = total / (2 * (firstSentence.size() + secondSentence.size()));
                if (similarity > 0.8) {
                    summaryStrings.removeIf(s -> s.equals(firstNode.getSentence()));
                    graph.getNodes().remove(firstNode);
                }
            }
        }
        return summaryStrings;
    }

}
