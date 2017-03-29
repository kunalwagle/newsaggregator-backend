package com.newsaggregator.ml.summarisation.Extractive;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by kunalwagle on 29/03/2017.
 */
public class PageRank {

    public static List<String> applyPageRank(Graph graph, List<String> sentences) {
        int N = sentences.size();
        double[] timeT = new double[N];
        double[] T1 = new double[N];
        for (int i = 0; i < timeT.length; i++) {
            timeT[i] = 1.0 / N;
        }
        List<List<Node>> nodeConnections = graph.getNodeConnections();
        do {
            for (int i = 0; i < timeT.length; i++) {
                T1[i] = (0.15 / N) + 0.85 * (summationCalculation(nodeConnections, timeT, i));
            }
        } while (!pageRankFinished(timeT, T1));
        int[] sortedIndices = IntStream.range(0, T1.length)
                .boxed().sorted((i, j) -> T1[i] > T1[j] ? 1 : 0)
                .mapToInt(ele -> ele).toArray();
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < sortedIndices.length; i++) {
            result.add(sentences.get(sortedIndices[i]));
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

}
