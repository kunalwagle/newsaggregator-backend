package com.newsaggregator.ml.nlp.apache;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kunalwagle on 15/05/2017.
 */
class NameFinder {

    NameFinder() {

    }

    List<String> findNames(String[] sentence) {
        InputStream is = getClass().getResourceAsStream("/en-ner-person.bin");

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

        return null;
    }
}
