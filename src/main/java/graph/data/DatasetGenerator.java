package graph.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {
    private static final Random R = new Random(12345);

    public static void main(String[] args) throws IOException {
        new File("data").mkdirs();
        ObjectMapper om = new ObjectMapper();
        int[][] sizes = {{6,9},{10,20},{20,50}};
        int fileIdx = 1;

        for (int i = 0; i < sizes.length; i++) {
            for (int variant = 0; variant < 3; variant++) {
                int n = i == 0 ? 6 + variant : i == 1 ? 10 + variant * 3 : 20 + variant * 10;
                Map<String, Object> ds = generate(n, variant);
                String name = String.format("data/dataset-%d.json", fileIdx++);
                om.writerWithDefaultPrettyPrinter().writeValue(new File(name), ds);
                System.out.println("Wrote " + name + " n=" + n);
            }
        }
    }

    private static Map<String, Object> generate(int n, int variant) {
        List<int[]> edges = new ArrayList<>();
        Map<Integer, Integer> dur = new HashMap<>();
        for (int v = 1; v <= n; v++) dur.put(v, 1 + R.nextInt(10));
        double density = variant == 0 ? 0.15 : variant == 1 ? 0.3 : 0.6;
        for (int u = 1; u <= n; u++) {
            for (int v = 1; v <= n; v++) {
                if (u != v && R.nextDouble() < density) edges.add(new int[]{u, v});
            }
        }
        if (variant == 0 && n >= 3) {
            edges.add(new int[]{1, 2});
            edges.add(new int[]{2, 3});
            edges.add(new int[]{3, 1});
        }
        Map<String, Object> map = new HashMap<>();
        List<Integer> nodes = new ArrayList<>();
        for (int i = 1; i <= n; i++) nodes.add(i);
        map.put("nodes", nodes);
        map.put("edges", edges);
        map.put("durations", dur);
        return map;
    }
}

