package app;

import com.fasterxml.jackson.databind.*;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSorter;
import graph.dagsp.DAGShortestPaths;
import graph.util.Metrics;
import java.io.File;
import java.util.*;

public class MainRunner {

    public static void main(String[] args) throws Exception {
        // 🔹 Пройти по всем 9 наборам
        for (int i = 1; i <= 9; i++) {
            String fileName = "data/dataset-" + i + ".json";
            System.out.println("\n===============================");
            System.out.println("Processing " + fileName);
            runPipeline(fileName);
        }
    }

    public static void runPipeline(String fileName) throws Exception {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> json = om.readValue(new File(fileName), Map.class);

        List<Integer> nodes = (List<Integer>) json.get("nodes");
        List<List<Integer>> edgesRaw = (List<List<Integer>>) json.get("edges");
        Map<String, Integer> durationsStr = (Map<String, Integer>) json.get("durations");

        Map<Integer, Integer> durations = new HashMap<>();
        for (Map.Entry<String, Integer> e : durationsStr.entrySet())
            durations.put(Integer.parseInt(e.getKey()), e.getValue());

        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int v : nodes) graph.put(v, new ArrayList<>());
        for (List<Integer> e : edgesRaw)
            graph.get(e.get(0)).add(e.get(1));

        System.out.println("Nodes: " + nodes.size() + ", Edges: " + edgesRaw.size());

        // SCC
        Metrics.reset();
        long t1 = System.nanoTime();
        TarjanSCC sccFinder = new TarjanSCC(graph);
        List<List<Integer>> comps = sccFinder.run();
        long t2 = System.nanoTime();

        System.out.println("Found " + comps.size() + " strongly connected components.");
        System.out.println("SCC time = " + (t2 - t1) / 1e6 + " ms");
        System.out.println("Metrics: " + Metrics.report());

        // Condensation DAG
        Map<Integer, List<Integer>> dag = buildCondensation(graph, comps);

        // Toposort
        List<Integer> topoOrder = TopologicalSorter.kahnToposort(dag);

        // Shortest/Longest Paths
        DAGShortestPaths dsp = new DAGShortestPaths(dag, buildDurationsForSCCs(comps, durations));
        int source = topoOrder.get(0);
        DAGShortestPaths.Result shortest = dsp.shortestFrom(source, topoOrder);
        DAGShortestPaths.PathResult longest = dsp.longestPath(topoOrder);

        System.out.println("Topo order: " + topoOrder);
        System.out.println("Longest path length = " + longest.length);
        System.out.println("Longest path (components): " + longest.path);
    }

    private static Map<Integer, List<Integer>> buildCondensation(Map<Integer, List<Integer>> graph, List<List<Integer>> comps) {
        Map<Integer, Integer> nodeToComp = new HashMap<>();
        for (int i = 0; i < comps.size(); i++)
            for (int v : comps.get(i))
                nodeToComp.put(v, i + 1);

        Map<Integer, List<Integer>> dag = new HashMap<>();
        for (int i = 1; i <= comps.size(); i++) dag.put(i, new ArrayList<>());

        for (var e : graph.entrySet()) {
            int fromComp = nodeToComp.get(e.getKey());
            for (int to : e.getValue()) {
                int toComp = nodeToComp.get(to);
                if (fromComp != toComp && !dag.get(fromComp).contains(toComp)) {
                    dag.get(fromComp).add(toComp);
                }
            }
        }
        return dag;
    }

    private static Map<Integer, Integer> buildDurationsForSCCs(List<List<Integer>> comps, Map<Integer, Integer> durations) {
        Map<Integer, Integer> compDur = new HashMap<>();
        for (int i = 0; i < comps.size(); i++) {
            int sum = 0;
            for (int v : comps.get(i))
                sum += durations.getOrDefault(v, 1);
            compDur.put(i + 1, sum);
        }
        return compDur;
    }
}
