package graph.scc;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class TarjanSCCTest {
    @Test
    public void testSimple() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(1, Arrays.asList(2));
        g.put(2, Arrays.asList(3));
        g.put(3, Arrays.asList(1));
        g.put(4, Arrays.asList(2, 5));
        g.put(5, Arrays.asList());
        TarjanSCC t = new TarjanSCC(g);
        List<List<Integer>> comps = t.run();
        boolean found = false;
        for (List<Integer> c : comps)
            if (c.containsAll(Arrays.asList(1, 2, 3))) found = true;
        assertTrue(found);
    }
}

