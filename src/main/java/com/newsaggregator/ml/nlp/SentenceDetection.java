package com.newsaggregator.ml.nlp;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kunalwagle on 09/03/2017.
 */
public class SentenceDetection {

    private SentenceDetector sentenceDetector;

    public SentenceDetection() {
        sentenceDetector = null;

        InputStream modelIn = null;
        try {
            modelIn = getClass().getResourceAsStream("/en-sent.bin");
            final SentenceModel sentenceModel = new SentenceModel(modelIn);
            modelIn.close();

            sentenceDetector = new SentenceDetectorME(sentenceModel);

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

    public String[] detectSentences(String content) {

        if (sentenceDetector != null) {
            return sentenceDetector.sentDetect(content);
        }
        return null;
    }

}
