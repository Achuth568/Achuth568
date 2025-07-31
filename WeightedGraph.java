import java.util.*;

/**
 * Weighted Graph Implementation with Shortest Path Algorithms
 * Supports both directed and undirected weighted graphs
 */
public class WeightedGraph {
    private int vertices;
    private boolean isDirected;
    private Map<Integer, List<Edge>> adjacencyList;
    
    // Edge class to represent weighted edges
    public static class Edge {
        int destination;
        int weight;
        
        public Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
        
        @Override
        public String toString() {
            return "(" + destination + ", " + weight + ")";
        }
    }
    
    // Constructor
    public WeightedGraph(int vertices, boolean isDirected) {
        this.vertices = vertices;
        this.isDirected = isDirected;
        this.adjacencyList = new HashMap<>();
        
        // Initialize adjacency list for all vertices
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }
    
    // Add weighted edge
    public void addEdge(int source, int destination, int weight) {
        adjacencyList.get(source).add(new Edge(destination, weight));
        
        // If undirected, add edge in both directions
        if (!isDirected) {
            adjacencyList.get(destination).add(new Edge(source, weight));
        }
    }
    
    // Print the weighted graph
    public void printGraph() {
        System.out.println("Weighted Graph representation:");
        for (int i = 0; i < vertices; i++) {
            System.out.print("Vertex " + i + ": ");
            for (Edge edge : adjacencyList.get(i)) {
                System.out.print(edge + " ");
            }
            System.out.println();
        }
    }
    
    // Dijkstra's Algorithm for shortest path from a source vertex
    public Map<Integer, Integer> dijkstra(int source) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
        Set<Integer> visited = new HashSet<>();
        
        // Initialize distances
        for (int i = 0; i < vertices; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        
        pq.offer(new Node(source, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int currentVertex = current.vertex;
            
            if (visited.contains(currentVertex)) {
                continue;
            }
            
            visited.add(currentVertex);
            
            // Check all neighbors
            for (Edge edge : adjacencyList.get(currentVertex)) {
                int neighbor = edge.destination;
                int newDistance = distances.get(currentVertex) + edge.weight;
                
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, currentVertex);
                    pq.offer(new Node(neighbor, newDistance));
                }
            }
        }
        
        return distances;
    }
    
    // Get shortest path between two vertices using Dijkstra
    public List<Integer> getShortestPath(int source, int destination) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
        Set<Integer> visited = new HashSet<>();
        
        // Initialize distances
        for (int i = 0; i < vertices; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        
        pq.offer(new Node(source, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int currentVertex = current.vertex;
            
            if (visited.contains(currentVertex)) {
                continue;
            }
            
            visited.add(currentVertex);
            
            // If we reached destination, break
            if (currentVertex == destination) {
                break;
            }
            
            // Check all neighbors
            for (Edge edge : adjacencyList.get(currentVertex)) {
                int neighbor = edge.destination;
                int newDistance = distances.get(currentVertex) + edge.weight;
                
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, currentVertex);
                    pq.offer(new Node(neighbor, newDistance));
                }
            }
        }
        
        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        Integer current = destination;
        
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        
        // If path doesn't start with source, no path exists
        if (path.isEmpty() || path.get(0) != source) {
            return new ArrayList<>();
        }
        
        return path;
    }
    
    // Floyd-Warshall Algorithm for all-pairs shortest paths
    public int[][] floydWarshall() {
        int[][] dist = new int[vertices][vertices];
        
        // Initialize distance matrix
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        
        // Fill in direct edges
        for (int i = 0; i < vertices; i++) {
            for (Edge edge : adjacencyList.get(i)) {
                dist[i][edge.destination] = edge.weight;
            }
        }
        
        // Floyd-Warshall main algorithm
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && 
                        dist[k][j] != Integer.MAX_VALUE && 
                        dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        
        return dist;
    }
    
    // Print distance matrix from Floyd-Warshall
    public void printDistanceMatrix(int[][] dist) {
        System.out.println("All-pairs shortest distances:");
        System.out.print("   ");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%4d", i);
        }
        System.out.println();
        
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%2d:", i);
            for (int j = 0; j < vertices; j++) {
                if (dist[i][j] == Integer.MAX_VALUE) {
                    System.out.print(" INF");
                } else {
                    System.out.printf("%4d", dist[i][j]);
                }
            }
            System.out.println();
        }
    }
    
    // Bellman-Ford Algorithm (handles negative weights)
    public Map<Integer, Integer> bellmanFord(int source) {
        Map<Integer, Integer> distances = new HashMap<>();
        
        // Initialize distances
        for (int i = 0; i < vertices; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        
        // Relax edges V-1 times
        for (int i = 0; i < vertices - 1; i++) {
            for (int u = 0; u < vertices; u++) {
                if (distances.get(u) != Integer.MAX_VALUE) {
                    for (Edge edge : adjacencyList.get(u)) {
                        int v = edge.destination;
                        int weight = edge.weight;
                        
                        if (distances.get(u) + weight < distances.get(v)) {
                            distances.put(v, distances.get(u) + weight);
                        }
                    }
                }
            }
        }
        
        // Check for negative cycles
        for (int u = 0; u < vertices; u++) {
            if (distances.get(u) != Integer.MAX_VALUE) {
                for (Edge edge : adjacencyList.get(u)) {
                    int v = edge.destination;
                    int weight = edge.weight;
                    
                    if (distances.get(u) + weight < distances.get(v)) {
                        System.out.println("Graph contains negative cycle!");
                        return null;
                    }
                }
            }
        }
        
        return distances;
    }
    
    // Minimum Spanning Tree using Kruskal's Algorithm
    public List<Edge> kruskalMST() {
        if (isDirected) {
            System.out.println("MST is for undirected graphs only");
            return new ArrayList<>();
        }
        
        List<EdgeWithVertices> edges = new ArrayList<>();
        
        // Collect all edges
        for (int u = 0; u < vertices; u++) {
            for (Edge edge : adjacencyList.get(u)) {
                if (u < edge.destination) { // Avoid duplicate edges in undirected graph
                    edges.add(new EdgeWithVertices(u, edge.destination, edge.weight));
                }
            }
        }
        
        // Sort edges by weight
        edges.sort(Comparator.comparingInt(e -> e.weight));
        
        // Union-Find data structure
        UnionFind uf = new UnionFind(vertices);
        List<Edge> mst = new ArrayList<>();
        
        for (EdgeWithVertices edge : edges) {
            if (uf.find(edge.source) != uf.find(edge.destination)) {
                uf.union(edge.source, edge.destination);
                mst.add(new Edge(edge.destination, edge.weight));
                
                if (mst.size() == vertices - 1) {
                    break;
                }
            }
        }
        
        return mst;
    }
    
    // Helper classes
    private static class Node {
        int vertex;
        int distance;
        
        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
    
    private static class EdgeWithVertices {
        int source;
        int destination;
        int weight;
        
        EdgeWithVertices(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }
    
    // Union-Find data structure for Kruskal's algorithm
    private static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // Union by rank
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
    
    public int getVertices() {
        return vertices;
    }
}