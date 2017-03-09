package com.newsaggregator.ml.modelling;


import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.*;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.base.TopicLabel;
import com.newsaggregator.base.TopicWord;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TopicModelling {

    private ParallelTopicModel model;
    private Alphabet dataAlphabet;
    private final int numTopics = 300;
    private InstanceList instances = getInstances();

    public TopicModelling() {
    }

    public List<TopicLabel> trainTopics(List<OutletArticle> articleList) throws IOException {

        String[] articleBodies = extractArticleText(articleList);

        instances.addThruPipe(new StringArrayIterator(articleBodies));

        this.model = new ParallelTopicModel(numTopics, 1.0, 0.01);
        model.addInstances(instances);
        model.setNumThreads(2);
        model.setNumIterations(1500);
        model.estimate();

        dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        List<TopicLabel> topicList = generateTopics(model.getSortedWords(), numTopics, dataAlphabet);


        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        System.out.println(out);

        return topicList;
    }

    private static InstanceList getInstances() {
        ArrayList<Pipe> pipeList = new ArrayList<>();
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
        pipeList.add(new TokenSequenceRemoveStopwords(new File("/Users/kunalwagle/Documents/Personal/Imperial/C4/401Project/Code/newsaggregator-backend/src/main/java/com/newsaggregator/ml/modelling/stoplists/en.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        return new InstanceList(new SerialPipes(pipeList));
    }

    public Topic getModel(String document) {
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(document, null, "document", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 2500, 40, 50);
        List<TopicLabel> labels = new ArrayList<>(generateTopics(model.getSortedWords(), numTopics, dataAlphabet));
        for (int i = 0; i < testProbabilities.length; i++) {
            int finalI = i;
            TopicLabel label = labels.stream().filter(topicLabel -> topicLabel.getLabel().equals("Topic" + finalI)).findFirst().get();
            label.getTopic().getTopWords().forEach(topicWord -> topicWord.setDistribution(testProbabilities[finalI]));
        }
        List<TopicWord> allWords = getAllWords(labels);
        List<TopicWord> topWords = allWords.stream().filter(topicWord -> isInArticle(topicWord.getWord(), document)).sorted(Comparator.comparing(TopicWord::getDistribution).reversed()).limit(10).collect(Collectors.toList());
        for (TopicWord word : topWords) {
            System.out.println(word.getWord());
        }
        return new Topic(topWords);
    }

    private boolean isInArticle(String word, String document) {
        return document.contains(word);
    }

    private List<TopicWord> getAllWords(List<TopicLabel> labels) {
        List<TopicWord> words = new ArrayList<>();
        for (TopicLabel label : labels) {
            words.addAll(label.getTopic().getTopWords());
        }
        return words;
    }

    private static String[] extractArticleText(List<OutletArticle> articleList) {

        List<String> articleBodies = new ArrayList<>();

        for (OutletArticle article : articleList) {
            articleBodies.add(article.getBody());
        }

        String[] result = new String[articleBodies.size()];
        result = articleBodies.toArray(result);

        return result;
    }

    private List<TopicLabel> generateTopics(List<TreeSet<IDSorter>> sortedWords, int numTopics, Alphabet dataAlphabet) {

        List<TopicLabel> topics = new ArrayList<>();

        int numWords = model.wordsPerTopic;

        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = sortedWords.get(topic).iterator();
            int rank = 0;
            List<TopicWord> topicWords = new ArrayList<>();
            while (iterator.hasNext() && rank < 10) {
                IDSorter idCountPair = iterator.next();
                topicWords.add(new TopicWord((String) dataAlphabet.lookupObject(idCountPair.getID()), (float) idCountPair.getWeight() / numWords));
                rank++;
            }
            topics.add(new TopicLabel("Topic" + topic, new Topic(topicWords)));
        }

        return topics;
    }

}
