package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.ml.tfidf.TfIdf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */

public class Graph {

    private List<Connection> connections;

    public Graph(List<String> sentences) {
        this.connections = generateConnectionsFromSentences(sentences);
    }

    private List<Connection> generateConnectionsFromSentences(List<String> sentences) {
        List<Connection> connections = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        int i = 0;
        for (String sentence : sentences) {
            nodes.add(new Node(sentence, i));
            i++;
        }
        for (i = 0; i < nodes.size(); i++) {
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

    public void applyCosineSimilarities() {
        for (Connection connection : connections) {
            Node firstNode = connection.getFirstNode();
            Node secondNode = connection.getSecondNode();
            connection.setDistance(TfIdf.performTfIdfCosineSimilarities(firstNode.getSentence(), secondNode.getSentence()));
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
}
