package com.newsaggregator.ml.summarisation.Extractive;

import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
import com.newsaggregator.ml.nlp.apache.NLPSingleton;
import com.newsaggregator.ml.nlp.apache.SentenceDetection;
import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Summarisation;
import com.newsaggregator.ml.summarisation.Summary;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Extractive implements Summarisation {

    private List<OutletArticle> articles;
    private List<String> texts;

    public Extractive(List<OutletArticle> articles) {
        this.articles = articles;
        this.texts = articles.stream().map(OutletArticle::getBody).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Summary summarise() {
        try {
            Graph graph = createGraph();
            graph = applyCosineSimilarities(graph, texts);
            graph = filterGraph(graph);
            List<Node> finalNodes = applyPageRank(graph);
            finalNodes = getRelatedNodes(finalNodes);
            graph = new Graph();
            finalNodes = fixQuotationOrdering(finalNodes);
            finalNodes = generateFinalStringFromList(finalNodes, getSize(finalNodes));
            graph.addNodes(finalNodes);
            return new Summary(graph.getNodes(), generateFinalString(finalNodes), articles);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error in summarisation", e);
            return null;
        }
    }

    private List<Node> getRelatedNodes(List<Node> strippedNodes) {
        List<Node> finals = new ArrayList<>();
        for (Node node : strippedNodes) {
            if (finals.contains(node) || finals.stream().anyMatch(n -> n.getRelatedNodes().contains(node))) {
                continue;
            }
            for (Node secondNode : strippedNodes) {
                if (finals.contains(secondNode) || finals.stream().anyMatch(n -> n.getRelatedNodes().contains(secondNode))) {
                    continue;
                }
                if (node != secondNode) {
                    try {
                        String[] firstNodeWords = node.getSentence().toLowerCase().split("\\W+");
                        String[] secondNodeWords = secondNode.getSentence().toLowerCase().split("\\W+");
//                        List<String> stopList = IOUtils.readLines(getClass().getResourceAsStream("/en.txt"), "UTF-8");
                        List<String> firstNodeList = Arrays.stream(firstNodeWords).collect(Collectors.toList());//.filter(string -> !stopList.contains(string)).collect(Collectors.toList());
                        List<String> secondNodeList = Arrays.stream(secondNodeWords).collect(Collectors.toList());//.filter(string -> !stopList.contains(string)).collect(Collectors.toList());
                        double size = secondNodeList.size();
                        if (firstNodeList.size() > secondNodeList.size()) {
                            size = firstNodeList.size();
                        }
                        double firstTotal = firstNodeList.stream().filter(secondNodeList::contains).count() / size;
                        if (firstTotal > 0.75) {
                            node.addNode(secondNode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            finals.add(node);
        }
        return finals;
    }

    private List<Node> generateFinalStringFromList(List<Node> finalNodes, int textSize) {
        List<Node> strippedNodes = finalNodes.stream().limit(textSize).collect(Collectors.toList());
        strippedNodes = strippedNodes.stream().sorted(Comparator.comparing(Node::getSentencePosition)).collect(Collectors.toList());
        return strippedNodes;
    }

    private String generateFinalString(List<Node> strippedNodes) {
//        List<String> finalStrings = stripClausesAndSentences(strippedNodes, strippedNodes.stream().map(Node::getSentence).collect(Collectors.toList()));
        return Combiner.combineStrings(strippedNodes.stream().map(Node::getSentence).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private List<Node> fixQuotationOrdering(List<Node> nodes) {
        List<Node> resultNodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            Node currentNode = nodes.get(i);
            String currentSentence = currentNode.getSentence();
            int numberOfQuotationMarks = numberOfQuotationMarks(currentSentence);
            if (numberOfQuotationMarks % 2 == 1) {
                boolean quotationResolved = false;
                int incrementJ;
                for (int j = i + 1; j < nodes.size(); j += incrementJ) {
                    Node nextNode = nodes.get(j);
                    if (nextNode.getSource().equals(currentNode.getSource()) && nextNode.getAbsoluteSentencePosition() == currentNode.getAbsoluteSentencePosition() + 1) {
                        String nextSentence = nextNode.getSentence();
                        nextSentence = nextSentence.substring(1);
                        currentSentence += " " + nextSentence;
                        incrementJ = 0;
                        nodes.remove(j);
                        if (nextSentence.charAt(nextSentence.length() - 1) == (char) 8221) {
                            quotationResolved = true;
                            break;
                        }
                    } else {
                        incrementJ = 1;
                    }
                }
                if (quotationResolved) {
                    currentNode.setSentence(currentSentence);
                    resultNodes.add(currentNode);
                }
            } else {
//                boolean canAdd = examineQuotation(nodes, currentNode);
//                if (canAdd) {
                    resultNodes.add(currentNode);
//                }
            }
        }
        return resultNodes;
    }

    private boolean examineQuotation(List<Node> nodes, Node currentNode) {
        String source = currentNode.getSource();
        List<Node> sourceNodes = nodes.stream().filter(node -> node.getSource().equals(source)).collect(Collectors.toList());
        int startingIndex = sourceNodes.indexOf(currentNode);
        int absoluteSentenceNumber = currentNode.getAbsoluteSentencePosition();
        if (startingIndex > 0 && sourceNodes.get(startingIndex - 1).getAbsoluteSentencePosition() == absoluteSentenceNumber - 1)
        {
            return true;
        }
        OutletArticle article = articles.stream().filter(outletArticle -> outletArticle.getSource().equals(source)).findFirst().get();
        String[] sentences = new SentenceDetection().detectSentences(article.getBody());
        for (int i = absoluteSentenceNumber - 1; i >= 0; i--) {
            try {
                String currentSentence = sentences[i];
                int numberOfQuotationMarks = numberOfQuotationMarks(currentSentence);
                if (numberOfQuotationMarks > 0 && numberOfQuotationMarks % 2 == 0) {
                    break;
                } else if (numberOfQuotationMarks % 2 == 1) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private int numberOfQuotationMarks(String sentence) {
        return Utils.numberOfTimesACharacterOccursInAString(sentence, (char) 8220)
                + Utils.numberOfTimesACharacterOccursInAString(sentence, (char) 8221)
                + Utils.numberOfTimesACharacterOccursInAString(sentence, (char) 34);
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

    private List<String> stripClausesAndSentences(List<Node> nodes, List<String> summaryStrings) {
        ExtractSentenceTypes extractSentenceTypes = NLPSingleton.getInstance();
        Iterator<Node> nodeIterator = nodes.iterator();
        while (nodeIterator.hasNext()) {
            Node firstNode = nodeIterator.next();
            boolean removeNode = false;
            for (Node secondNode : nodes) {
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

    public int getSize(List<Node> finalNodes) {
        if (texts.size() > 1) {
            return finalNodes.size() / texts.size();
        }
        return (int) Math.ceil(finalNodes.size() / 2.0);
    }
}
