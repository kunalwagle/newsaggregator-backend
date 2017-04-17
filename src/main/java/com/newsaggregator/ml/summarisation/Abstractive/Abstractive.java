//package com.newsaggregator.ml.summarisation.Abstractive;
//
//import com.google.common.collect.Sets;
//import com.newsaggregator.base.OutletArticle;
//import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
//import com.newsaggregator.ml.nlp.apache.SentenceDetection;
//import com.newsaggregator.ml.nlp.stanford.StanfordAnalysis;
//import com.newsaggregator.ml.nlp.stanford.StanfordNLP;
//import com.newsaggregator.ml.nlp.wordnet.Wordnet;
//import com.newsaggregator.ml.nlp.wordnet.WordnetFailedToOpenException;
//import com.newsaggregator.ml.summarisation.Combiner;
//import com.newsaggregator.ml.summarisation.Extractive.Graph;
//import com.newsaggregator.ml.summarisation.Extractive.Node;
//import com.newsaggregator.ml.summarisation.Summarisation;
//import com.newsaggregator.ml.summarisation.Summary;
//import edu.mit.jwi.item.IIndexWord;
//import edu.mit.jwi.item.IWord;
//import edu.mit.jwi.item.IWordID;
//import edu.mit.jwi.item.POS;
//import edu.stanford.nlp.ie.util.RelationTriple;
//import edu.stanford.nlp.ling.CoreAnnotations;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.util.CoreMap;
//import simplenlg.features.Feature;
//import simplenlg.features.Tense;
//import simplenlg.framework.NLGFactory;
//import simplenlg.lexicon.Lexicon;
//import simplenlg.phrasespec.NPPhraseSpec;
//import simplenlg.phrasespec.SPhraseSpec;
//import simplenlg.phrasespec.VPPhraseSpec;
//import simplenlg.realiser.english.Realiser;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * Created by kunalwagle on 29/03/2017.
// */
//public class Abstractive implements Summarisation {
//
//    private Summary initialSummary;
//    private List<StanfordAnalysis> stanfordAnalyses;
//    private Wordnet wordnet = new Wordnet();
//
//    public Abstractive(Summary initialSummary) throws IOException, WordnetFailedToOpenException {
//        this.initialSummary = initialSummary;
//    }
//
//    @Override
//    public Summary summarise() {
//        //List<String> strippedSentences = new ArrayList<>();// = stripClausesAndSentences();
//        //List<Node> nodes = preProcessPronouns();
//        List<Node> nodes = initialSummary.getNodes();
////        nodes = preProcessing(nodes);
//        RSGraph subGraph = createRichSemanticGraphs(nodes);
//        subGraph = reduceRichSemanticGraphs(subGraph);
//        List<String> sentences = generateSummaryText(subGraph);
//        return createSummary(nodes, sentences);
//    }
//
//    private List<String> generateSummaryText(RSGraph graph) {
//        Lexicon lexicon = Lexicon.getDefaultLexicon();
//        NLGFactory factory = new NLGFactory(lexicon);
//        Realiser realiser = new Realiser(lexicon);
//        graph.synonymisation();
//        List<String> sentences = new ArrayList<>();
//        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
//        for (RSNode node : graph.getNodes()) {
//            Collection<RelationTriple> triples = node.getRelationTriples();
//            List<RSWord> words = node.getWords();
//            Iterator<RelationTriple> relationTripleIterator = triples.iterator();
//            while (relationTripleIterator.hasNext()) {
//                RelationTriple triple = relationTripleIterator.next();
//                List<CoreLabel> label = triple.asSentence();
//                List<String> subject = synonymReplacer(extractSentenceTypes.allWords(triple.subjectLemmaGloss()), words);
//                List<String> verb = synonymReplacer(extractSentenceTypes.allWords(triple.relationLemmaGloss()), words);
//                List<String> object = synonymReplacer(extractSentenceTypes.allWords(triple.objectLemmaGloss()), words);
//                NPPhraseSpec s = factory.createNounPhrase(Combiner.combineStrings(subject));
//                VPPhraseSpec v = factory.createVerbPhrase(Combiner.combineStrings(verb));
//                NPPhraseSpec o = factory.createNounPhrase(Combiner.combineStrings(object));
//                SPhraseSpec phrase = factory.createClause();
//                phrase.setSubject(s);
//                phrase.setObject(o);
//                phrase.setVerb(v);
//                phrase.setFeature(Feature.TENSE, Tense.PAST);
//                sentences.add(realiser.realiseSentence(phrase));
//            }
//        }
//        return sentences;
//    }
//
//    private List<String> synonymReplacer(List<String> strings, List<RSWord> words) {
//        List<String> newStrings = new ArrayList<>();
//        for (String string : strings) {
//            boolean wordExists = words.stream().anyMatch(w -> w.getLemma().equals(string));
//            if (wordExists) {
//                newStrings.add(words.stream().filter(w -> w.getLemma().equals(string)).findAny().get().getSynonym());
//            }
//        }
//        return newStrings;
//    }
//
//    private RSGraph reduceRichSemanticGraphs(RSGraph subGraph) {
//        subGraph.applyHeuristics();
//        return subGraph;
//    }
//
//    private RSGraph createRichSemanticGraphs(List<Node> nodes) {
//        //stanfordAnalyses = initialSummary.getArticles().stream().map(StanfordNLP::performAnalysis).collect(Collectors.toList());
//        List<Node> preProcessedNodes = preProcessing(nodes);
//        List<RSSubGraph> subGraphs = subGraphGeneration(preProcessedNodes);
//        return graphGeneration(subGraphs);
//    }
//
//    private List<Node> preProcessing(List<Node> nodes) {
//        StanfordCoreNLP coreNLP = StanfordNLP.getCoreNLP();
//        stanfordAnalyses = new ArrayList<>();
//        for (OutletArticle article : initialSummary.getArticles()) {
//            stanfordAnalyses.add(new StanfordNLP(coreNLP, article).performAnalysis());
//        }
//        StanfordNLP.performPronounResolution(nodes, stanfordAnalyses);
//        return nodes;
//    }
//
//    private List<RSSubGraph> subGraphGeneration(List<Node> preProcessedNodes) {
//        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
//        List<RSSubGraph> subGraphs = new ArrayList<>();
//        List<Collection<RelationTriple>> relationTriples = StanfordNLP.getRelationTriples(preProcessedNodes, stanfordAnalyses);
//        for (Node node : preProcessedNodes) {
//            String sentence = node.getSentence();
//            StanfordAnalysis stanfordAnalysis = stanfordAnalyses.stream().filter(stanford -> stanford.getSource().equals(node.getSource())).findFirst().get();
//            CoreMap sent = stanfordAnalysis.getSentences().get(node.getAbsoluteSentencePosition());
//            List<CoreLabel> words = sent.get(CoreAnnotations.TokensAnnotation.class);
//            String[] tags = extractSentenceTypes.tag(sentence);
//            ArrayList<IIndexWord> wordNetWords = new ArrayList<>();
//            for (int i = 0; i < tags.length; i++) {
//                String word = words.get(i).lemma();
//                POS pos;
//                if (extractSentenceTypes.isNoun(tags[i])) {
//                    pos = POS.NOUN;
//                } else if (extractSentenceTypes.isVerb(tags[i])) {
//                    pos = POS.VERB;
//                } else {
//                    continue;
//                }
//                IIndexWord w = wordnet.getWord(word, pos);
//                if (w != null) {
//                    wordNetWords.add(w);
//                }
//            }
//            List<Set<RSWord>> senses = new ArrayList<>();
//            for (IIndexWord indexWord : wordNetWords) {
//                List<IWordID> iWordIDS = indexWord.getWordIDs();
//                int size = iWordIDS.size();
//                senses.add(iWordIDS.stream().map(iWord -> generateRSWord(iWord, size)).collect(Collectors.toSet()));
//            }
//            Set<List<RSWord>> sets = Sets.cartesianProduct(senses);
//            List<RSNode> rsNodes = new ArrayList<>();
//            for (List<RSWord> rsWords : sets) {
//                RSNode rsNode = new RSNode(rsWords, relationTriples.get(preProcessedNodes.indexOf(node)));
//                rsNodes.add(rsNode);
//            }
//            subGraphs.add(new RSSubGraph(rsNodes));
//        }
//        return subGraphs;
//    }
//
//    private RSWord generateRSWord(IWordID iWord, int size) {
//        IWord word = wordnet.getWord(iWord);
//        return new RSWord(word.getLemma(), word, size, wordnet);
//    }
//
//    private RSGraph graphGeneration(List<RSSubGraph> subGraphs) {
//        return new RSGraph(subGraphs.stream().map(RSSubGraph::filterNodes).collect(Collectors.toList()));
//    }
//
//    private List<Node> preProcessPronouns() {
//
//        Graph graph = createNewGraphFromSentences();
//        List<Node> nodes = combineNodesIfFromSameArticle(graph);
//        SentenceDetection sentenceDetection = new SentenceDetection();
//        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
//        List<OutletArticle> articles = initialSummary.getArticles();
//
//        List<List<Node>> splitNodes = new ArrayList<>();
//
//        for (OutletArticle article : articles) {
//            splitNodes.add(graph.getAllSentencesForASingleSource(article.getSource()));
//        }
//
//        for (int i = 0; i < splitNodes.size(); i++) {
//            OutletArticle article = articles.get(i);
//            List<Node> articleNodes = splitNodes.get(i);
//            String[] articleSentences = sentenceDetection.detectSentences(article.getBody());
//            for (Node node : articleNodes) {
//                int position = node.getAbsoluteSentencePosition();
//                String startingString = articleSentences[position];
//                if (extractSentenceTypes.pronounsExist(startingString)) {
////                    if (position != 0 && pronounsInSubject(startingString, extractSentenceTypes)) {
////                        String nounPhrase = obtainNounPhraseFromPreviousSentence(articleSentences[position-1], extractSentenceTypes);
////                        String sentence = replacePronounWithNounPhrase(nounPhrase, startingString, extractSentenceTypes);
////                        articleSentences[position] = sentence;
////                        String[] sentences = sentenceDetection.detectSentences(node.getSentence());
////                        sentences[0] = sentence;
////                        node.setSentence(Combiner.combineStrings(Arrays.asList(sentences)));
////                    }
//                    if (position != 0) {
//                        String previousSentence = articleSentences[position - 1];
//                        String currentSentence = node.getSentence();
//                        previousSentence += " " + currentSentence;
//                        node.setSentence(previousSentence);
//                        node.setAbsoluteSentencePosition(position - 1);
//                    }
//                }
//            }
//        }
//
//        graph = createNewGraphFromSentences();
//        nodes = combineNodesIfFromSameArticle(graph);
//
//        return nodes;
//
//
////        Graph graph = createNewGraphFromSentences();
////        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
////        SentenceDetection sentenceDetection = new SentenceDetection();
////        Map<String, List<Node>> nodeSourceMap = new HashMap<>();
////        for (OutletArticle article : initialSummary.getArticles()) {
////            String source = article.getSource();
////            List<Node> nodes = graph.getAllSentencesForASingleSource(source);
////            nodeSourceMap.put(source, nodes);
////        }
////        for (Map.Entry<String, List<Node>> entry : nodeSourceMap.entrySet()) {
////            String source = entry.getKey();
////            List<Node> sourceNodes = entry.getValue();
////            OutletArticle originalArticle = initialSummary
////                    .getArticles()
////                    .stream()
////                    .filter(article -> article.getSource().equals(source))
////                    .findFirst()
////                    .get();
////            String[] articleSentences = sentenceDetection.detectSentences(originalArticle.getBody());
////            for (Node node : sourceNodes) {
////                String nodeSentence = node.getSentence();
////                if (extractSentenceTypes.pronounsExist(nodeSentence)) {
////                    String previousArticleSentence = "";
////                    for (int i = 1; i < articleSentences.length; i++) {
////                        if (articleSentences[i].equals(nodeSentence)) {
////                            previousArticleSentence = articleSentences[i - 1];
////                            break;
////                        }
////                    }
////
////                }
////            }
////        }
////        for (Node node : graph.getNodes()) {
////            if (extractSentenceTypes.pronounsExist(node.getSentence())) {
////                String source = node.getSource();
////                OutletArticle article = initialSummary.getArticles().stream().filter(outletArticle -> source.equals(outletArticle.getSource())).findFirst().get();
////                List<String> articleSentences = new ArrayList<>(Arrays.asList(sentenceDetection.detectSentences(article.getBody())));
////                List<Node> summarySentences = graph.getAllSentencesForASingleSource(source);
////                for (int i=1; i<articleSentences.size(); i++) {
////                    String articleSentence = articleSentences.get(i);
////                    boolean pronounsExist = extractSentenceTypes.pronounsExist(articleSentence);
////                    if (pronounsExist && summarySentences.stream().anyMatch(currentNode -> currentNode.getSentence().equals(articleSentence))) {
////                        = summarySentences.stream().filter(sentence -> sentence.equals(articleSentence)).findFirst().get();
////
////                    }
////                }
////            }
////        }
//    }
//
//    private String replacePronounWithNounPhrase(String nounPhrase, String string, ExtractSentenceTypes extractSentenceTypes) {
//        String[] chunks = extractSentenceTypes.chunk(string);
//        String[] pos = extractSentenceTypes.tag(string);
//        List<String> words = extractSentenceTypes.allWords(string);
//        for (int i = findStartingPointForSubject(chunks); i < chunks.length; i++) {
//            if (extractSentenceTypes.isPronoun(pos[i])) {
//                if (extractSentenceTypes.isPossessivePronoun(pos[i])) {
//                    if (nounPhrase.endsWith("s")) {
//                        nounPhrase += "\'";
//                    } else {
//                        nounPhrase += "\'s";
//                    }
//                }
//                words.set(i, nounPhrase);
//            }
//            if (chunks[i].contains("VP")) {
//                break;
//            }
//        }
//        return Combiner.combineStrings(words);
//    }
//
//    private String obtainNounPhraseFromPreviousSentence(String articleSentence, ExtractSentenceTypes extractSentenceTypes) {
//        String[] chunks = extractSentenceTypes.chunk(articleSentence);
//        List<String> tokens = extractSentenceTypes.allWords(articleSentence);
//        String result;
//        int endPoint = findStartingPointForSubject(chunks);
//        if (endPoint == 0) {
//            for (int i = endPoint; i < chunks.length; i++) {
//                if (chunks[i].contains("VP")) {
//                    endPoint = i;
//                    break;
//                }
//            }
//        }
//        result = Combiner.combineStrings(tokens.subList(0, endPoint));
//
//        return result.length() > 0 ? result.substring(1) : result;
//    }
//
//    private boolean pronounsInSubject(String startingString, ExtractSentenceTypes extractSentenceTypes) {
//        String[] chunks = extractSentenceTypes.chunk(startingString);
//        String[] pos = extractSentenceTypes.tag(startingString);
//        for (int i = findStartingPointForSubject(chunks); i < chunks.length; i++) {
//            if (chunks[i].contains("VP")) {
//                break;
//            }
//            if (extractSentenceTypes.isPronoun(pos[i])) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private int findStartingPointForSubject(String[] chunks) {
//        int startingI = 0;
//        String lookingFor = "O";
//        if (chunks[0].contains("VP")) {
//            for (int i = 1; i < chunks.length; i++) {
//                if (chunks[i].contains(lookingFor)) {
//                    if (lookingFor.equals("O") && chunks[i + 1].contains("NP")) {
//                        lookingFor = "VP";
//                    } else {
//                        startingI = i + 1;
//                        break;
//                    }
//                }
//            }
//        }
//        return startingI;
//    }
//
//    private List<Node> combineNodesIfFromSameArticle(Graph graph) {
//        List<Node> nodes = initialSummary.getNodes();
//        int incrementI;
//        for (int i = 0; i < nodes.size() - 1; i += incrementI) {
//            Node node = nodes.get(i);
//            int startingIndex = node.getAbsoluteSentencePosition();
//            List<Node> nodesForArticle = graph.getAllSentencesForASingleSource(node.getSource());
//            SentenceDetection sentenceDetection = new SentenceDetection();
//            int numberOfSentences = sentenceDetection.detectSentences(node.getSentence()).length;
//            int index = nodesForArticle.indexOf(node);
//            Node nextNode = nodes.get(index + 1);
//            if (nextNode.getAbsoluteSentencePosition() == startingIndex + numberOfSentences) {
//                node.setSentence(node.getSentence() + " " + nextNode.getSentence());
//                incrementI = 0;
//                nodes.remove(nextNode);
//            } else {
//                incrementI = 1;
//            }
//        }
//        return nodes;
//    }
//
//    private Graph createNewGraphFromSentences() {
//        Graph graph = new Graph();
//        graph.addNodes(initialSummary.getNodes());
//        return graph;
//    }
//
//    private Summary createSummary(List<Node> nodes, List<String> sentences) {
//        String articleText = "";
//        for (String node : sentences) {
//            articleText += node + "\n";
//        }
//        return new Summary(nodes, articleText, initialSummary.getArticles());
//    }
//
//}
