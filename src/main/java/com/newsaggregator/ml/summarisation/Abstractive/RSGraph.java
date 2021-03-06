//package com.newsaggregator.ml.summarisation.Abstractive;
//
//import com.newsaggregator.ml.nlp.apache.ExtractSentenceTypes;
//import com.newsaggregator.ml.nlp.wordnet.Wordnet;
//import edu.mit.jwi.item.ISynsetID;
//import edu.mit.jwi.item.IWord;
//import edu.stanford.nlp.ie.util.RelationTriple;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * Created by kunalwagle on 13/04/2017.
// */
//public class RSGraph {
//
//    private List<RSNode> nodes;
//
//    public RSGraph(List<RSNode> nodes) {
//        this.nodes = nodes;
//    }
//
//    public void synonymisation() {
//        List<String> lemmas = generateAllLemmas();
//        for (RSNode node : nodes) {
//            for (RSWord word : node.getWords()) {
//                List<IWord> synsets = word.getWordSense().getSynset().getWords();
//                Wordnet wordnet = word.getWordnet();
//                double total = synsets.stream().mapToInt(wordnet::getFrequency).sum();
//                double currentHighest = 0.0;
//                IWord maxWord = synsets.get(0);
//                for (IWord w : synsets) {
//                    double frequency = wordnet.getFrequency(w);
//                    int occurrences = Collections.frequency(lemmas, w.getLemma());
//                    double calculation = (occurrences / lemmas.size()) + (1 - (frequency / total));
//                    if (calculation > currentHighest) {
//                        currentHighest = calculation;
//                        maxWord = w;
//                    }
//                }
//                word.setSynonym(maxWord.getLemma());
//            }
//        }
//    }
//
//    private List<String> generateAllLemmas() {
//        List<String> result = new ArrayList<>();
//        for (RSNode node : nodes) {
//            result.addAll(node.getWords().stream().map(RSWord::getLemma).collect(Collectors.toList()));
//        }
//        return result;
//    }
//
//    public void applyHeuristics() {
//        ExtractSentenceTypes extractSentenceTypes = new ExtractSentenceTypes();
//        Iterator<RSNode> rsNodeIterator = nodes.iterator();
//        while (rsNodeIterator.hasNext()) {
//            RSNode currentNode = rsNodeIterator.next();
//            List<RSNode> rsNodes = new ArrayList<>(nodes);
//            for (RSNode secondNode : rsNodes) {
//                if (currentNode != secondNode) {
//                    heuristics(currentNode, secondNode, extractSentenceTypes);
//                }
//            }
//        }
//    }
//
//    private void heuristics(RSNode currentNode, RSNode secondNode, ExtractSentenceTypes extractSentenceTypes) {
//        Collection<RelationTriple> currentNodeRelationTriples = currentNode.getRelationTriples();
//        Collection<RelationTriple> secondNodeRelationTriples = secondNode.getRelationTriples();
//        List<Syntax> currentNodeSyntax = currentNodeRelationTriples.stream().map(rt -> generateSyntax(extractSentenceTypes, rt, currentNode.getWords())).collect(Collectors.toList());
//        List<Syntax> secondNodeSyntax = secondNodeRelationTriples.stream().map(rt -> generateSyntax(extractSentenceTypes, rt, currentNode.getWords())).collect(Collectors.toList());
//        Iterator<Syntax> currentSyntaxIterator = currentNodeSyntax.iterator();
//        while (currentSyntaxIterator.hasNext()) {
//            Syntax currentSyntax = currentSyntaxIterator.next();
//            for (Syntax secondSyntax : secondNodeSyntax) {
//                if (allSimilar(currentSyntax, secondSyntax)) {
//                    currentNodeRelationTriples.removeIf(rt -> currentSyntax.equals(generateSyntax(extractSentenceTypes, rt, currentNode.getWords())));
//                    currentSyntaxIterator.remove();
//                } else if (onlyObjectDifferent(currentSyntax, secondSyntax)) {
//                    RelationTriple toMove = currentNodeRelationTriples.stream().filter(rt -> currentSyntax.equals(generateSyntax(extractSentenceTypes, rt, currentNode.getWords()))).findFirst().get();
//                    currentNodeRelationTriples.remove(toMove);
//                    secondNodeRelationTriples.add(toMove);
//                    addWordsToNode(secondNode, currentSyntax);
//                    currentSyntaxIterator.remove();
//                } else if (verbAndObjectDifferent(currentSyntax, secondSyntax)) {
//                    RelationTriple toMove = currentNodeRelationTriples.stream().filter(rt -> currentSyntax.equals(generateSyntax(extractSentenceTypes, rt, currentNode.getWords()))).findFirst().get();
//                    currentNodeRelationTriples.remove(toMove);
//                    secondNodeRelationTriples.add(toMove);
//                    currentSyntaxIterator.remove();
//                }
//            }
//        }
//
//
//    }
//
//    private void addWordsToNode(RSNode secondNode, Syntax currentSyntax) {
//        secondNode.addWords(currentSyntax.getAllWords());
//    }
//
//    private boolean allSimilar(Syntax first, Syntax second) {
//        if (similar(first.getSubject(), second.getSubject())) {
//            if (similar(first.getVerb(), second.getVerb())) {
//                if (similar(first.getObject(), second.getObject())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean onlyObjectDifferent(Syntax first, Syntax second) {
//        if (similar(first.getSubject(), second.getSubject())) {
//            if (similar(first.getVerb(), second.getVerb())) {
//                if (!similar(first.getObject(), second.getObject())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean verbAndObjectDifferent(Syntax first, Syntax second) {
//        if (similar(first.getSubject(), second.getSubject())) {
//            if (!similar(first.getVerb(), second.getVerb())) {
//                if (!similar(first.getObject(), second.getObject())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
////    private boolean fourthHeuristic(Syntax first, Syntax second) {
////
////        return false;
////    }
//
//    private boolean similar(List<RSWord> first, List<RSWord> second) {
//        for (RSWord s1 : first) {
//            List<Integer> s1words = s1.getWordSense().getSynset().getRelatedSynsets().stream().map(ISynsetID::getOffset).collect(Collectors.toList());
//            if (second.stream().anyMatch(word -> s1words.contains(word.getWordSense().getSynset().getID().getOffset()))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private Syntax generateSyntax(ExtractSentenceTypes extractSentenceTypes, RelationTriple relationTriple, List<RSWord> words) {
//        List<String> firstSubject = extractSentenceTypes.allWords(relationTriple.subjectLemmaGloss());
//        List<String> firstVerb = extractSentenceTypes.allWords(relationTriple.relationLemmaGloss());
//        List<String> firstObject = extractSentenceTypes.allWords(relationTriple.objectLemmaGloss());
//        List<RSWord> firstSubjectWords = words.stream().filter(word -> firstSubject.contains(word.getLemma())).collect(Collectors.toList());
//        List<RSWord> firstVerbWords = words.stream().filter(word -> firstVerb.contains(word.getLemma())).collect(Collectors.toList());
//        List<RSWord> firstObjectWords = words.stream().filter(word -> firstObject.contains(word.getLemma())).collect(Collectors.toList());
//        return new Syntax(firstSubjectWords, firstObjectWords, firstVerbWords);
//    }
//
//    public List<RSNode> getNodes() {
//        return nodes;
//    }
//
//    private class Syntax {
//
//        private List<RSWord> subject;
//        private List<RSWord> object;
//        private List<RSWord> verb;
//
//        public Syntax(List<RSWord> subject, List<RSWord> object, List<RSWord> verb) {
//            this.subject = subject;
//            this.object = object;
//            this.verb = verb;
//        }
//
//        public List<RSWord> getSubject() {
//            return subject;
//        }
//
//        public List<RSWord> getObject() {
//            return object;
//        }
//
//        public List<RSWord> getVerb() {
//            return verb;
//        }
//
//        public List<RSWord> getAllWords() {
//            List<RSWord> result = new ArrayList<>();
//            result.addAll(subject);
//            result.addAll(object);
//            result.addAll(verb);
//            return result;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            Syntax other = (Syntax) o;
//            List<String> firstList = subject.stream().map(RSWord::getLemma).collect(Collectors.toList());
//            firstList.addAll(object.stream().map(RSWord::getLemma).collect(Collectors.toList()));
//            firstList.addAll(verb.stream().map(RSWord::getLemma).collect(Collectors.toList()));
//            List<String> secondList = other.getSubject().stream().map(RSWord::getLemma).collect(Collectors.toList());
//            secondList.addAll(other.getObject().stream().map(RSWord::getLemma).collect(Collectors.toList()));
//            secondList.addAll(other.getVerb().stream().map(RSWord::getLemma).collect(Collectors.toList()));
//            return firstList.stream().allMatch(secondList::contains);
//        }
//
//        @Override
//        public int hashCode() {
//            int result = subject != null ? subject.hashCode() : 0;
//            result = 31 * result + (object != null ? object.hashCode() : 0);
//            result = 31 * result + (verb != null ? verb.hashCode() : 0);
//            return result;
//        }
//    }
//}
