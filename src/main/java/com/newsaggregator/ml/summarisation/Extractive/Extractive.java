package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.ml.nlp.SentenceDetection;
import com.newsaggregator.ml.summarisation.Summarisation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Extractive implements Summarisation {


    @Override
    public String summarise(String text) {
        Graph graph = createGraph(text);
        graph = applyCosineSimilarities(graph);
        graph = filterGraph(graph);
        List<String> finalStrings = applyPageRank(graph);
        return generateFinalStringFromList(finalStrings);
    }

    private String generateFinalStringFromList(List<String> finalStrings) {
        return null;
    }

    private List<String> applyPageRank(Graph graph) {
        return null;
    }

    private Graph filterGraph(Graph graph) {
        return null;
    }

    private Graph applyCosineSimilarities(Graph graph) {
        graph.applyCosineSimilarities();
        return graph;
    }

    private Graph createGraph(String text) {
        List<String> sentences = splitToSentences(text);
        return new Graph(sentences);
    }

    private List<String> splitToSentences(String text) {
        SentenceDetection sentenceDetection = new SentenceDetection();
        return Arrays.asList(sentenceDetection.detectSentences(text));
    }
}
