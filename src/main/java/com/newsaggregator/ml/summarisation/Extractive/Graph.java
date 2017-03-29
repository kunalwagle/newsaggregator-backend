package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.ml.nlp.ExtractSentenceTypes;
import com.newsaggregator.ml.nlp.SentenceDetection;
import com.newsaggregator.ml.tfidf.TfIdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 29/03/2017.
 */

public class Graph {

    private List<Connection> connections;
    private List<Node> nodes;

    public Graph(List<String> texts) {
        this.nodes = new ArrayList<>();
        int i = 0;
        for (String text : texts) {
            List<String> sentences = splitToSentences(text);
            int j = 0;
            for (String sentence : sentences) {
                nodes.add(new Node(sentence, j, i));
                j++;
                i++;
            }
        }
        this.connections = generateConnections();
    }

    private List<Connection> generateConnections() {
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                int finalI = i;
                int finalJ = j;
                if (i != j && connections.stream().noneMatch(connection -> connection.matches(nodes.get(finalI), nodes.get(finalJ)))) {
                    connections.add(new Connection(nodes.get(finalI), nodes.get(finalJ)));
                }
            }
        }
        return connections;
    }

    public void applyCosineSimilarities(List<String> texts) {
        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
        List<String> nounifiedTexts = texts.stream().map(extractSentenceTypes::nounifyDocument).collect(Collectors.toList());
        TfIdf tfIdf = new TfIdf(nounifiedTexts);
        for (Connection connection : connections) {
            String nounifiedFirstSentence = extractSentenceTypes.nounifyDocument(connection.getFirstNode().getSentence());
            String nounifiedSecondSentence = extractSentenceTypes.nounifyDocument(connection.getSecondNode().getSentence());
            connection.setDistance(tfIdf.performTfIdfCosineSimilarities(nounifiedFirstSentence, nounifiedSecondSentence));
        }
    }

    public void filterOutConnections(double threshold) {
        Iterator iterator = connections.iterator();
        while (iterator.hasNext()) {
            Connection connection = (Connection) iterator.next();
            double distance = connection.getDistance();
            if (distance < threshold) {
                iterator.remove();
            }
        }
    }

    public List<List<Node>> getNodeConnections() {
        List<List<Node>> result = new ArrayList<>();
        for (Node ignored : nodes) {
            result.add(new ArrayList<>());
        }
        for (Connection connection : connections) {
            Node node1 = connection.getFirstNode();
            Node node2 = connection.getSecondNode();
            result.get(node1.getIdentifier()).add(node2);
            result.get(node2.getIdentifier()).add(node1);
        }
        return result;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    private List<String> splitToSentences(String text) {
        SentenceDetection sentenceDetection = new SentenceDetection();

        return Arrays.asList(sentenceDetection.detectSentences(text));
    }
}
