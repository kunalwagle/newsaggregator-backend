package com.newsaggregator.base;

import com.newsaggregator.ml.tfidf.TfIdfScores;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by kunalwagle on 28/03/2017.
 */
public class ArticleVector implements ClusterItem {

    private OutletArticle article;
    private VectorScore vectorScore;

    public ArticleVector(OutletArticle article, VectorScore vectorScore) {
        this.article = article;
        this.vectorScore = vectorScore;
    }

    @Override
    public double getSimilarityScore(ClusterItem otherItem) {
        try {
            double result;
            VectorScore otherVectorScore = otherItem.getVector();
            result = findSimilarities(otherVectorScore);
            if (result > 0.0005) {
                return result / factorInTimeStamp(otherVectorScore);
            }
            return -1;
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("An error in the clusterer", e);
            return -1;
        }

    }

    private double factorInTimeStamp(VectorScore otherVectorScore) {
        double timestamp1 = vectorScore.getTimeStampScore() / (1000 * 60 * 60);
        double timestamp2 = otherVectorScore.getTimeStampScore() / (1000 * 60 * 60);
        return Math.abs(timestamp1 - timestamp2);
    }

    private double findSimilarities(VectorScore otherVectorScore) {
        List<TfIdfScores> firstNounScores = vectorScore.getNounScores();
        List<TfIdfScores> secondNounScores = otherVectorScore.getNounScores();
        double similarityRunningTotal = 0;
        double differenceRunningTotal = 0;
        double averageWords = (firstNounScores.size() + secondNounScores.size()) / 2;
        double totalSimilarWords = 0;
        for (TfIdfScores word : firstNounScores) {
            String noun = word.getLabel();
            if (secondNounScores.stream().anyMatch(tfIdf -> tfIdf.getLabel().equals(noun))) {
                TfIdfScores score = secondNounScores.stream().filter(tfIdf -> tfIdf.getLabel().equals(noun)).findFirst().get();
                double number = word.getCalculation() - score.getCalculation();
                similarityRunningTotal += Math.pow(number, 2);
                totalSimilarWords++;
            } else {
                differenceRunningTotal += word.getCalculation();
            }
        }
        for (TfIdfScores word : secondNounScores) {
            String noun = word.getLabel();
            if (secondNounScores.stream().noneMatch(tfIdf -> tfIdf.getLabel().equals(noun))) {
                differenceRunningTotal += word.getCalculation();
            }
        }
        double multiplicationFactor = totalSimilarWords / averageWords;
        similarityRunningTotal = Math.pow(similarityRunningTotal, 0.5) * multiplicationFactor;
        return similarityRunningTotal / differenceRunningTotal;
    }

    public OutletArticle getArticle() {
        return article;
    }

    @Override
    public VectorScore getVector() {
        return vectorScore;
    }

    @Override
    public String getText() {
        return article.getBody();
    }

    @Override
    public String getIdentifier() {
        return article.getArticleUrl();
    }

}
