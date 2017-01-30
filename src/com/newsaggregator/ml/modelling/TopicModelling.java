package com.newsaggregator.ml.modelling;


import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;
import com.newsaggregator.base.Article;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TopicModelling {

public static void trainTopics(List<Article> articleList) throws IOException {

    String[] articleBodies = extractArticleText(articleList);

    ArrayList<Pipe> pipeList = new ArrayList<>();
    pipeList.add(new CharSequenceLowercase());
    pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
    pipeList.add(new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false));
    pipeList.add(new TokenSequence2FeatureSequence());

    InstanceList instances = new InstanceList(new SerialPipes(pipeList));
    instances.addThruPipe(new StringArrayIterator(articleBodies));

    int numTopics = 100;
    ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
    model.addInstances(instances);
    model.setNumThreads(2);
    model.setNumIterations(50);
    model.estimate();

}

    private static String[] extractArticleText(List<Article> articleList) {

        List<String> articleBodies = new ArrayList<>();

        for (Article article : articleList) {
            articleBodies.add(article.getBody());
        }

        String[] result = new String[articleBodies.size()];
        result = articleBodies.toArray(result);

        return result;
    }

}
