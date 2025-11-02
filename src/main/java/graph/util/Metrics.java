package graph.util;

public class Metrics {
    private static long dfsVisits = 0;
    private static long dfsEdges = 0;
    private static long kahnPushes = 0;
    private static long kahnPops = 0;
    private static long relaxations = 0;

    public static void incDFSVisits() { dfsVisits++; }
    public static void incDFSEdges() { dfsEdges++; }
    public static void incKahnPushes() { kahnPushes++; }
    public static void incKahnPops() { kahnPops++; }
    public static void incRelaxations() { relaxations++; }

    public static void reset() {
        dfsVisits = dfsEdges = kahnPushes = kahnPops = relaxations = 0;
    }

    public static String report() {
        return String.format(
                "DFS visits=%d, DFS edges=%d, Kahn pushes=%d, Kahn pops=%d, relaxations=%d",
                dfsVisits, dfsEdges, kahnPushes, kahnPops, relaxations
        );
    }
}

