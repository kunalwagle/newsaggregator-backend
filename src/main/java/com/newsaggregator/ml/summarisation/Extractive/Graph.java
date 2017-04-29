package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
import com.newsaggregator.ml.nlp.apache.SentenceDetection;
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
    protected List<Node> nodes;

    public Graph(List<OutletArticle> articles) {
        this.nodes = new ArrayList<>();
        int i = 0;
        for (OutletArticle article : articles) {
            String text = article.getBody();
            List<String> sentences = splitToSentences(text);
            double j = 0.0;
            for (String sentence : sentences) {
                nodes.add(new Node(sentence, j / sentences.size(), (int) j, i, article.getSource()));
                j++;
                i++;
            }
        }
        this.connections = generateConnections();
    }

    public Graph() {
    }

    public void addNodes(List<Node> nodes) {
        this.nodes = nodes;
        //this.connections = generateConnections();
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
        double max = 0;
        Iterator<Connection> connectionIterator = connections.iterator();
        while (connectionIterator.hasNext()) {
            Connection connection = connectionIterator.next();
            String nounifiedFirstSentence = extractSentenceTypes.nounifyDocument(connection.getFirstNode().getSentence());
            String nounifiedSecondSentence = extractSentenceTypes.nounifyDocument(connection.getSecondNode().getSentence());
            if (nounifiedFirstSentence.length() == 0 || nounifiedSecondSentence.length() == 0) {
                connectionIterator.remove();
                continue;
            }
            double cosineSimilarity = tfIdf.performTfIdfCosineSimilarities(nounifiedFirstSentence, nounifiedSecondSentence);
            double sentencePositionDifference = 1 - Math.abs(connection.getFirstNode().getSentencePosition() - connection.getSecondNode().getSentencePosition());
            cosineSimilarity *= sentencePositionDifference;
            if (max < cosineSimilarity) {
                max = cosineSimilarity;
            }
            connection.setDistance(cosineSimilarity);
        }
        normaliseDistances(max);
    }

    private void normaliseDistances(double max) {
        for (Connection connection : connections) {
            double distance = connection.getDistance();
            connection.setDistance(distance / max);
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

    public Node getNodeForSentence(String sentence) {
        return nodes.stream().filter(node -> node.getSentence().equals(sentence)).findFirst().get();
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

    public List<Connection> getConnections() {
        return connections;
    }

    public List<Node> getAllSentencesForASingleSource(String source) {
        return nodes.stream().filter(node -> node.getSource().equals(source)).collect(Collectors.toList());
    }
}
