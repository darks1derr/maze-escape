import java.util.*;

/** Mission 1 and 2: build spanning trees using Kruskal and Prim. */
public class Maze {
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet d = new DisjointSet(w.cells());
        int[] chosen = new int[Math.max(0, w.cells() - 1)]; int n = 0;
        for (int k = 0; k < w.count(); k++) if (d.union(w.a()[k], w.b()[k])) chosen[n++] = k;
        return Arrays.copyOf(chosen, n);
    }
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> w.cost()[i]));
        DisjointSet d = new DisjointSet(w.cells());
        int[] chosen = new int[Math.max(0, w.cells() - 1)]; int n = 0;
        for (int k : order) if (d.union(w.a()[k], w.b()[k])) { chosen[n++] = k; if (n == w.cells() - 1) break; }
        return Arrays.copyOf(chosen, n);
    }
    private record Edge(int to, int cost) {}
    private record Candidate(int to, int cost) {}
    public static long primCost(Input.Walls w) {
        int n = w.cells(); if (n <= 1) return 0;
        @SuppressWarnings("unchecked") ArrayList<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i], b = w.b()[i], c = w.cost()[i];
            graph[a].add(new Edge(b, c)); graph[b].add(new Edge(a, c));
        }
        int[] best = new int[n]; Arrays.fill(best, Integer.MAX_VALUE);
        boolean[] used = new boolean[n];
        PriorityQueue<Candidate> pq = new PriorityQueue<>(Comparator.comparingInt(Candidate::cost));
        best[0] = 0; pq.add(new Candidate(0, 0)); long total = 0; int taken = 0;
        while (!pq.isEmpty() && taken < n) {
            Candidate current = pq.poll(); int u = current.to();
            if (used[u] || current.cost() != best[u]) continue;
            used[u] = true; taken++; total += current.cost();
            for (Edge e : graph[u]) {
                if (!used[e.to()] && e.cost() < best[e.to()]) {
                    best[e.to()] = e.cost(); pq.add(new Candidate(e.to(), e.cost()));
                }
            }
        }
        if (taken != n) throw new IllegalArgumentException("Maze graph is disconnected");
        return total;
    }
}
