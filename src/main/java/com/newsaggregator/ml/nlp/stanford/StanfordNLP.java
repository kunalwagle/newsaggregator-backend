package com.newsaggregator.ml.nlp.stanford;

import com.newsaggregator.base.OutletArticle;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;

/**
 * Created by kunalwagle on 09/04/2017.
 */
public class StanfordNLP {

    public static StanfordAnalysis performAnalysis(OutletArticle article) {
        StanfordCoreNLP pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,parse,natlog",
                        "ssplit.isOneSentence", "true",
                        "parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
                        "tokenize.language", "en"));

        Annotation document = new Annotation(article.getBody());

        pipeline.annotate(document);

        return new StanfordAnalysis(article.getSource(), document);

    }

}
