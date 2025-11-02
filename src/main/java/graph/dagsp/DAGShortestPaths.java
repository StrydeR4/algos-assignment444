package graph.dagsp;

import graph.util.Metrics;
import java.util.*;

public class DAGShortestPaths {
    private final Map<Integer, List<Integer>> dag;
    private final Map<Integer, Integer> duration;

    public DAGShortestPaths(Map<Integer, List<Integer>> dag, Map<Integer, Integer> duration) {
        this.dag = dag;
        this.duration = duration;
    }

    public static class Result {
        public final Map<Integer, Integer> dist;
        public final Map<Integer, Integer> pred;
        public Result(Map<Integer, Integer> dist, Map<Integer, Integer> pred) {
            this.dist = dist;
            this.pred = pred;
        }
    }

    public Result shortestFrom(int source, List<Integer> topoOrder) {
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> pred = new HashMap<>();
        for (int v : topoOrder) dist.put(v, Integer.MAX_VALUE / 4);
        dist.put(source, duration.getOrDefault(source, 0));

        for (int v : topoOrder) {
            if (dist.get(v) < Integer.MAX_VALUE / 4) {
                for (int w : dag.getOrDefault(v, Collections.emptyList())) {
                    Metrics.incRelaxations();
                    int cost = dist.get(v) + duration.getOrDefault(w, 0);
                    if (cost < dist.get(w)) {
                        dist.put(w, cost);
                        pred.put(w, v);
                    }
                }
            }
        }
        return new Result(dist, pred);
    }

    public PathResult longestPath(List<Integer> topoOrder) {
        Map<Integer, Integer> best = new HashMap<>();
        Map<Integer, Integer> pred = new HashMap<>();
        for (int v : topoOrder) best.put(v, Integer.MIN_VALUE / 4);
        for (int v : topoOrder) {
            if (best.get(v) == Integer.MIN_VALUE / 4)
                best.put(v, duration.getOrDefault(v, 0));
            for (int w : dag.getOrDefault(v, Collections.emptyList())) {
                Metrics.incRelaxations();
                int cand = best.get(v) + duration.getOrDefault(w, 0);
                if (cand > best.get(w)) {
                    best.put(w, cand);
                    pred.put(w, v);
                }
            }
        }
        int bestNode = -1, bestVal = Integer.MIN_VALUE / 4;
        for (var e : best.entrySet()) {
            if (e.getValue() > bestVal) {
                bestVal = e.getValue();
                bestNode = e.getKey();
            }
        }
        List<Integer> path = new ArrayList<>();
        if (bestNode != -1) {
            int cur = bestNode;
            while (pred.containsKey(cur)) {
                path.add(cur);
                cur = pred.get(cur);
            }
            path.add(cur);
            Collections.reverse(path);
        }
        return new PathResult(bestVal, path);
    }

    public static class PathResult {
        public final int length;
        public final List<Integer> path;
        public PathResult(int length, List<Integer> path) {
            this.length = length;
            this.path = path;
        }
    }
}
