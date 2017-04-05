package com.newsaggregator.ml.summarisation.Abstractive;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.nlp.ExtractSentenceTypes;
import com.newsaggregator.ml.nlp.SentenceDetection;
import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Extractive.Graph;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import com.newsaggregator.ml.summarisation.Summarisation;
import com.newsaggregator.ml.summarisation.Summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<String> strippedSentences = new ArrayList<>();// = stripClausesAndSentences();
        preProcessPronouns(strippedSentences);
        return createSummary(strippedSentences);
    }

    private void preProcessPronouns(List<String> sentences) {
        Graph graph = createNewGraphFromSentences(sentences);
        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
        SentenceDetection sentenceDetection = new SentenceDetection();
        Map<String, List<Node>> nodeSourceMap = new HashMap<>();
        for (OutletArticle article : initialSummary.getArticles()) {
            String source = article.getSource();
            List<Node> nodes = graph.getAllSentencesForASingleSource(source);
            nodeSourceMap.put(source, nodes);
        }
        for (Map.Entry<String, List<Node>> entry : nodeSourceMap.entrySet()) {
            String source = entry.getKey();
            List<Node> sourceNodes = entry.getValue();
            OutletArticle originalArticle = initialSummary
                    .getArticles()
                    .stream()
                    .filter(article -> article.getSource().equals(source))
                    .findFirst()
                    .get();
            String[] articleSentences = sentenceDetection.detectSentences(originalArticle.getBody());
            for (Node node : sourceNodes) {
                String nodeSentence = node.getSentence();
                if (extractSentenceTypes.pronounsExist(nodeSentence)) {
                    String previousArticleSentence = "";
                    for (int i = 1; i < articleSentences.length; i++) {
                        if (articleSentences[i].equals(nodeSentence)) {
                            previousArticleSentence = articleSentences[i - 1];
                            break;
                        }
                    }

                }
            }
        }
//        for (Node node : graph.getNodes()) {
//            if (extractSentenceTypes.pronounsExist(node.getSentence())) {
//                String source = node.getSource();
//                OutletArticle article = initialSummary.getArticles().stream().filter(outletArticle -> source.equals(outletArticle.getSource())).findFirst().get();
//                List<String> articleSentences = new ArrayList<>(Arrays.asList(sentenceDetection.detectSentences(article.getBody())));
//                List<Node> summarySentences = graph.getAllSentencesForASingleSource(source);
//                for (int i=1; i<articleSentences.size(); i++) {
//                    String articleSentence = articleSentences.get(i);
//                    boolean pronounsExist = extractSentenceTypes.pronounsExist(articleSentence);
//                    if (pronounsExist && summarySentences.stream().anyMatch(currentNode -> currentNode.getSentence().equals(articleSentence))) {
//                        = summarySentences.stream().filter(sentence -> sentence.equals(articleSentence)).findFirst().get();
//
//                    }
//                }
//            }
//        }
    }

    private Graph createNewGraphFromSentences(List<String> sentences) {
        List<Node> nodesToKeep = new ArrayList<>();
        nodesToKeep.addAll(sentences.stream().map(initialSummary.getGraph()::getNodeForSentence).collect(Collectors.toList()));
        Graph graph = new Graph();
        graph.addNodes(nodesToKeep);
        return graph;
    }

    private Summary createSummary(List<String> strippedSentences) {
        return new Summary(null, Combiner.combineStrings(strippedSentences), initialSummary.getArticles());
    }

//    private List<String> stripClausesAndSentences() {
//        Graph graph = initialSummary.getGraph();
//        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
//        Iterator<Node> nodeIterator = graph.getNodes().iterator();
//        SentenceDetection sentenceDetection = new SentenceDetection();
//        List<String> summaryStrings = new LinkedList<>(Arrays.asList(sentenceDetection.detectSentences(initialSummary.getText())));
//        while (nodeIterator.hasNext()) {
//            Node firstNode = nodeIterator.next();
//            boolean removeNode = false;
//            for (Node secondNode : graph.getNodes()) {
//                if (summaryStrings.contains(firstNode.getSentence())
//                        && summaryStrings.contains(secondNode.getSentence())
//                        && firstNode.getIdentifier() != secondNode.getIdentifier()) {
//                    List<String> firstSentence = extractSentenceTypes.allWords(firstNode.getSentence());
//                    List<String> secondSentence = extractSentenceTypes.allWords(secondNode.getSentence());
//                    double total = 0.0;
//                    for (String noun : firstSentence) {
//                        for (String secondNoun : secondSentence) {
//                            if (noun.toLowerCase().equals(secondNoun.toLowerCase())) {
//                                total++;
//                            }
//                        }
//                    }
//                    double similarity = total / ((firstSentence.size() + secondSentence.size()) / 2);
//                    if (similarity > 0.8) {
//                        Iterator<String> stringIterator = summaryStrings.iterator();
//                        while (stringIterator.hasNext()) {
//                            if (stringIterator.next().equals(firstNode.getSentence())) {
//                                stringIterator.remove();
//                            }
//                        }
//                        removeNode = true;
//                    }
//                }
//            }
//            if (removeNode) {
//                nodeIterator.remove();
//            }
//        }
//        return summaryStrings;
//    }

}
