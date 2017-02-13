package com.newsaggregator.ml.modelling;


import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.*;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.base.TopicWord;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class TopicModelling {

    public static List<Topic> trainTopics(List<OutletArticle> articleList) throws IOException {

        String[] articleBodies = extractArticleText(articleList);

        ArrayList<Pipe> pipeList = new ArrayList<>();
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
        pipeList.add(new TokenSequenceRemoveStopwords(new File("/Users/kunalwagle/Documents/Personal/Imperial/C4/401Project/Code/newsaggregator-backend/src/main/java/com/newsaggregator/ml/modelling/stoplists/en.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));
        instances.addThruPipe(new StringArrayIterator(articleBodies));

        int numTopics = 100;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
        model.addInstances(instances);
        model.setNumThreads(2);
        model.setNumIterations(1000);
        model.estimate();

        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        List<Topic> topicList = generateTopics(model.getSortedWords(), numTopics, dataAlphabet);


        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        System.out.println(out);

        return topicList;
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

    private static List<Topic> generateTopics(List<TreeSet<IDSorter>> sortedWords, int numTopics, Alphabet dataAlphabet) {

        List<Topic> topics = new ArrayList<>();

        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = sortedWords.get(topic).iterator();
            int rank = 0;
            List<TopicWord> topicWords = new ArrayList<>();
            while (iterator.hasNext() && rank < 10) {
                IDSorter idCountPair = iterator.next();
                topicWords.add(new TopicWord((String) dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight()));
                rank++;
            }
            topics.add(new Topic(topicWords));
        }

        return topics;
    }

}
