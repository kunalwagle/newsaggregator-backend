package com.newsaggregator.ml.labelling;

import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.base.Topic;
import com.newsaggregator.base.TopicLabel;
import com.newsaggregator.base.TopicWord;
import com.newsaggregator.base.WikipediaArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 08/02/2017.
 */
public class TopicLabelling {

    public static TopicLabel generateTopicLabel(Topic model) {

        List<String> primaryLabels = new ArrayList<>();
        List<String> secondaryLabels = new ArrayList<>();
        List<TopicWord> topicWords = model.getTopWords();

        for (TopicWord topicWord : topicWords) {
            primaryLabels.addAll(extractTitles(Wikipedia.getArticles(topicWord.getWord())));
        }

        for (String primaryLabel : primaryLabels) {
            secondaryLabels.addAll(isolateNounChunks(primaryLabel));
        }

        for (String secondaryLabel : secondaryLabels) {
            if (!isWikipediaArticle(secondaryLabel) || racoScore(secondaryLabel, primaryLabels) < 0.1) {
                secondaryLabels.remove(secondaryLabel);
            }
        }

        primaryLabels.addAll(secondaryLabels);

        for (int i = 0; i < 5; i++) {
            primaryLabels.add(topicWords.get(i).getWord());
        }

        String label = performCandidateRanking(primaryLabels);

        return new TopicLabel(label, model);
    }

    private static String performCandidateRanking(List<String> primaryLabels) {
        return null;
    }

    private static double racoScore(String secondaryLabel, List<String> primaryLabels) {
        return 0;
    }

    private static boolean isWikipediaArticle(String secondaryLabel) {
        return false;
    }

    private static List<String> isolateNounChunks(String primaryLabel) {

        return null;
    }

    private static List<String> extractTitles(ArrayList<WikipediaArticle> articles) {

        return null;
    }

}
