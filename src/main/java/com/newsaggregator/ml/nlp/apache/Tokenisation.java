package com.newsaggregator.ml.nlp.apache;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 09/03/2017.
 */
public class Tokenisation {

    private Tokenizer tokeniser;

    public Tokenisation() {
        tokeniser = null;

        InputStream modelIn = null;
        try {
            modelIn = getClass().getResourceAsStream("/en-token.bin");
            final TokenizerModel tokenModel = new TokenizerModel(modelIn);
            modelIn.close();

            tokeniser = new TokenizerME(tokenModel);

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

    public List<String> findTokens(String[] sentences) {

        List<String> sentenceList = Arrays.asList(sentences);

        Tokenizer finalTokeniser = tokeniser;
        if (finalTokeniser != null) {
            return sentenceList.stream().map(finalTokeniser::tokenize).filter(Objects::nonNull).map(Arrays::asList).flatMap(List::stream).collect(Collectors.toList());
        }
        return null;
    }

}
