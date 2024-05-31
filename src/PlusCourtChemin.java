import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


public class PlusCourtChemin {
    
    static class Edge {
        int target;
        int weight;

        public Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    public int plusCourtChemin(List<List<Edge>> graph, int start, int end) {
        int n = graph.size();
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[start] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> dist[a] - dist[b]);
        pq.add(start);

        while (!pq.isEmpty()) {
            int u = pq.poll();
            if (visited[u]) continue;
            visited[u] = true;

            for (Edge edge : graph.get(u)) {
                int v = edge.target;
                int weight = edge.weight;

                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(v);
                }
            }

            // Stop early if we reached the destination
            if (u == end) break;
        }

        return dist[end];
    }

    public static void main(String[] args) {
        List<List<Edge>> graph = new ArrayList<>();

        // Example graph construction
        for (int i = 0; i < 4; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(new Edge(1, 1));
        graph.get(0).add(new Edge(2, 4));
        graph.get(1).add(new Edge(2, 2));
        graph.get(1).add(new Edge(3, 5));
        graph.get(2).add(new Edge(3, 1));

        PlusCourtChemin pcc = new PlusCourtChemin();
        int start = 0;
        int end = 3;
        int distance = pcc.plusCourtChemin(graph, start, end);

        System.out.println("La distance la plus courte du site " + start + " au site " + end + " est: " + distance);
    }
}


