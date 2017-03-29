package com.newsaggregator.ml.summarisation.Extractive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class PageRank {

    public static List<String> applyPageRank(Graph graph, List<String> sentences) {
        int N = sentences.size();
        double[] timeT = new double[N];
        double[] T1 = new double[N];
        for (int i = 0; i < timeT.length; i++) {
            T1[i] = 1.0 / N;
        }
        List<List<Node>> nodeConnections = graph.getNodeConnections();
        do {
            timeT = T1;
            T1 = new double[N];
            for (int i = 0; i < timeT.length; i++) {
                T1[i] = (0.15 / N) + 0.85 * (summationCalculation(nodeConnections, timeT, i));
            }
        } while (!pageRankFinished(timeT, T1));
        Pair[] pairs = new Pair[T1.length];
        for (int i = 0; i < T1.length; i++) {
            pairs[i] = new Pair(i, T1[i]);
        }
        Arrays.sort(pairs);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < pairs.length; i++) {
            result.add(sentences.get(pairs[i].index));
        }
        return result;
    }

    private static double summationCalculation(List<List<Node>> nodes, double[] timeT, int i) {
        double total = 0;
        for (Node node : nodes.get(i)) {
            List<Node> connections = nodes.get(node.getIdentifier());
            total += timeT[node.getIdentifier()] / connections.size();
        }
        return total;
    }

    private static boolean pageRankFinished(double[] timeT, double[] T1) {
        double total = 0;
        for (int i = 0; i < timeT.length; i++) {
            total += Math.abs(T1[i] - timeT[i]);
        }
        return total < 0.005;
    }

    private static class Pair implements Comparable<Pair> {
        public final int index;
        public final double value;

        public Pair(int index, double value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Pair other) {
            //multiplied to -1 as the author need descending sort order
            return -1 * Double.valueOf(this.value).compareTo(other.value);
        }
    }

}
