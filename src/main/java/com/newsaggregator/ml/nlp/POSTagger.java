package com.newsaggregator.ml.nlp;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 09/03/2017.
 */
public class POSTagger {

    private POSTaggerME posTagger;

    public POSTagger() {
        posTagger = null;

        InputStream modelIn = null;
        try {
            // Loading tokenizer model
            modelIn = getClass().getResourceAsStream("/en-pos-maxent.bin");
            final POSModel posModel = new POSModel(modelIn);
            modelIn.close();

            posTagger = new POSTaggerME(posModel);

        } catch (final IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (final IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    public String[] tagWords(List<String> tokens) {

        String[] tokenArray = new String[tokens.size()];

        if (posTagger != null) {
            return posTagger.tag(tokens.toArray(tokenArray));
        }

        return null;

    }

    public List<String> filterNouns(List<String> tokens, String[] tags) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].startsWith("N")) {
                result.add(tokens.get(i));
            }
        }
        return result;
    }

}
