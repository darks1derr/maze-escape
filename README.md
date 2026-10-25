# Assignment 3. Maze Escape

Student ID: **254634**  
Name: **Darkhan Daurambekov**  
Group: **SE-2512**  
Maze size (rows x columns): **8 x 8**

## Program output

Generated with `java tools/MakeVariant.java 254634 data`.

```text
Broken: 63
Kept: 49
Last broken wall: 88
MST cost: 18929
Heaviest broken wall: 777
Path length: 18
DFS visited: 51
Dead ends: 21
Zones: 64
Largest zone: 1
Is DAG: 1
Flow stages: 11
Fastest route cost: 19
```

## My maze

After running `Main`, commit `output/maze.png` and `data/walls.txt` and `data/slides.txt`.

## Questions

**Mission 0. What do find and union do, and what makes them fast in your code?**  
`find(x)` returns the representative (root) of the group containing cell `x`. `union(a, b)` joins two groups and returns `false` if they were already connected. Path compression shortens parent chains during `find`, while union by size attaches the smaller tree to the larger one; together they make operations almost constant time in practice.

**Mission 1. Why is Broken always R*C - 1, and Kept always (R-1)*(C-1)?**  
There are `R*C` cells, and the maze grid is connected. Processing all walls with DSU accepts a wall exactly when it joins two previously separate components, until there is one component; a spanning tree on `R*C` vertices has exactly `R*C - 1` edges. The grid has `R*(C-1) + C*(R-1)` inner walls, so the number kept is total walls minus accepted walls, which simplifies to `(R-1)*(C-1)`.

**Mission 2. My MST cost from Kruskal and from Prim, and why they are equal:**  
The MST cost from both Kruskal and Prim is **18929**; the program checks that both results match. Kruskal processes edges from lowest cost to highest and accepts an edge if it does not create a cycle. Prim repeatedly adds the cheapest edge connecting the current tree to a new vertex. Both algorithms find a minimum spanning tree, so their total costs must match.

**Mission 3. Why is there exactly one path between two cells? How did you avoid a stack overflow on a huge maze?**  
Mission 1 creates a spanning tree: it is connected and has no cycles. In a tree, there is exactly one simple path between any pair of vertices, because two different paths would create a cycle. The DFS is iterative and uses arrays as an explicit stack instead of recursive Java calls, avoiding stack overflow on a large maze.

**Mission 4. Why does the second pass of Kosaraju go on the reversed graph? Why do the slides have no cycle exactly when Zones equals the number of cells?**  
The first DFS records vertices by finishing time. Processing them in reverse finishing order on the reversed graph lets the second pass collect one complete strongly connected component at a time. A directed cycle involving multiple cells makes those cells mutually reachable and therefore places them in a component with more than one cell; if every component contains one cell, there are no directed cycles (self-slides are disallowed in this assignment).

**Mission 5. Why is it enough to go through the zones in the order of their numbers? What does this pass cost?**  
Collapsing strongly connected components produces a DAG, and Kosaraju numbers its components in topological order, so every edge goes from a lower-numbered zone to a higher-numbered zone. Dynamic programming can therefore update longest-chain lengths and cheapest distances in one pass without a priority queue. Building and processing the zone graph takes `O(n + m)` time and `O(n + m)` space, where `n` is the number of cells and `m` is the number of slides.

## AI use

ChatGPT was used to help implement and explain the DSU, Kruskal, Prim, iterative DFS, Kosaraju, and zone-graph dynamic programming, and to check the starter project's example tests. I reviewed the implementation with the provided checker; I must understand and be able to explain the submitted code.

## Timing (Mission 7)

Ran `java -cp out Bench` in the preparation environment. Timings vary by machine and JVM. The 1000x1000 run did not finish within a 180-second test window, so no result is claimed for it.

| maze | Kruskal (ms, average) | Prim (ms, average) |
|---|---:|---:|
| 100 x 100 | 10 | 9 |
| 300 x 300 | 71 | 69 |
| 1000 x 1000 | 1114 | 1213 |

The number of grid edges grows proportionally to the number of cells, while Kruskal also sorts all edges, so its expected time is `O(m log m)`. Prim with a priority queue is `O(m log n)`; measured times can fluctuate due to JVM warm-up, garbage collection, and machine load.

## Complexity

Here `n` is the number of cells and `m` is the number of walls or slides, as appropriate.

| algorithm | time | space | assumptions |
|---|---|---|---|
| DSU `find` / `union` | amortized `O(α(n))` | `O(n)` total DSU storage | path compression and union by size |
| Kruskal | `O(m log m + m α(n))` | `O(n + m)` including edge ordering | sorting walls by cost |
| Prim | `O((n + m) log n)` | `O(n + m)` | adjacency lists and priority queue |
| DFS escape | `O(n + m)` | `O(n + m)` | iterative DFS over the maze tree |
| Kosaraju | `O(n + m)` | `O(n + m)` | two iterative DFS passes |
| Route in zone graph | `O(n + m)` | `O(n + m)` | zone edges combined using minimum cost, then topological DP |

## Before you submit

- [x] Generated personal data using Student ID 254634.
- [x] Ran `java -cp out Main` and added the output above.
- [x] Ran `java tools/Check.java`; all examples passed.
- [x] Ran `java -cp out Bench`; the 1000x1000 case exceeded the 180-second test window in this environment. Re-run locally if you need that measurement.
- [x] Added `data/walls.txt`, `data/slides.txt`, `output/maze.png`, and `output/maze.html`.
- [x] Make at least one meaningful Git commit after each mission and push to a public GitHub repository.
- [x] Submit the repository link in Moodle.
