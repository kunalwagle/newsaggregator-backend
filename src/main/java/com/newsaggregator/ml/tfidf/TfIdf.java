package com.newsaggregator.ml.tfidf;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kunalwagle on 27/02/2017.
 */
public class TfIdf {

    private List<Map<String, Long>> corpusSizes;
    private int corpusSize;

    public TfIdf(List<String> corpus) {
        this.corpusSize = corpus.size();
        this.corpusSizes = new ArrayList<>();
        for (String document : corpus) {
            this.corpusSizes.add(calculateFrequencies(document));
        }
    }

    public double performTfIdf(String document, String term) {
        Map<String, Long> frequencies = calculateFrequencies(document);
        if (frequencies != null) {
            double tf = calculateTf(frequencies, term);
            double idf = calculateIdf(term);
            return tf * idf;
        }
        return 0;
    }

    public double performTfIdfCosineSimilarities(String sentence1, String sentence2) {
        Map<String, Long> sentence1Freq = calculateFrequencies(sentence1);
        Map<String, Long> sentence2Freq = calculateFrequencies(sentence2);
        Map<String, Long> generatedCorpus = generateCorpus(sentence1Freq, sentence2Freq);
        List<Map<String, Long>> sentenceCorpi = Lists.newArrayList(sentence1Freq, sentence2Freq);
        double topHalfTotal = 0;
        double bottomFirstSentenceTotal = 0;
        double bottomSecondSentenceTotal = 0;

        for (Map.Entry<String, Long> entry : generatedCorpus.entrySet()) {
            topHalfTotal += completeTopHalf(entry.getKey(), sentenceCorpi);
        }

        for (Map.Entry<String, Long> entry : sentence1Freq.entrySet()) {
            bottomFirstSentenceTotal += bottomHalf(entry.getKey(), sentence1Freq);
        }

        for (Map.Entry<String, Long> entry : sentence2Freq.entrySet()) {
            bottomSecondSentenceTotal += bottomHalf(entry.getKey(), sentence2Freq);
        }

        bottomFirstSentenceTotal = Math.pow(bottomFirstSentenceTotal, 0.5);
        bottomSecondSentenceTotal = Math.pow(bottomSecondSentenceTotal, 0.5);

        if (bottomFirstSentenceTotal == 0 || bottomSecondSentenceTotal == 0) {
            return 0;
        }

        return topHalfTotal / (bottomFirstSentenceTotal * bottomSecondSentenceTotal);
    }

    private Map<String, Long> generateCorpus(Map<String, Long> sentence1Freq, Map<String, Long> sentence2Freq) {
        Map<String, Long> corpus = new HashMap<>();

        for (Map.Entry<String, Long> entry : sentence1Freq.entrySet()) {
            corpus.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Long> entry : sentence2Freq.entrySet()) {
            if (corpus.containsKey(entry.getKey())) {
                corpus.put(entry.getKey(), entry.getValue() + corpus.get(entry.getKey()));
            } else {
                corpus.put(entry.getKey(), entry.getValue());
            }
        }

        return corpus;
    }

    private double completeTopHalf(String word, List<Map<String, Long>> sentenceCorpi) {
        double s1TF = calculateTf(sentenceCorpi.get(0), word);
        double s2TF = s1TF;
        if (sentenceCorpi.size() > 1) {
            s2TF = calculateTf(sentenceCorpi.get(1), word);
        }
        double idf = idfCalculation(word, corpusSizes, corpusSize);
        return s1TF * s2TF * Math.pow(idf, 2);
    }

    private double bottomHalf(String word, Map<String, Long> corpus) {
        double TF = calculateTf(corpus, word);
        double IDF = idfCalculation(word, corpusSizes, corpusSize);
        return Math.pow((TF * IDF), 2);
    }

    private double calculateIdf(String term) {
        return idfCalculation(term, this.corpusSizes, this.corpusSize);
    }

    private double idfCalculation(String term, List<Map<String, Long>> corpusSizes, int corpusSize) {
        if (term == null) {
            term = "";
        }
        String finalTerm = term;
        int numberOfDocumentsWithTerm = corpusSizes.stream().filter(map -> map.containsKey(finalTerm.toLowerCase())).collect(Collectors.toList()).size();
        if (numberOfDocumentsWithTerm == 0) {
            numberOfDocumentsWithTerm++;
        }
        double inverse = (float) corpusSize / numberOfDocumentsWithTerm;
        return Math.log(inverse);
    }

    private double calculateTf(Map<String, Long> frequencies, String term) {
        if (frequencies.containsKey(term.toLowerCase())) {
            long termFrequency = frequencies.get(term.toLowerCase());
            long maxFrequency = Collections.max(frequencies.entrySet(), Map.Entry.comparingByValue()).getValue();
            return 0.5 + (0.5 * ((float) termFrequency / maxFrequency));
        }
        return 0;
    }

    private Map<String, Long> calculateFrequencies(String document) {
        try {
            Stream<String> stream = Stream.of(document.toLowerCase().split("\\W+")).parallel();

            return stream
                    .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error calculating frequencies", e);
            return null;
        }
    }

}
