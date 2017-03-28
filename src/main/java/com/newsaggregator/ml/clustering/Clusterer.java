package com.newsaggregator.ml.clustering;

import com.newsaggregator.base.ArticleVector;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.VectorScore;
import com.newsaggregator.ml.nlp.ExtractSentenceTypes;
import com.newsaggregator.ml.tfidf.TfIdf;
import com.newsaggregator.ml.tfidf.TfIdfScores;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 27/03/2017.
 */
public class Clusterer {

    private List<Cluster<ArticleVector>> clusters = new ArrayList<>();
    private ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
    private TfIdf tfIdf;

    public Clusterer(List<OutletArticle> articles) {
        tfIdf = new TfIdf(articles.stream().map(OutletArticle::getBody).collect(Collectors.toList()));
        articles.forEach(this::createNewClusterFromArticle);
    }

    public List<Cluster<ArticleVector>> cluster() {
        boolean clusterChanged;
        do {
            double[][] similarityMatrix = generateSimilarityMatrix();
            ScoreAddress closeClusterAddresses = findHighestScoreAddress(similarityMatrix);
            clusterChanged = combineIfPossible(closeClusterAddresses);
        }
        while (clusterChanged);

        return clusters;
    }

    private boolean combineIfPossible(ScoreAddress score) {
        if (score.getScore() > 0.001) {
            Cluster firstCluster = clusters.get(score.getI());
            Cluster secondCluster = clusters.get(score.getJ());
            firstCluster.combine(secondCluster);
            clusters.remove(secondCluster);
            return true;
        }
        return false;
    }

    private ScoreAddress findHighestScoreAddress(double[][] similarityMatrix) {
        int iMax = -1;
        int jMax = -1;
        double scoreMax = -1;
        for (int i = 0; i < similarityMatrix.length; i++) {
            for (int j = 0; j < similarityMatrix.length; j++) {
                if (similarityMatrix[i][j] > scoreMax) {
                    scoreMax = similarityMatrix[i][j];
                    iMax = i;
                    jMax = j;
                }
            }
        }
        return new ScoreAddress(iMax, jMax, scoreMax);
    }

    private double[][] generateSimilarityMatrix() {
        double[][] similarityMatrix = new double[clusters.size()][clusters.size()];
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = 0; j < clusters.size(); j++) {
                if (i == j) {
                    similarityMatrix[i][j] = 0;
                } else {
                    if (similarityMatrix[j][i] != 0) {
                        similarityMatrix[i][j] = similarityMatrix[j][i];
                    } else {
                        similarityMatrix[i][j] = findSimilarity(i, j);
                    }
                }
            }
        }
        return similarityMatrix;
    }

    private double findSimilarity(int i, int j) {
        Cluster firstCluster = clusters.get(i);
        Cluster secondCluster = clusters.get(j);
        return firstCluster.getSimilarity(secondCluster);
    }

    private void createNewClusterFromArticle(OutletArticle article) {
        List<String> nouns = extractSentenceTypes.individualNouns(article.getBody());
        List<TfIdfScores> nounScores = new ArrayList<>();
        for (String noun : nouns) {
            nounScores.add(new TfIdfScores(noun, tfIdf.performTfIdf(article.getBody(), noun)));
        }
        double timeStampScore = generateTimeStampScore(article.getLastPublished());
        VectorScore vectorScore = new VectorScore(nounScores, timeStampScore);
        Cluster<ArticleVector> cluster = new Cluster<>(new ArticleVector(article, vectorScore));
        clusters.add(cluster);
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


    private class ScoreAddress {

        private int i;
        private int j;
        private double score;

        ScoreAddress(int i, int j, double score) {
            this.i = i;
            this.j = j;
            this.score = score;
        }

        int getI() {
            return i;
        }

        int getJ() {
            return j;
        }

        double getScore() {
            return score;
        }
    }

}
