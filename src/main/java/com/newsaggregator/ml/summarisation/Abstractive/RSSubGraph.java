package com.newsaggregator.ml.summarisation.Abstractive;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 10/04/2017.
 */
public class RSSubGraph {

    private List<RSNode> nodes;

    public RSSubGraph(List<RSNode> nodes) {
        this.nodes = nodes;
        for (RSNode node : nodes) {
            node.calculationFactor(nodes.size());
        }
    }

    public List<RSNode> getNodes() {
        return nodes;
    }

    public RSNode filterNodes() {
        return nodes.stream().sorted(Comparator.comparing(RSNode::getCalculation).reversed()).limit(1).collect(Collectors.toList()).get(0);
    }
}
