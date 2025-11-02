package graph.topo;

import graph.util.Metrics;
import java.util.*;


public class TopologicalSorter {
    public static List<Integer> kahnToposort(Map<Integer, List<Integer>> graph) {
        Map<Integer, Integer> indeg = new HashMap<>();
        for (Integer v : graph.keySet()) indeg.put(v, 0);
        for (Integer v : graph.keySet()) {
            for (Integer w : graph.getOrDefault(v, Collections.emptyList())) {
                indeg.put(w, indeg.getOrDefault(w, 0) + 1);
            }
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (Map.Entry<Integer, Integer> e : indeg.entrySet()) {
            if (e.getValue() == 0) {
                q.add(e.getKey());
                Metrics.incKahnPushes();
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.remove();
            Metrics.incKahnPops();
            order.add(v);
            for (int w : graph.getOrDefault(v, Collections.emptyList())) {
                indeg.put(w, indeg.get(w) - 1);
                if (indeg.get(w) == 0) {
                    q.add(w);
                    Metrics.incKahnPushes();
                }
            }
        }
        return order;
    }
}
