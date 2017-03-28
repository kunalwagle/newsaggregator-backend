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


}
