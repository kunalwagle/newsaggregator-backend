package com.newsaggregator.ml.labelling;

import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.base.*;
import com.newsaggregator.ml.tfidf.TfIdf;
import com.newsaggregator.ml.tfidf.TfIdfScores;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopicLabelling {

    public static List<String> generateTopicLabel(Topic model) {

        List<String> primaryLabels = new ArrayList<>();
        List<String> secondaryLabels = new ArrayList<>();
        List<TopicWord> topicWords = model.getTopWords();

        for (TopicWord topicWord : topicWords) {
            primaryLabels.addAll(extractTitles(Wikipedia.getArticles(topicWord.getWord())));
        }

        List<CandidateLabel> primaryCandidates = primaryLabels.parallelStream().map(label -> new CandidateLabel(label, Wikipedia.getNearMatchArticle(label), Wikipedia.getOutlinksAndCategories(label))).collect(Collectors.toList());

        for (String primaryLabel : primaryLabels) {
            secondaryLabels.addAll(isolateNounChunks(primaryLabel));
        }

        secondaryLabels = secondaryLabels.parallelStream().filter(TopicLabelling::isWikipediaArticle).filter(primaryLabels::contains).collect(Collectors.toList());

        List<CandidateLabel> secondaryCandidates = secondaryLabels.parallelStream().map(label -> new CandidateLabel(label, Wikipedia.getNearMatchArticle(label), Wikipedia.getOutlinksAndCategories(label))).collect(Collectors.toList());

        secondaryCandidates = secondaryCandidates.stream().filter(candidate -> secondaryLabelViable(primaryCandidates, candidate)).collect(Collectors.toList());

        primaryCandidates.addAll(secondaryCandidates);


        return performCandidateRanking(primaryCandidates, topicWords);
    }

    private static boolean secondaryLabelViable(List<CandidateLabel> primaryCandidates, CandidateLabel secondaryCandidate) {
        return racoScore(secondaryCandidate, primaryCandidates) > 0.1;
    }

    private static List<String> performCandidateRanking(List<CandidateLabel> labels, List<TopicWord> topicWords) {
        //TODO Attempt with nounified candidate labels - perhaps nounify at the very beginning
        TfIdf tfIdf = new TfIdf(labels.stream().map(CandidateLabel::getArticleBody).collect(Collectors.toList()));

        List<TfIdfScores> potentialLabels = new ArrayList<>();

        for (CandidateLabel label : labels) {
            //TODO Attempt with nounified document
            double calc = topicWords.stream().mapToDouble(term -> tfIdf.performTfIdf(label.getArticleBody(), term.getWord())).sum();
            potentialLabels.add(new TfIdfScores(label.getLabel(), calc));
        }

        return potentialLabels.stream().sorted(Comparator.comparing(TfIdfScores::getCalculation).reversed()).map(TfIdfScores::getLabel).distinct().limit(10).collect(Collectors.toList());

    }

    private static double racoScore(CandidateLabel secondaryLabel, List<CandidateLabel> primaryLabels) {
        double runningTotal = 0;

        for (CandidateLabel primaryLabel : primaryLabels) {
            runningTotal += individualRacoScore(secondaryLabel, primaryLabel);
        }

        return runningTotal / primaryLabels.size();
    }

    private static double individualRacoScore(CandidateLabel secondaryLabel, CandidateLabel primaryLabel) {
        try {
            List<Outlink> primaryOutlinks = primaryLabel.getOutlinks();
            List<Outlink> secondaryOutlinks = secondaryLabel.getOutlinks();
            int categoryOverlap = 0;
            int primarySize = primaryOutlinks.stream().mapToInt(Outlink::categorySize).sum();
            int secondarySize = secondaryOutlinks.stream().mapToInt(Outlink::categorySize).sum();
            for (Outlink primaryOutlink : primaryOutlinks) {
                for (Outlink secondaryOutlink : secondaryOutlinks) {
                    ArrayList<String> intersection = new ArrayList<>(primaryOutlink.getCategories());
                    intersection.retainAll(secondaryOutlink.getCategories());
                    categoryOverlap += intersection.size();
                }
            }
            return (2 * categoryOverlap) / (primarySize + secondarySize);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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
