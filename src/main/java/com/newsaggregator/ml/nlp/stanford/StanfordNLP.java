package com.newsaggregator.ml.nlp.stanford;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
import com.newsaggregator.ml.summarisation.Combiner;
import com.newsaggregator.ml.summarisation.Extractive.Node;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
            List<CoreMap> sentences = stanfordAnalysis.getSentences();
            for (CorefChain corefChain : graph.values()) {
                CorefChain.CorefMention representativeMention = corefChain.getRepresentativeMention();
                String mentionString = "";
                if (corefChain.getMentionsInTextualOrder().size() > 1) {
                    mentionString = representativeMention.mentionSpan;
                }
                for (CorefChain.CorefMention mention : corefChain.getMentionsInTextualOrder()) {
                    if (!mention.equals(representativeMention)) {
                        if (mention.sentNum - 1 == node.getAbsoluteSentencePosition()) {
                            String[] tokens = extractSentenceTypes.tag(sentence);
                            if (extractSentenceTypes.isPronoun(tokens[mention.startIndex - 1])) {
                                tokens[mention.startIndex - 1] = mentionString;
                            }
                            node.setSentence(Combiner.combineStrings(Arrays.asList(tokens)));
                        }
                    }
                }
            }
        }
        return nodes;
    }

    public Annotation getDocument() {
        return document;
    }
}
