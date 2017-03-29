package com.newsaggregator.ml.tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        double tf = calculateTf(frequencies, term);
        double idf = calculateIdf(term);
        return tf * idf;
    }

    public static double performTfIdfCosineSimilarities(String sentence1, String sentence2) {
        Map<String, Long> sentence1Freq = calculateFrequencies(sentence1);
        Map<String, Long> sentence2Freq = calculateFrequencies(sentence2);
        List<Map<String, Long>> corpusSizes = new ArrayList<>();
        corpusSizes.add(sentence1Freq);
        corpusSizes.add(sentence2Freq);
        double topHalfTotal = 0;
        double bottomFirstSentenceTotal = 0;
        double bottomSecondSentenceTotal = 0;

        for (Map.Entry<String, Long> entry : sentence1Freq.entrySet()) {
            topHalfTotal += completeTopHalf(entry.getKey(), corpusSizes);
            bottomFirstSentenceTotal += bottomHalf(entry.getKey(), corpusSizes);
        }

        for (Map.Entry<String, Long> entry : sentence2Freq.entrySet()) {
            topHalfTotal += completeTopHalf(entry.getKey(), corpusSizes);
            bottomSecondSentenceTotal += bottomHalf(entry.getKey(), corpusSizes);
        }

        bottomFirstSentenceTotal = Math.pow(bottomFirstSentenceTotal, 0.5);
        bottomSecondSentenceTotal = Math.pow(bottomSecondSentenceTotal, 0.5);

        return topHalfTotal / (bottomFirstSentenceTotal * bottomSecondSentenceTotal);
    }

    private static double completeTopHalf(String word, List<Map<String, Long>> corpusSizes) {
        double s1TF = calculateTf(corpusSizes.get(0), word);
        double s2TF = calculateTf(corpusSizes.get(1), word);
        double idf = idfCalculation(word, corpusSizes, 2);
        return s1TF * s2TF * Math.pow(idf, 2);
    }

    private static double bottomHalf(String word, List<Map<String, Long>> corpusSizes) {
        double TF = calculateTf(corpusSizes.get(0), word);
        double IDF = idfCalculation(word, corpusSizes, 2);
        return Math.pow((TF * IDF), 2);
    }

    private double calculateIdf(String term) {
        return idfCalculation(term, this.corpusSizes, this.corpusSize);
    }

    private static double idfCalculation(String term, List<Map<String, Long>> corpusSizes, int corpusSize) {
        int numberOfDocumentsWithTerm = corpusSizes.stream().filter(map -> map.containsKey(term.toLowerCase())).collect(Collectors.toList()).size();
        if (numberOfDocumentsWithTerm == 0) {
            numberOfDocumentsWithTerm++;
        }
        double inverse = (float) corpusSize / numberOfDocumentsWithTerm;
        return Math.log(inverse);
    }

    private static double calculateTf(Map<String, Long> frequencies, String term) {
        if (frequencies.containsKey(term.toLowerCase())) {
            long termFrequency = frequencies.get(term.toLowerCase());
            long maxFrequency = Collections.max(frequencies.entrySet(), Map.Entry.comparingByValue()).getValue();
            return 0.5 + (0.5 * ((float) termFrequency / maxFrequency));
        }
        return 0;
    }

    private static Map<String, Long> calculateFrequencies(String document) {
        Stream<String> stream = Stream.of(document.toLowerCase().split("\\W+")).parallel();

        return stream
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
    }

}
