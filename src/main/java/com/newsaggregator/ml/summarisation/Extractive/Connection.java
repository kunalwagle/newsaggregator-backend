package com.newsaggregator.ml.summarisation.Extractive;

import org.apache.log4j.Logger;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class Connection {

    private Node firstNode;
    private Node secondNode;
    private double distance;

    public Connection(Node firstNode, Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.distance = -1;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean matches(Node node1, Node node2) {
        try {
            int fn = firstNode.getIdentifier();
            int sn = secondNode.getIdentifier();
            int n1 = node1.getIdentifier();
            int n2 = node2.getIdentifier();
            return (fn == n1 || fn == n2) && (sn == n1 || sn == n2);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("Error in connection matches", e);
            return false;
        }
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public double getDistance() {
        return distance;
    }
}
