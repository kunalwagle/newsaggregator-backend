package com.newsaggregator.ml.summarisation.Abstractive;

import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
import edu.stanford.nlp.ie.util.RelationTriple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 13/04/2017.
 */
public class RSGraph {

    private List<RSNode> nodes;

    public RSGraph(List<RSNode> nodes) {
        this.nodes = nodes;
    }

    public void applyHeuristics() {
        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
        Iterator<RSNode> rsNodeIterator = nodes.iterator();
        while (rsNodeIterator.hasNext()) {
            RSNode currentNode = rsNodeIterator.next();
            List<RSNode> rsNodes = new ArrayList<>(nodes);
            for (RSNode secondNode : rsNodes) {
                if (currentNode != secondNode) {
                    heuristics(currentNode, secondNode, extractSentenceTypes);
                }
            }
        }
    }

    private void heuristics(RSNode currentNode, RSNode secondNode, ExtractSentenceTypes extractSentenceTypes) {
//        Syntax currentSyntax = generateSyntax(currentNode);
//        Syntax secondSyntax = generateSyntax(secondNode);
        Collection<RelationTriple> currentNodeRelationTriples = currentNode.getRelationTriples();
        Collection<RelationTriple> secondNodeRelationTriples = secondNode.getRelationTriples();
        List<Syntax> currentNodeSyntax = currentNodeRelationTriples.stream().map(rt -> generateSyntax(extractSentenceTypes, rt, currentNode.getWords())).collect(Collectors.toList());
        List<Syntax> secondNodeSyntax = currentNodeRelationTriples.stream().map(rt -> generateSyntax(extractSentenceTypes, rt, currentNode.getWords())).collect(Collectors.toList());
        for (Syntax currentSyntax : currentNodeSyntax) {
            for (Syntax secondSyntax : secondNodeSyntax) {
                if (firstHeuristic(currentSyntax, secondSyntax)) {

                } else if (secondHeuristic(currentSyntax, secondSyntax)) {

                } else if (thirdHeuristic(currentSyntax, secondSyntax)) {

                } else if (fourthHeuristic(currentSyntax, secondSyntax)) {

                }
            }
        }


    }

    private boolean firstHeuristic(Syntax first, Syntax second) {

        return false;
    }

    private boolean secondHeuristic(Syntax first, Syntax second) {

        return false;
    }

    private boolean thirdHeuristic(Syntax first, Syntax second) {

        return false;
    }

    private boolean fourthHeuristic(Syntax first, Syntax second) {

        return false;
    }

    private Syntax generateSyntax(ExtractSentenceTypes extractSentenceTypes, RelationTriple relationTriple, List<RSWord> words) {
        List<String> firstSubject = extractSentenceTypes.allWords(relationTriple.subjectLemmaGloss());
        List<String> firstVerb = extractSentenceTypes.allWords(relationTriple.relationLemmaGloss());
        List<String> firstObject = extractSentenceTypes.allWords(relationTriple.objectLemmaGloss());
        List<RSWord> firstSubjectWords = words.stream().filter(word -> firstSubject.contains(word.getLemma())).collect(Collectors.toList());
        List<RSWord> firstVerbWords = words.stream().filter(word -> firstVerb.contains(word.getLemma())).collect(Collectors.toList());
        List<RSWord> firstObjectWords = words.stream().filter(word -> firstObject.contains(word.getLemma())).collect(Collectors.toList());
        return new Syntax(firstSubjectWords, firstObjectWords, firstVerbWords);
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
