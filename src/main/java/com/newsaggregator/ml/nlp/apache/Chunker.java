package com.newsaggregator.ml.nlp.apache;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kunalwagle on 05/04/2017.
 */
public class Chunker {

    private ChunkerME chunkerModel = null;

    public Chunker() {
        InputStream modelIn = null;

        try {
            // Loading tokenizer model
            modelIn = getClass().getResourceAsStream("/en-chunker.bin");
            final ChunkerModel chunker = new ChunkerModel(modelIn);
            modelIn.close();

            chunkerModel = new ChunkerME(chunker);

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

    public String[] chunk(String[] tokens, String[] pos) {
        if (chunkerModel == null) {
            return null;
        }
        return chunkerModel.chunk(tokens, pos);
    }

}
