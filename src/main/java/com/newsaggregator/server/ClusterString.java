package com.newsaggregator.server;

import com.newsaggregator.ml.summarisation.Extractive.Node;

import java.util.List;

/**
 * Created by kunalwagle on 19/04/2017.
 */
public class ClusterString {

    private List<String> cluster;
    private List<Node> nodes;

    public ClusterString(List<String> cluster, List<Node> nodes) {
        this.cluster = cluster;
        this.nodes = nodes;
    }

    public ClusterString() {
    }

    public List<String> getCluster() {
        return cluster;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setCluster(List<String> cluster) {
        this.cluster = cluster;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
