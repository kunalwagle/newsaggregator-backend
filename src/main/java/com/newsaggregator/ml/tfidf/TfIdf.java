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

    private double calculateIdf(String term) {
        int numberOfDocumentsWithTerm = corpusSizes.stream().filter(map -> map.containsKey(term.toLowerCase())).collect(Collectors.toList()).size() + 1;
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
        Stream<String> stream = Stream.of(document.toLowerCase().split("\\W+")).parallel();

        return stream
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
    }

}
