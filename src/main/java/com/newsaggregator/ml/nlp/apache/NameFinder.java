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

    private List<NameFinderME> models = new ArrayList<>();

    NameFinder() {
        List<String> fileNames = Lists.newArrayList("location", "organization", "person");
        for (String fileName : fileNames) {
            NameFinderME nameFinderME = getModel("/en-ner-" + fileName + ".bin");
            if (nameFinderME != null) {
                models.add(nameFinderME);
            }
        }
    }

    List<String> findNames(String[] sentence) {
        List<String> names = new ArrayList<>();
        for (NameFinderME file : models) {
            names.addAll(getNames(sentence, file));
        }
        return names;
    }

    private NameFinderME getModel(String fileName) {
        InputStream is = getClass().getResourceAsStream(fileName);

        TokenNameFinderModel model;
        try {
            model = new TokenNameFinderModel(is);

            is.close();

            return new NameFinderME(model);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> getNames(String[] sentence, NameFinderME file) {

        try {
            Span nameSpans[] = file.find(sentence);

            return Arrays.asList(Span.spansToStrings(nameSpans, sentence));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


}
