package com.newsaggregator.ml.labelling;

import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.base.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TopicLabelling {

    public static TopicLabel generateTopicLabel(Topic model) {

        List<String> primaryLabels = new ArrayList<>();
        List<String> secondaryLabels = new ArrayList<>();
        List<TopicWord> topicWords = model.getTopWords();

        for (TopicWord topicWord : topicWords) {
            primaryLabels.addAll(extractTitles(Wikipedia.getArticles(topicWord.getWord())));
        }

        List<CandidateLabel> primaryCandidates = primaryLabels.stream().map(label -> new CandidateLabel(label, Wikipedia.getOutlinksAndCategories(label))).collect(Collectors.toList());

        for (String primaryLabel : primaryLabels) {
            secondaryLabels.addAll(isolateNounChunks(primaryLabel));
        }

        secondaryLabels = secondaryLabels.stream().filter(secondaryLabel -> isWikipediaArticle(secondaryLabel)).collect(Collectors.toList());

        List<CandidateLabel> secondaryCandidates = secondaryLabels.stream().map(label -> new CandidateLabel(label, Wikipedia.getOutlinksAndCategories(label))).filter(candidate -> secondaryLabelViable(primaryCandidates, candidate)).collect(Collectors.toList());

        primaryCandidates.addAll(secondaryCandidates);

        for (int i = 0; i < 5; i++) {
            primaryLabels.add(topicWords.get(i).getWord());
        }

        String label = performCandidateRanking(primaryLabels);

        return new TopicLabel(label, model);
    }

    private static boolean secondaryLabelViable(List<CandidateLabel> primaryCandidates, CandidateLabel secondaryCandidate) {
        return racoScore(secondaryCandidate, primaryCandidates) > 0.1;
    }

    private static String performCandidateRanking(List<String> primaryLabels) {
        return null;
    }

    private static double racoScore(CandidateLabel secondaryLabel, List<CandidateLabel> primaryLabels) {
        double runningTotal = 0;

        for (CandidateLabel primaryLabel : primaryLabels) {
            runningTotal += individualRacoScore(secondaryLabel, primaryLabel);
        }

        return runningTotal / primaryLabels.size();
    }

    private static double individualRacoScore(CandidateLabel secondaryLabel, CandidateLabel primaryLabel) {
        return 0;
    }

    private static boolean isWikipediaArticle(String secondaryLabel) {
        WikipediaArticle article = Wikipedia.getNearMatchArticle(secondaryLabel);
        return article != null;
    }

    private static List<String> isolateNounChunks(String primaryLabel) {
        String[] result = primaryLabel.split("\\s");
        List<String> nounChunks = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            int j = i + 1;
            String currentString = result[i];
            while (j < result.length) {
                currentString = currentString + " " + result[j];
                nounChunks.add(currentString);
                j++;
            }
        }
        return nounChunks;
    }

    private static List<String> extractTitles(ArrayList<WikipediaArticle> articles) {
        return articles.stream().map(WikipediaArticle::getTitle).collect(Collectors.toList());
    }

}
