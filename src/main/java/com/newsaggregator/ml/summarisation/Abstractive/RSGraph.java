package com.newsaggregator.ml.summarisation.Abstractive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kunalwagle on 13/04/2017.
 */
public class RSGraph {

    private List<RSNode> nodes;

    public RSGraph(List<RSNode> nodes) {
        this.nodes = nodes;
    }

    public void applyHeuristics() {
        Iterator<RSNode> rsNodeIterator = nodes.iterator();
        while (rsNodeIterator.hasNext()) {
            RSNode currentNode = rsNodeIterator.next();
            List<RSNode> rsNodes = new ArrayList<>(nodes);
            for (RSNode secondNode : rsNodes) {
                if (currentNode != secondNode) {
                    heuristics(currentNode, secondNode);
                }
            }
        }
    }

    private void heuristics(RSNode currentNode, RSNode secondNode) {
        Syntax currentSyntax = generateSyntax(currentNode);
        Syntax secondSyntax = generateSyntax(secondNode);
        if (firstHeuristic(currentSyntax, secondSyntax)) {

        } else if (secondHeuristic(currentSyntax, secondSyntax)) {

        } else if (thirdHeuristic(currentSyntax, secondSyntax)) {

        } else if (fourthHeuristic(currentSyntax, secondSyntax)) {

        }
    }

    private boolean fourthHeuristic(Syntax currentSyntax, Syntax secondSyntax) {

        return false;
    }

    private boolean thirdHeuristic(Syntax currentSyntax, Syntax secondSyntax) {
        return false;
    }

    private boolean secondHeuristic(Syntax currentSyntax, Syntax secondSyntax) {
        return false;
    }

    private boolean firstHeuristic(Syntax currentSyntax, Syntax secondSyntax) {

        return false;
    }

    private Syntax generateSyntax(RSNode node) {
        List<RSWord> subject = node.getNounSubject();
        List<RSWord> object = node.getNounObject();
        List<RSWord> verb = node.getVerb();
        return new Syntax(subject, object, verb);
    }

    private class Syntax {

        private List<RSWord> subject;
        private List<RSWord> object;
        private List<RSWord> verb;

        public Syntax(List<RSWord> subject, List<RSWord> object, List<RSWord> verb) {
            this.subject = subject;
            this.object = object;
            this.verb = verb;
        }

        public List<RSWord> getSubject() {
            return subject;
        }

        public List<RSWord> getObject() {
            return object;
        }

        public List<RSWord> getVerb() {
            return verb;
        }
    }
}
