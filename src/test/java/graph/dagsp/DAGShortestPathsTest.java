package graph.dagsp;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class DAGShortestPathsTest {
    @Test
    public void testShortestAndLongest() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(1, Arrays.asList(2, 3));
        dag.put(2, Arrays.asList(4));
        dag.put(3, Arrays.asList(4));
        dag.put(4, Arrays.asList());
        Map<Integer, Integer> dur = new HashMap<>();
        dur.put(1, 1); dur.put(2, 2); dur.put(3, 3); dur.put(4, 4);
        List<Integer> topo = Arrays.asList(1, 2, 3, 4);
        DAGShortestPaths dsp = new DAGShortestPaths(dag, dur);
        DAGShortestPaths.Result res = dsp.shortestFrom(1, topo);
        assertEquals((Integer)(1 + 2 + 4), res.dist.get(4));
        DAGShortestPaths.PathResult pr = dsp.longestPath(topo);
        assertEquals(1 + 3 + 4, pr.length);
    }
}

