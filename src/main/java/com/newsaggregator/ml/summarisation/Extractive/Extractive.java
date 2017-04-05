package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Summarisation;
import com.newsaggregator.ml.summarisation.Summary;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Extractive implements Summarisation {

    private List<OutletArticle> articles;
    private List<String> texts;

    public Extractive(List<OutletArticle> articles) {
        this.articles = articles;
        this.texts = articles.stream().map(OutletArticle::getBody).collect(Collectors.toList());
    }

    @Override
    public Summary summarise() {
        Graph graph = createGraph();
        graph = applyCosineSimilarities(graph, texts);
        graph = filterGraph(graph);
        List<Node> finalNodes = applyPageRank(graph);
        String finalString = generateFinalStringFromList(finalNodes, texts.size() * 2);
        return new Summary(graph, finalString, articles);
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
        graph.filterOutConnections(0.8);
        return graph;
    }

    private Graph applyCosineSimilarities(Graph graph, List<String> texts) {
        graph.applyCosineSimilarities(texts);
        return graph;
    }

    private Graph createGraph() {
        return new Graph(articles);
    }
}
