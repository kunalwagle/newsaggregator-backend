package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.ml.nlp.SentenceDetection;
import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Summarisation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Extractive implements Summarisation {


    @Override
    public String summarise(List<String> texts) {
        List<String> sentences = splitToSentences(texts);
        Graph graph = createGraph(sentences);
        graph = applyCosineSimilarities(graph, texts);
        graph = filterGraph(graph);
        List<String> finalStrings = applyPageRank(graph, sentences);
        return generateFinalStringFromList(finalStrings);
    }

    private String generateFinalStringFromList(List<String> finalStrings) {
        return Combiner.combineStrings(finalStrings.stream().limit(5).collect(Collectors.toList()));
    }

    private List<String> applyPageRank(Graph graph, List<String> texts) {
        return PageRank.applyPageRank(graph, texts);
    }

    private Graph filterGraph(Graph graph) {
        graph.filterOutConnections(1.7);
        return graph;
    }

    private Graph applyCosineSimilarities(Graph graph, List<String> texts) {
        graph.applyCosineSimilarities(texts);
        return graph;
    }

    private Graph createGraph(List<String> texts) {
        return new Graph(texts);
    }

    private List<String> splitToSentences(List<String> texts) {
        SentenceDetection sentenceDetection = new SentenceDetection();
        List<String> sentences = new ArrayList<>();
        for (String text : texts) {
            sentences.addAll(Arrays.asList(sentenceDetection.detectSentences(text)));
        }

        return sentences;
    }
}
