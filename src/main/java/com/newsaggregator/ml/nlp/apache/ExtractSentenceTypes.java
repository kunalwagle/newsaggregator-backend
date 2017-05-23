package com.newsaggregator.ml.nlp.apache;

import java.util.List;

/**
 * Created by kunalwagle on 27/03/2017.
 */
public class ExtractSentenceTypes {

    private Tokenisation tokeniser;
    private SentenceDetection sentenceDetector;
    private POSTagger tagger;
    private Chunker chunker;

    public ExtractSentenceTypes() {
        tokeniser = new Tokenisation();
        sentenceDetector = new SentenceDetection();
        tagger = new POSTagger();
        chunker = new Chunker();
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

    public List<String> individualPronouns(String document) {
        String[] sentences = sentenceDetector.detectSentences(document);
        List<String> tokens = tokeniser.findTokens(sentences);
        String[] tags = tagger.tagWords(tokens);
        return tagger.filterPronouns(tokens, tags);
    }

    public boolean pronounsExist(String document) {
        String[] sentences = sentenceDetector.detectSentences(document);
        List<String> tokens = tokeniser.findTokens(sentences);
        String[] tags = tagger.tagWords(tokens);
        return tagger.pronounsExist(tags);
    }

    public boolean isPronoun(String word) {
        return tagger.pronounsExist(new String[]{word});
    }

    public List<String> allWords(String document) {
        String[] sentences = sentenceDetector.detectSentences(document);
        return tokeniser.findTokens(sentences);
    }

    public String[] tag(String document) {
        return tagger.tagWords(allWords(document));
    }

    public String[] chunk(String string) {
        List<String> tokens = tokeniser.findTokens(new String[]{string});
        String[] pos = tagger.tagWords(tokens);
        return chunker.chunk(tokens.toArray(new String[tokens.size()]), pos);
    }


    public boolean isPossessivePronoun(String pronoun) {
        return pronoun.contains("$");
    }

    public boolean isNonFirstPersonPronoun(String token, String s) {
        return isPronoun(token) && !s.toLowerCase().equals("i") && !s.toLowerCase().equals("we");
    }

    public boolean isNoun(String tag) {
        return tag.startsWith("N");
    }

    public boolean isVerb(String tag) {
        return tag.startsWith("V");
    }

    public List<String> nameFinder(String document) {
        String sentences[] = sentenceDetector.detectSentences(document);
        List<String> tokens = tokeniser.findTokens(sentences);
        String toks[] = new String[tokens.size()];
        List<String> strings = new NameFinder().findNames(tokens.toArray(toks));
        return strings;
    }


}
