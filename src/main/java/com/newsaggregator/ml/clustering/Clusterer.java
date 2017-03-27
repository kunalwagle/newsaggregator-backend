package com.newsaggregator.ml.clustering;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.nlp.ExtractSentenceTypes;
import com.newsaggregator.ml.tfidf.TfIdf;
import com.newsaggregator.ml.tfidf.TfIdfScores;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 27/03/2017.
 */
public class Clusterer {

    private HashMap<OutletArticle, VectorScore> articleVectorScoreHashMap = new HashMap<>();
    private ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
    private TfIdf tfIdf;

    public Clusterer(List<OutletArticle> articles) {
        //TODO: Create Corpus - potentially with nouns only
        tfIdf = new TfIdf(articles.stream().map(OutletArticle::getBody).collect(Collectors.toList()));
    }

    private void calculateVectorScore(OutletArticle article) {
        List<String> nouns = extractSentenceTypes.individualNouns(article.getBody());
        List<TfIdfScores> nounScores = new ArrayList<>();
        for (String noun : nouns) {
            nounScores.add(new TfIdfScores(noun, tfIdf.performTfIdf(article.getBody(), noun)));
        }
        double timeStampScore = generateTimeStampScore(article.getLastPublished());
        VectorScore vectorScore = new VectorScore(nounScores, timeStampScore);
        articleVectorScoreHashMap.put(article, vectorScore);
    }

    private double generateTimeStampScore(String lastPublished) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        try {
            date = dateFormat.parse(lastPublished);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    private class VectorScore {

        private List<TfIdfScores> nounScores;
        private double timeStampScore;

        public VectorScore(List<TfIdfScores> nounScores, double timeStampScore) {
            this.timeStampScore = timeStampScore;
            this.nounScores = nounScores;
        }

        public List<TfIdfScores> getNounScores() {
            return nounScores;
        }

        public double getTimeStampScore() {
            return timeStampScore;
        }
    }


}
