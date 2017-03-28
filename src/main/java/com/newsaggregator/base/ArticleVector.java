package com.newsaggregator.base;

import com.newsaggregator.ml.tfidf.TfIdfScores;

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
        double result;
        VectorScore otherVectorScore = otherItem.getVector();
        result = findSimilarities(otherVectorScore);
        result += factorInTimeStamp(otherVectorScore);
        return result;
    }

    private double factorInTimeStamp(VectorScore otherVectorScore) {
        return 0;
    }

    private double findSimilarities(VectorScore otherVectorScore) {
        List<TfIdfScores> firstNounScores = vectorScore.getNounScores();
        List<TfIdfScores> secondNounScores = otherVectorScore.getNounScores();
        double similarityRunningTotal = 0;
        double differenceRunningTotal = 0;
        double total = vectorScore.getTotalTfIdfScore() + otherVectorScore.getTotalTfIdfScore();
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
        //differenceRunningTotal = differenceRunningTotal / total;
        double multiplicationFactor = totalSimilarWords / averageWords;
        similarityRunningTotal = Math.pow(similarityRunningTotal, 0.5) * multiplicationFactor;
        System.out.println("Total: " + similarityRunningTotal / differenceRunningTotal);
        return similarityRunningTotal / differenceRunningTotal;
    }

    @Override
    public VectorScore getVector() {
        return vectorScore;
    }

}
