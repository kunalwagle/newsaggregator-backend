package com.newsaggregator.ml.summarisation.Abstractive;

import java.util.List;

/**
 * Created by kunalwagle on 10/04/2017.
 */
public class RSSubGraph {

    private List<RSNode> nodes;

    public RSSubGraph(List<RSNode> nodes) {
        this.nodes = nodes;
    }

    public List<RSNode> getNodes() {
        return nodes;
    }
}
