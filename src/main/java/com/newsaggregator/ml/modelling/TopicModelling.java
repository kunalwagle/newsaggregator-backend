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
import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TopicModelling {

    private ParallelTopicModel model;
    private Alphabet dataAlphabet;
    private final int numTopics = 100;
    private InstanceList instances = getInstances();
    private ExtractSentenceTypes nounifier;
    private Logger logger = Logger.getLogger(getClass());

    public TopicModelling() throws Exception {
        nounifier = new ExtractSentenceTypes();
    }

    public List<TopicLabel> trainTopics(List<OutletArticle> articleList) throws Exception {

        logger.info("Starting to train topics");

        String[] articleBodies = extractArticleText(articleList);

        logger.info("Extracted article text");

        instances.addThruPipe(new StringArrayIterator(articleBodies));

        this.model = new ParallelTopicModel(numTopics, 1.0, 0.01);
        model.addInstances(instances);
        model.setNumThreads(2);
        model.setNumIterations(1000);
        model.estimate();

        logger.info("Performed the estimate");

        dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        List<TopicLabel> topicList = generateTopics(model.getSortedWords(), numTopics, dataAlphabet);


        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        logger.info(out);

        return topicList;
    }

    private InstanceList getInstances() throws Exception {
        ArrayList<Pipe> pipeList = new ArrayList<>();
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
        //pipeList.add(new TokenSequenceRemoveStopwords(new File("en.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        return new InstanceList(new SerialPipes(pipeList));
    }

    public Topic getModel(OutletArticle article) {

        String document = article.getBody();

        String title = nounifier.nounifyDocument(article.getTitle());

        String nounifiedDocument = nounifier.nounifyDocument(document);

        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(nounifiedDocument, null, "document", null));

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
        List<TopicWord> titleWords = new ArrayList<>();

        try {
            titleWords = Arrays.stream(title.split(" ")).map(string -> new TopicWord(string, topWords.get(0).getDistribution() * 2)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Got an exception in topic modelling, but can continue", e);
        }

        titleWords.addAll(topWords);
        List<TopicWord> finalWords = titleWords.stream().limit(10).collect(Collectors.toList());

//        for (TopicWord word : finalWords) {
//            logger.info(word.getWord());
//        }
        return new Topic(finalWords);
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

    private String[] extractArticleText(List<OutletArticle> articleList) {

        List<String> articleBodies = new ArrayList<>();

        int i = 0;

        for (OutletArticle article : articleList) {
            logger.info(i + " out of " + articleList.size());
            i++;
            articleBodies.add(nounifier.nounifyDocument(article.getBody()));
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
