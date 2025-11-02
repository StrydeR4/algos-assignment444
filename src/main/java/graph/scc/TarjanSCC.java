package graph.scc;

import graph.util.Metrics;
import java.util.*;


public class TarjanSCC {
    private final Map<Integer, List<Integer>> graph;
    private final Map<Integer, Integer> indexMap = new HashMap<>();
    private final Map<Integer, Integer> lowlink = new HashMap<>();
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Set<Integer> onStack = new HashSet<>();
    private final List<List<Integer>> components = new ArrayList<>();
    private int index = 0;


    public TarjanSCC(Map<Integer, List<Integer>> graph) {
        this.graph = graph;
    }


    public List<List<Integer>> run() {
        for (Integer v : graph.keySet()) {
            if (!indexMap.containsKey(v)) {
                strongConnect(v);
            }
        }
        return components;
    }


    private void strongConnect(int v) {
        indexMap.put(v, index);
        lowlink.put(v, index);
        index++;
        stack.push(v);
        onStack.add(v);
        Metrics.incDFSVisits();


        for (int w : graph.getOrDefault(v, Collections.emptyList())) {
            Metrics.incDFSEdges();
            if (!indexMap.containsKey(w)) {
                strongConnect(w);
                lowlink.put(v, Math.min(lowlink.get(v), lowlink.get(w)));
            } else if (onStack.contains(w)) {
                lowlink.put(v, Math.min(lowlink.get(v), indexMap.get(w)));
            }
        }


        if (lowlink.get(v).equals(indexMap.get(v))) {
            List<Integer> comp = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (w != v);
            components.add(comp);
        }
    }
}