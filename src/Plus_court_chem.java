import java.util.ArrayList;
import java.util.List;

public class Plus_court_chem {
	
	static class Edge {
        int target;
        int weight;

        public Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

	public int[][] plus_court_chemin(List<Integer> sites) {
		int n = sites.size();
		List<List<Edge>> sites = new ArrayList<>();
        
		for (int i = 0; i < n; i++) {
			sites.add(new ArrayList<>());
        }
        
		int[][] distances = new int[n][n];

        for (int i = 0; i < n; i++) {
            distances[i] = dijkstra(sites, i);
        }

        return distances;
	}

	private int[] dijkstra(List<List<Plus_court_chem.Edge>> sites, int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
