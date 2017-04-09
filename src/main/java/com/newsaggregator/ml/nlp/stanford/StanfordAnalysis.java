package com.newsaggregator.ml.nlp.stanford;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

/**
 * Created by kunalwagle on 09/04/2017.
 */
public class StanfordAnalysis {

    private String source;
    private List<CoreMap> sentences;

    public StanfordAnalysis(String source, Annotation annotation) {
        this.source = source;
        this.sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
    }


}
