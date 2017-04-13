package com.newsaggregator.ml.summarisation.Abstractive;

import java.util.ArrayList;

/**
 * Created by kunalwagle on 10/04/2017.
 */
public class RSNode {

    private ArrayList<RSWord> words;

    public RSNode(ArrayList<RSWord> words) {
        this.words = words;
    }

    public ArrayList<RSWord> getWords() {
        return words;
    }
}
