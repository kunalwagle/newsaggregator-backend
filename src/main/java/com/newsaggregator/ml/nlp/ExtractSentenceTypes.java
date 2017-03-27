package com.newsaggregator.ml.nlp;

import java.util.List;

/**
 * Created by kunalwagle on 27/03/2017.
 */
public class ExtractSentenceTypes {

    private Tokenisation tokeniser;
    private SentenceDetection sentenceDetector;
    private POSTagger tagger;

    public ExtractSentenceTypes() {
        tokeniser = new Tokenisation();
        sentenceDetector = new SentenceDetection();
        tagger = new POSTagger();
    }

    public String nounifyDocument(String document) {
        List<String> nouns = individualNouns(document);
        return String.join(" ", nouns);
    }

    public List<String> individualNouns(String document) {
        String[] sentences = sentenceDetector.detectSentences(document);
        List<String> tokens = tokeniser.findTokens(sentences);
        String[] tags = tagger.tagWords(tokens);
        return tagger.filterNouns(tokens, tags);
    }

}
