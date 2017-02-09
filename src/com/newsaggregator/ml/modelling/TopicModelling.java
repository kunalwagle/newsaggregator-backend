package com.newsaggregator.ml.modelling;


import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;
import com.newsaggregator.base.OutletArticle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TopicModelling {

public static void trainTopics(List<OutletArticle> articleList) throws IOException {

    String[] articleBodies = extractArticleText(articleList);

    ArrayList<Pipe> pipeList = new ArrayList<>();
    pipeList.add(new CharSequenceLowercase());
    pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
    pipeList.add(new TokenSequenceRemoveStopwords(new File("/Users/kunalwagle/Documents/Personal/Imperial/C4/401Project/Code/newsaggregator-backend/src/com/newsaggregator/ml/modelling/stoplists/en.txt"), "UTF-8", false, false, false));
    pipeList.add(new TokenSequence2FeatureSequence());

    InstanceList instances = new InstanceList(new SerialPipes(pipeList));
    instances.addThruPipe(new StringArrayIterator(articleBodies));

    int numTopics = 40;
    ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
    model.addInstances(instances);
    model.setNumThreads(2);
    model.setNumIterations(1000);
    model.estimate();

    Alphabet dataAlphabet = instances.getDataAlphabet();

    FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
    LabelSequence topics = model.getData().get(0).topicSequence;

    Formatter out = new Formatter(new StringBuilder(), Locale.US);
    for (int position = 0; position < tokens.getLength(); position++) {
        out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
    }
    System.out.println(out);
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

}
