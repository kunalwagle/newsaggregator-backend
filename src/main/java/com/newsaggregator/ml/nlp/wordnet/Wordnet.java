package com.newsaggregator.ml.nlp.wordnet;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

import java.io.IOException;
import java.net.URL;

/**
 * Created by kunalwagle on 13/04/2017.
 */
public class Wordnet {

    private IDictionary dict;

    public Wordnet() throws WordnetFailedToOpenException, IOException {
        URL url = getClass().getResource("/dict");
        if (url == null) {
            throw new WordnetFailedToOpenException();
        }
        dict = new Dictionary(url);
        dict.open();
    }

    public IIndexWord getWord(String word, POS syntax) {
        return dict.getIndexWord(word, syntax);
    }

    public IWord getWord(IWordID iWord) {
        return dict.getWord(iWord);
    }

    public int getFrequency(IWord word) {
        return dict.getSenseEntry(word.getSenseKey()).getTagCount();
    }

}
