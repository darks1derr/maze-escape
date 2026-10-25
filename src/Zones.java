import java.util.*;

/** Strongly connected components and dynamic programming on the resulting DAG. */
public class Zones {
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n(); int[][] out = Graphs.slidesFrom(s), in = Graphs.slidesInto(s);
        boolean[] seen = new boolean[n]; int[] finish = new int[n]; int finishN = 0;
        int[] stack = new int[n], next = new int[n];
        for (int start = 0; start < n; start++) if (!seen[start]) {
            int top = 0; stack[0] = start; next[0] = 0; seen[start] = true;
            while (top >= 0) {
                int u = stack[top];
                if (next[top] < out[u].length) {
                    int edge = out[u][next[top]++], v = s.to()[edge];
                    if (!seen[v]) { seen[v] = true; top++; stack[top] = v; next[top] = 0; }
                } else { finish[finishN++] = u; top--; }
            }
        }
        int[] zone = new int[n]; Arrays.fill(zone, -1); int z = 0;
        for (int i = finishN - 1; i >= 0; i--) {
            int start = finish[i]; if (zone[start] != -1) continue;
            int top = 0; stack[0] = start; zone[start] = z;
            while (top >= 0) {
                int u = stack[top--];
                for (int edge : in[u]) { int v = s.from()[edge]; if (zone[v] == -1) { zone[v] = z; stack[++top] = v; } }
            }
            z++;
        }
        return zone;
    }
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        int start = zoneOf[0], goal = zoneOf[s.n() - 1];
        @SuppressWarnings("unchecked") HashMap<Integer, Integer>[] edges = new HashMap[zoneCount];
        for (int i = 0; i < zoneCount; i++) edges[i] = new HashMap<>();
        for (int i = 0; i < s.count(); i++) {
            int a = zoneOf[s.from()[i]], b = zoneOf[s.to()[i]];
            if (a == b) continue;
            edges[a].merge(b, s.time()[i], Math::min);
        }
        int[] longest = new int[zoneCount]; Arrays.fill(longest, Integer.MIN_VALUE); longest[start] = 1;
        long inf = Long.MAX_VALUE / 4; long[] dist = new long[zoneCount]; Arrays.fill(dist, inf); dist[start] = 0;
        for (int a = 0; a < zoneCount; a++) {
            if (longest[a] == Integer.MIN_VALUE && dist[a] == inf) continue;
            for (var e : edges[a].entrySet()) {
                int b = e.getKey();
                if (longest[a] != Integer.MIN_VALUE) longest[b] = Math.max(longest[b], longest[a] + 1);
                if (dist[a] != inf) dist[b] = Math.min(dist[b], dist[a] + e.getValue());
            }
        }
        return new long[]{longest[goal], dist[goal]};
    }
}
