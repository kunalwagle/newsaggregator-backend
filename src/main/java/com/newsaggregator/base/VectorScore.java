package com.newsaggregator.base;

import com.newsaggregator.ml.tfidf.TfIdfScores;

import java.util.List;

/**
 * Created by kunalwagle on 28/03/2017.
 */
public class VectorScore {

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
