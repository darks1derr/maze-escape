import java.util.*;

/** Iterative depth-first search, preserving up/right/down/left neighbour order. */
public class Escape {
    public record Result(int[] path, int visited, int deadEnds) {}
    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells(), cols = w.cols(), goal = n - 1;
        int[][] graph = Graphs.open(w, broken);
        int dead = 0; for (int[] neighbours : graph) if (neighbours.length == 1) dead++;
        boolean[] seen = new boolean[n]; int[] parent = new int[n]; Arrays.fill(parent, -1);
        int[] stackCell = new int[n], stackNext = new int[n]; int top = 0, visited = 1;
        stackCell[0] = 0; seen[0] = true;
        while (top >= 0 && !seen[goal]) {
            int u = stackCell[top];
            int[] order = {u - cols, u + 1, u + cols, u - 1};
            boolean advanced = false;
            while (stackNext[top] < 4) {
                int v = order[stackNext[top]++];
                if (v < 0 || v >= n) continue;
                if ((v == u + 1 && u / cols != v / cols) || (v == u - 1 && u / cols != v / cols)) continue;
                if (contains(graph[u], v) && !seen[v]) {
                    seen[v] = true; visited++; parent[v] = u; top++;
                    stackCell[top] = v; stackNext[top] = 0; advanced = true; break;
                }
            }
            if (!advanced && stackNext[top] >= 4) top--;
        }
        if (!seen[goal]) return new Result(new int[0], visited, dead);
        int len = 1; for (int x = goal; x != 0; x = parent[x]) len++;
        int[] path = new int[len]; int x = goal;
        for (int i = len - 1; i >= 0; i--) { path[i] = x; if (x != 0) x = parent[x]; }
        return new Result(path, visited, dead);
    }
    private static boolean contains(int[] a, int x) { for (int v : a) if (v == x) return true; return false; }
}
