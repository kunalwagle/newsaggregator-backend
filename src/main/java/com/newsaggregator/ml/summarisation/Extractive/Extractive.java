package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Summarisation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Extractive implements Summarisation {


    @Override
    public String summarise(List<String> texts) {
        Graph graph = createGraph(texts);
        graph = applyCosineSimilarities(graph, texts);
        graph = filterGraph(graph);
        List<Node> finalNodes = applyPageRank(graph);
        return generateFinalStringFromList(finalNodes, texts.size());
    }

    private String generateFinalStringFromList(List<Node> finalNodes, int textSize) {
        List<Node> strippedNodes = finalNodes.stream().limit(finalNodes.size() / textSize).collect(Collectors.toList());
        strippedNodes = strippedNodes.stream().sorted(Comparator.comparing(Node::getSentencePosition)).collect(Collectors.toList());
        return Combiner.combineStrings(strippedNodes.stream().map(Node::getSentence).collect(Collectors.toList()));
    }

    private List<Node> applyPageRank(Graph graph) {
        return PageRank.applyPageRank(graph);
    }

    private Graph filterGraph(Graph graph) {
        graph.filterOutConnections(2);
        return graph;
    }

    private Graph applyCosineSimilarities(Graph graph, List<String> texts) {
        graph.applyCosineSimilarities(texts);
        return graph;
    }

    private Graph createGraph(List<String> texts) {
        return new Graph(texts);
    }
}
