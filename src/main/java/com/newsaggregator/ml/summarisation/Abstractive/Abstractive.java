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
        List<String> strippedSentences = new ArrayList<>();// = stripClausesAndSentences();
        preProcessPronouns();
        return createSummary(strippedSentences);
    }

    private void preProcessPronouns() {

        Graph graph = createNewGraphFromSentences();
        List<Node> nodes = combineNodesIfFromSameArticle(graph);
        SentenceDetection sentenceDetection = new SentenceDetection();
        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
        List<OutletArticle> articles = initialSummary.getArticles();

        List<List<Node>> splitNodes = new ArrayList<>();

        for (OutletArticle article : articles) {
            splitNodes.add(graph.getAllSentencesForASingleSource(article.getSource()));
        }

        for (List<Node> articleNodes : splitNodes) {
            for (int i = 0; i < articleNodes.size(); i++) {
                Node node = articleNodes.get(i);
                if (extractSentenceTypes.pronounsExist(node.getSentence())) {
                    int absoluteSentencePosition = node.getAbsoluteSentencePosition();
                    OutletArticle article = articles.get(i);

                }
            }
        }


//        Graph graph = createNewGraphFromSentences();
//        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
//        SentenceDetection sentenceDetection = new SentenceDetection();
//        Map<String, List<Node>> nodeSourceMap = new HashMap<>();
//        for (OutletArticle article : initialSummary.getArticles()) {
//            String source = article.getSource();
//            List<Node> nodes = graph.getAllSentencesForASingleSource(source);
//            nodeSourceMap.put(source, nodes);
//        }
//        for (Map.Entry<String, List<Node>> entry : nodeSourceMap.entrySet()) {
//            String source = entry.getKey();
//            List<Node> sourceNodes = entry.getValue();
//            OutletArticle originalArticle = initialSummary
//                    .getArticles()
//                    .stream()
//                    .filter(article -> article.getSource().equals(source))
//                    .findFirst()
//                    .get();
//            String[] articleSentences = sentenceDetection.detectSentences(originalArticle.getBody());
//            for (Node node : sourceNodes) {
//                String nodeSentence = node.getSentence();
//                if (extractSentenceTypes.pronounsExist(nodeSentence)) {
//                    String previousArticleSentence = "";
//                    for (int i = 1; i < articleSentences.length; i++) {
//                        if (articleSentences[i].equals(nodeSentence)) {
//                            previousArticleSentence = articleSentences[i - 1];
//                            break;
//                        }
//                    }
//
//                }
//            }
//        }
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

    private List<Node> combineNodesIfFromSameArticle(Graph graph) {
        List<Node> nodes = initialSummary.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            int startingIndex = node.getAbsoluteSentencePosition();
            List<Node> nodesForArticle = graph.getAllSentencesForASingleSource(node.getSource());
            SentenceDetection sentenceDetection = new SentenceDetection();

        }
        return null;
    }

    private Graph createNewGraphFromSentences() {
        Graph graph = new Graph();
        graph.addNodes(initialSummary.getNodes());
        return graph;
    }

    private Summary createSummary(List<String> strippedSentences) {
        return new Summary(null, Combiner.combineStrings(strippedSentences), initialSummary.getArticles());
    }

}
