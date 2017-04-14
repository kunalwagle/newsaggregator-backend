package com.newsaggregator.ml.nlp.stanford;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Dictionaries;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by kunalwagle on 09/04/2017.
 */
public class StanfordNLP {


    private StanfordCoreNLP pipeline;
    private Annotation document;
    private OutletArticle article;

    public StanfordNLP(StanfordCoreNLP pipeline, OutletArticle article) {
        this.pipeline = pipeline;
        document = new Annotation(article.getBody());
        this.article = article;
    }

    public static StanfordCoreNLP getCoreNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,ner,mention,depparse,coref");
        return new StanfordCoreNLP(props);
    }

    public StanfordAnalysis performAnalysis() {

        pipeline.annotate(document);

        return new StanfordAnalysis(article.getSource(), document);

    }

    public static List<Node> performPronounResolution(List<Node> nodes, List<StanfordAnalysis> stanfordAnalyses) {

        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();

        for (StanfordAnalysis stanfordAnalysis : stanfordAnalyses) {
            Map<Integer, CorefChain> graph = stanfordAnalysis.getAnnotation().get(CorefCoreAnnotations.CorefChainAnnotation.class);
            for (CorefChain corefChain : graph.values()) {
                CorefChain.CorefMention representativeMention = corefChain.getRepresentativeMention();
                String mentionString = "";
                if (corefChain.getMentionsInTextualOrder().size() > 1) {
                    mentionString = representativeMention.mentionSpan;
                }
                for (CorefChain.CorefMention mention : corefChain.getMentionsInTextualOrder()) {
                    if (!mention.equals(representativeMention) && (representativeMention.mentionType.equals(Dictionaries.MentionType.NOMINAL) || representativeMention.mentionType.equals(Dictionaries.MentionType.PROPER))) {
                        if (nodes.stream().anyMatch(node -> mentionInNodeSentence(node, mention.sentNum, stanfordAnalysis.getSource(), representativeMention.sentNum))) {
                            Node node = nodes.stream().filter(node1 -> mentionInNodeSentence(node1, mention.sentNum, stanfordAnalysis.getSource(), representativeMention.sentNum)).findFirst().get();
                            String sentence = node.getSentence();
                            List<String> words = extractSentenceTypes.allWords(sentence);
                            String[] tokens = extractSentenceTypes.tag(sentence);
                            if (extractSentenceTypes.isNonFirstPersonPronoun(tokens[mention.startIndex - 1], words.get(mention.startIndex - 1))) {
                                String mentionStringCopy = mentionString;
                                if (extractSentenceTypes.isPossessivePronoun(tokens[mention.startIndex - 1])) {
                                    if (mentionString.endsWith("s")) {
                                        mentionStringCopy += "\'";
                                    } else {
                                        mentionStringCopy += "\'s";
                                    }
                                }
                                sentence = sentence.replaceAll("\\b" + mention.mentionSpan + "\\b", mentionStringCopy);
                            }
                            node.setSentence(sentence);
                        }
                    }
                }
            }
        }
        return nodes;
    }

    private static boolean mentionInNodeSentence(Node node, int sentNum, String source, int originalSentNum) {
        return (node.getAbsoluteSentencePosition() == sentNum - 1) &&
                (node.getSource().equals(source)) &&
                (originalSentNum != sentNum);
    }

    public static List<Collection<TypedDependency>> getTypedDependencies(List<Node> nodes, List<StanfordAnalysis> stanfordAnalyses) {
        List<Collection<TypedDependency>> result = new ArrayList<>();
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        for (Node node : nodes) {
            StanfordAnalysis stanfordAnalysis = stanfordAnalyses.stream().filter(stanford -> stanford.getSource().equals(node.getSource())).findFirst().get();
            CoreMap sentence = stanfordAnalysis.getSentences().get(node.getAbsoluteSentencePosition());
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
            result.add(gs.typedDependenciesCollapsed());
        }
        return result;
    }

    public Annotation getDocument() {
        return document;
    }
}
