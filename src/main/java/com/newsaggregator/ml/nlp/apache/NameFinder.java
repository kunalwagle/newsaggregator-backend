package com.newsaggregator.ml.nlp.apache;

import com.google.common.collect.Lists;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kunalwagle on 15/05/2017.
 */
class NameFinder {

    NameFinder() {

    }

    List<String> findNames(String[] sentence) {
        List<String> fileNames = Lists.newArrayList("date", "location", "money", "organization", "person", "time");
        List<String> names = new ArrayList<>();
        for (String fileName : fileNames) {
            names.addAll(getNames(sentence, "/en-ner-" + fileName + ".bin"));
        }
        return names;
    }

    private List<String> getNames(String[] sentence, String fileName) {
        InputStream is = getClass().getResourceAsStream(fileName);

        TokenNameFinderModel model = null;
        try {
            model = new TokenNameFinderModel(is);

            is.close();

            NameFinderME nameFinder = new NameFinderME(model);


            Span nameSpans[] = nameFinder.find(sentence);

            return Arrays.asList(Span.spansToStrings(nameSpans, sentence));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


}
