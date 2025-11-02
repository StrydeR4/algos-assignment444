# Assignment 4 
Name: Ruslan Dussenbayev
Group: SE-2434
----

## Overview
This project combines **two main graph algorithm topics**:
- **Strongly Connected Components (SCC)** - Tarjan’s algorithm  
- **Topological Ordering and Shortest/Longest Paths in DAGs**

Applied on randomly generated task dependency graphs (Smart City / Smart Campus scheduling scenarios).

---

## Dataset Summary

| Dataset | Nodes | Edges | Structure | Cyclic? |
|----------|--------|--------|------------|----------|
| 1 | 6 | 9 | Small | ✅ |
| 2 | 7 | 11 | Small | ✅ |
| 3 | 8 | 34 | Small (dense) | ✅ (fully connected) |
| 4 | 10 | 11 | Medium | ✅ |
| 5 | 13 | 47 | Medium (dense) | ✅ |
| 6 | 16 | 148 | Large (dense) | ✅ |
| 7 | 20 | 55 | Large | ✅ |
| 8 | 30 | 260 | Large (very dense) | ✅ |
| 9 | 15 | 22 | Large | ✅ |

All datasets contain node durations (1–10).  
Some are fully cyclic (1 SCC), others produce several SCCs and a condensation DAG.

---

## Experimental Results

| Dataset | Nodes | Edges | SCCs | Topo Order Size | Longest Path | Time (ms) | DFS Visits | DFS Edges |
|----------|--------|--------|-------|------------------|---------------|-------------|-------------|-------------|
| 1 | 6 | 9 | 3 | 3 | 25 | 0.67 | 6 | 9 |
| 2 | 7 | 11 | 3 | 3 | 22 | 0.05 | 7 | 11 |
| 3 | 8 | 34 | 1 | 1 | 47 | 0.03 | 8 | 34 |
| 4 | 10 | 11 | 8 | 8 | 32 | 0.05 | 10 | 11 |
| 5 | 13 | 47 | 1 | 1 | 90 | 0.06 | 13 | 47 |
| 6 | 16 | 148 | 1 | 1 | 92 | 0.12 | 16 | 148 |
| 7 | 20 | 55 | 3 | 3 | 134 | 0.07 | 20 | 55 |
| 8 | 30 | 260 | 1 | 1 | 197 | 0.17 | 30 | 260 |
| 9 | 15 | 22 | 1 | 1 | 80 | 0.05 | 15 | 22 |

---

## Analysis

### SCC & Topological Ordering
- Sparse graphs with mixed edges (datasets 1, 2, 4, 7) produced **multiple SCCs** (3–8).  
- Dense graphs (3, 5, 6, 8, 9) formed **a single giant SCC**, meaning every vertex is reachable from every other vertex.
- Tarjan’s algorithm scaled linearly with graph size - **time < 0.2 ms even for 30 nodes / 260 edges**.

### Shortest & Longest Paths (Critical Path)
- Since fully connected graphs become a single SCC, condensation DAG often had only one node.
- Longest path length correlates with the sum of node durations within large SCCs.
- In acyclic condensed graphs (datasets 1, 2, 4, 7), we observed realistic multi-stage task dependencies.

### Performance Observations
- **DFS visits ≈ number of nodes** → confirms linear Tarjan behavior.
- **DFS edges ≈ number of edges** → consistent edge traversal complexity.
- **Kahn/Relaxations = 0** for single-SCC DAGs (no true DAG edges).
- Execution time increases slightly with graph density.

---

## Conclusions

- **SCC Compression** is crucial for handling cyclic dependencies in scheduling problems.  
  Once cycles are detected and collapsed, DAG-based methods can be applied safely.

- **Topological Sorting** gives a valid execution order of tasks or task clusters.

- **Longest Path (Critical Path)** helps identify time bottlenecks in project/task networks.

- **Complexity**: all algorithms are `O(V + E)` and perform efficiently even on dense graphs.

---

## Recommendations

| Scenario | Recommended Algorithm | Why |
|-----------|------------------------|-----|
| Cyclic dependencies present | Tarjan SCC | Detect & group cycles |
| Acyclic dependencies only | Kahn Toposort | Simple and fast |
| Time optimization / critical path | DAG Longest Path | Identifies bottlenecks |
| Real-time monitoring | Combine SCC + Toposort + Longest Path | Most general case |

---

### Overall Performance Summary
- Average SCC runtime: **0.08 ms**
- Maximum (dataset 8, 30 nodes, 260 edges): **0.17 ms**
- Memory usage negligible for all tested sizes.

