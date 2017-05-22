package com.newsaggregator.ml.labelling;

import com.newsaggregator.api.Wikipedia;
import com.newsaggregator.base.*;
import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
import com.newsaggregator.ml.tfidf.TfIdf;
import com.newsaggregator.ml.tfidf.TfIdfScores;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopicLabelling {

    public static List<String> generateTopicLabel(Topic model, OutletArticle outletArticle) {

        try {

            List<String> primaryLabels = new ArrayList<>();
            List<String> secondaryLabels = new ArrayList<>();
            List<TopicWord> topicWords = model.getTopWords();
            List<CandidateLabel> primaryCandidates = new ArrayList<>();
            List<String> nameCandidates = new ArrayList<>();

            ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
            List<String> names = extractSentenceTypes.nameFinder(outletArticle.getBody()).stream().distinct().collect(Collectors.toList());

            List<String> words = new ArrayList<>(names);

            for (String topicWord : names) {
                String article = Wikipedia.getNearMatchArticle(topicWord);
                if (article != null) {
                    nameCandidates.add(article);
                }
                //primaryLabels.addAll(extractTitles(Wikipedia.getArticles(topicWord.getWord())));
            }

            for (TopicWord topicWord : topicWords) {
                words.addAll(Wikipedia.titlesWithLimit(topicWord.getWord(), 15));
                //primaryLabels.addAll(extractTitles(Wikipedia.getArticles(topicWord.getWord())));
            }

            words = words.stream().distinct().collect(Collectors.toList());

            for (String word : words) {
                if (!names.contains(word)) {
                    WikipediaArticle article = Wikipedia.convertToArticle(word);
                    if (article != null) {
                        primaryCandidates.add(new CandidateLabel(article.getTitle(), article));
                    }
                }
            }

            //List<CandidateLabel> primaryCandidates = primaryLabels.parallelStream().map(label -> new CandidateLabel(label, Wikipedia.getNearMatchArticle(label))).collect(Collectors.toList());

//        for (String primaryLabel : primaryLabels) {
//            secondaryLabels.addAll(isolateNounChunks(primaryLabel));
//        }
//
//        secondaryLabels = secondaryLabels.parallelStream().filter(TopicLabelling::isWikipediaArticle).filter(primaryLabels::contains).collect(Collectors.toList());
//
//        List<CandidateLabel> secondaryCandidates = secondaryLabels.parallelStream().map(label -> new CandidateLabel(label, Wikipedia.getNearMatchArticle(label), Wikipedia.getOutlinksAndCategories(label))).collect(Collectors.toList());
//
//        secondaryCandidates = secondaryCandidates.stream().filter(candidate -> secondaryLabelViable(primaryCandidates, candidate)).collect(Collectors.toList());
//
//        primaryCandidates.addAll(secondaryCandidates);


            List<String> results = performCandidateRanking(primaryCandidates, topicWords, outletArticle).stream().distinct().collect(Collectors.toList());

            nameCandidates.addAll(results);

            return nameCandidates;
        } catch (Exception e) {
            Logger.getLogger(TopicLabelling.class).error("An error in labelling", e);
            return null;
        }
    }

    private static boolean secondaryLabelViable(List<CandidateLabel> primaryCandidates, CandidateLabel secondaryCandidate) {
        return racoScore(secondaryCandidate, primaryCandidates) > 0.1;
    }

    private static List<String> performCandidateRanking(List<CandidateLabel> labels, List<TopicWord> topicWords, OutletArticle outletArticle) {
        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
        TfIdf tfIdf = new TfIdf(labels.stream().map(label -> stripArticleBodies(label, extractSentenceTypes)).filter(s -> s.length() > 0).collect(Collectors.toList()));

        List<TfIdfScores> potentialLabels = new ArrayList<>();

        for (CandidateLabel label : labels) {
            String strippedBody = stripArticleBodies(label, extractSentenceTypes);
            double calc = topicWords.stream().mapToDouble(term -> tfIdf.performTfIdf(strippedBody, term.getWord())).sum();
            List<String> articleNouns = extractSentenceTypes.individualNouns(outletArticle.getBody());
            List<String> wikipediaArticleNouns = extractSentenceTypes.individualNouns(label.getArticleBody());
            double crossover = 0.0;
            for (String string : articleNouns) {
                if (wikipediaArticleNouns.contains(string)) {
                    crossover++;
                }
            }
            crossover /= (wikipediaArticleNouns.size() + articleNouns.size());
            calc *= crossover;
            potentialLabels.add(new TfIdfScores(label.getLabel(), calc));
        }

        return potentialLabels.stream().sorted(Comparator.comparing(TfIdfScores::getCalculation).reversed()).map(TfIdfScores::getLabel).distinct().limit(18).collect(Collectors.toList());

    }

    private static String stripArticleBodies(CandidateLabel label, ExtractSentenceTypes extractor) {
        try {
            return extractor.nounifyDocument(label.getArticleBody());
        } catch (Exception e) {
            Logger.getLogger(TopicLabelling.class).error("An error stripping article bodies", e);
            return "";
        }
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
            ////e.printStackTrace();
        }
        return 0;
    }

    private static boolean isWikipediaArticle(String secondaryLabel) {
        String article = Wikipedia.getNearMatchArticle(secondaryLabel);
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
