import java.util.*;

/**
 * Comprehensive Graph Data Structure and Algorithms Tutorial in Java
 * 
 * This tutorial covers:
 * 1. Graph representation (Adjacency List and Adjacency Matrix)
 * 2. Graph traversal (BFS and DFS)
 * 3. Shortest path algorithms (Dijkstra's and Bellman-Ford)
 * 4. Minimum Spanning Tree (Kruskal's and Prim's)
 * 5. Topological Sorting
 * 6. Cycle Detection
 * 7. Connected Components
 */

public class GraphDSATutorial {
    
    // ==================== GRAPH REPRESENTATION ====================
    
    /**
     * Graph representation using Adjacency List
     * Space Complexity: O(V + E)
     * Time Complexity for checking edge: O(V) in worst case
     */
    static class Graph {
        private int V; // number of vertices
        private List<List<Integer>> adjList;
        
        public Graph(int vertices) {
            this.V = vertices;
            adjList = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                adjList.add(new ArrayList<>());
            }
        }
        
        // Add edge to undirected graph
        public void addEdge(int u, int v) {
            adjList.get(u).add(v);
            adjList.get(v).add(u); // for undirected graph
        }
        
        // Add edge to directed graph
        public void addDirectedEdge(int u, int v) {
            adjList.get(u).add(v);
        }
        
        // Get neighbors of a vertex
        public List<Integer> getNeighbors(int vertex) {
            return adjList.get(vertex);
        }
        
        public int getVertices() {
            return V;
        }
    }
    
    /**
     * Weighted Graph representation using Adjacency List
     */
    static class WeightedGraph {
        private int V;
        private List<List<Edge>> adjList;
        
        static class Edge {
            int destination;
            int weight;
            
            Edge(int dest, int w) {
                this.destination = dest;
                this.weight = w;
            }
        }
        
        public WeightedGraph(int vertices) {
            this.V = vertices;
            adjList = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                adjList.add(new ArrayList<>());
            }
        }
        
        public void addEdge(int u, int v, int weight) {
            adjList.get(u).add(new Edge(v, weight));
            adjList.get(v).add(new Edge(u, weight)); // for undirected graph
        }
        
        public void addDirectedEdge(int u, int v, int weight) {
            adjList.get(u).add(new Edge(v, weight));
        }
        
        public List<Edge> getNeighbors(int vertex) {
            return adjList.get(vertex);
        }
        
        public int getVertices() {
            return V;
        }
    }
    
    // ==================== GRAPH TRAVERSAL ====================
    
    /**
     * Breadth-First Search (BFS)
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     * Uses: Shortest path in unweighted graph, Level-order traversal
     */
    public static void bfs(Graph graph, int start) {
        boolean[] visited = new boolean[graph.getVertices()];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[start] = true;
        queue.offer(start);
        
        System.out.println("BFS starting from vertex " + start + ":");
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");
            
            for (int neighbor : graph.getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        System.out.println();
    }
    
    /**
     * Depth-First Search (DFS) - Recursive
     * Time Complexity: O(V + E)
     * Space Complexity: O(V) for recursion stack
     * Uses: Topological sorting, Cycle detection, Connected components
     */
    public static void dfsRecursive(Graph graph, int start) {
        boolean[] visited = new boolean[graph.getVertices()];
        System.out.println("DFS (Recursive) starting from vertex " + start + ":");
        dfsUtil(graph, start, visited);
        System.out.println();
    }
    
    private static void dfsUtil(Graph graph, int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(vertex + " ");
        
        for (int neighbor : graph.getNeighbors(vertex)) {
            if (!visited[neighbor]) {
                dfsUtil(graph, neighbor, visited);
            }
        }
    }
    
    /**
     * Depth-First Search (DFS) - Iterative using Stack
     */
    public static void dfsIterative(Graph graph, int start) {
        boolean[] visited = new boolean[graph.getVertices()];
        Stack<Integer> stack = new Stack<>();
        
        visited[start] = true;
        stack.push(start);
        
        System.out.println("DFS (Iterative) starting from vertex " + start + ":");
        
        while (!stack.isEmpty()) {
            int current = stack.pop();
            System.out.print(current + " ");
            
            for (int neighbor : graph.getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    stack.push(neighbor);
                }
            }
        }
        System.out.println();
    }
    
    // ==================== SHORTEST PATH ALGORITHMS ====================
    
    /**
     * Dijkstra's Algorithm for Shortest Path
     * Time Complexity: O((V + E) log V) with Priority Queue
     * Space Complexity: O(V)
     * Works for: Positive weighted graphs
     */
    public static int[] dijkstra(WeightedGraph graph, int start) {
        int V = graph.getVertices();
        int[] distances = new int[V];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;
        
        // Priority queue to get minimum distance vertex
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{start, 0});
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int vertex = current[0];
            int distance = current[1];
            
            if (distance > distances[vertex]) continue;
            
            for (WeightedGraph.Edge edge : graph.getNeighbors(vertex)) {
                int neighbor = edge.destination;
                int weight = edge.weight;
                
                if (distances[vertex] + weight < distances[neighbor]) {
                    distances[neighbor] = distances[vertex] + weight;
                    pq.offer(new int[]{neighbor, distances[neighbor]});
                }
            }
        }
        
        return distances;
    }
    
    /**
     * Bellman-Ford Algorithm for Shortest Path
     * Time Complexity: O(VE)
     * Space Complexity: O(V)
     * Works for: Graphs with negative weights (no negative cycles)
     */
    public static int[] bellmanFord(WeightedGraph graph, int start) {
        int V = graph.getVertices();
        int[] distances = new int[V];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;
        
        // Relax all edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            for (int u = 0; u < V; u++) {
                for (WeightedGraph.Edge edge : graph.getNeighbors(u)) {
                    int v = edge.destination;
                    int weight = edge.weight;
                    
                    if (distances[u] != Integer.MAX_VALUE && 
                        distances[u] + weight < distances[v]) {
                        distances[v] = distances[u] + weight;
                    }
                }
            }
        }
        
        // Check for negative cycles
        for (int u = 0; u < V; u++) {
            for (WeightedGraph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.destination;
                int weight = edge.weight;
                
                if (distances[u] != Integer.MAX_VALUE && 
                    distances[u] + weight < distances[v]) {
                    System.out.println("Graph contains negative cycle!");
                    return null;
                }
            }
        }
        
        return distances;
    }
    
    // ==================== MINIMUM SPANNING TREE ====================
    
    /**
     * Kruskal's Algorithm for Minimum Spanning Tree
     * Time Complexity: O(E log E) or O(E log V)
     * Space Complexity: O(V)
     */
    static class KruskalMST {
        static class Edge implements Comparable<Edge> {
            int src, dest, weight;
            
            Edge(int src, int dest, int weight) {
                this.src = src;
                this.dest = dest;
                this.weight = weight;
            }
            
            @Override
            public int compareTo(Edge other) {
                return this.weight - other.weight;
            }
        }
        
        static class DisjointSet {
            int[] parent, rank;
            
            DisjointSet(int n) {
                parent = new int[n];
                rank = new int[n];
                for (int i = 0; i < n; i++) {
                    parent[i] = i;
                }
            }
            
            int find(int x) {
                if (parent[x] != x) {
                    parent[x] = find(parent[x]);
                }
                return parent[x];
            }
            
            void union(int x, int y) {
                int rootX = find(x);
                int rootY = find(y);
                
                if (rootX == rootY) return;
                
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
        
        public static List<Edge> kruskalMST(List<Edge> edges, int V) {
            List<Edge> result = new ArrayList<>();
            Collections.sort(edges);
            
            DisjointSet ds = new DisjointSet(V);
            
            for (Edge edge : edges) {
                int srcRoot = ds.find(edge.src);
                int destRoot = ds.find(edge.dest);
                
                if (srcRoot != destRoot) {
                    result.add(edge);
                    ds.union(srcRoot, destRoot);
                }
            }
            
            return result;
        }
    }
    
    /**
     * Prim's Algorithm for Minimum Spanning Tree
     * Time Complexity: O(E log V)
     * Space Complexity: O(V)
     */
    public static List<WeightedGraph.Edge> primMST(WeightedGraph graph, int start) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];
        List<WeightedGraph.Edge> mst = new ArrayList<>();
        
        PriorityQueue<WeightedGraph.Edge> pq = new PriorityQueue<>((a, b) -> a.weight - b.weight);
        
        visited[start] = true;
        for (WeightedGraph.Edge edge : graph.getNeighbors(start)) {
            pq.offer(edge);
        }
        
        while (!pq.isEmpty() && mst.size() < V - 1) {
            WeightedGraph.Edge edge = pq.poll();
            
            if (visited[edge.destination]) continue;
            
            visited[edge.destination] = true;
            mst.add(edge);
            
            for (WeightedGraph.Edge neighborEdge : graph.getNeighbors(edge.destination)) {
                if (!visited[neighborEdge.destination]) {
                    pq.offer(neighborEdge);
                }
            }
        }
        
        return mst;
    }
    
    // ==================== TOPOLOGICAL SORTING ====================
    
    /**
     * Topological Sort using DFS
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     * Works for: Directed Acyclic Graphs (DAG)
     */
    public static List<Integer> topologicalSort(Graph graph) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                topologicalSortUtil(graph, i, visited, stack);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        
        return result;
    }
    
    private static void topologicalSortUtil(Graph graph, int vertex, boolean[] visited, Stack<Integer> stack) {
        visited[vertex] = true;
        
        for (int neighbor : graph.getNeighbors(vertex)) {
            if (!visited[neighbor]) {
                topologicalSortUtil(graph, neighbor, visited, stack);
            }
        }
        
        stack.push(vertex);
    }
    
    // ==================== CYCLE DETECTION ====================
    
    /**
     * Detect cycle in undirected graph using DFS
     */
    public static boolean hasCycleUndirected(Graph graph) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (hasCycleUndirectedUtil(graph, i, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean hasCycleUndirectedUtil(Graph graph, int vertex, boolean[] visited, int parent) {
        visited[vertex] = true;
        
        for (int neighbor : graph.getNeighbors(vertex)) {
            if (!visited[neighbor]) {
                if (hasCycleUndirectedUtil(graph, neighbor, visited, vertex)) {
                    return true;
                }
            } else if (neighbor != parent) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Detect cycle in directed graph using DFS
     */
    public static boolean hasCycleDirected(Graph graph) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (hasCycleDirectedUtil(graph, i, visited, recStack)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean hasCycleDirectedUtil(Graph graph, int vertex, boolean[] visited, boolean[] recStack) {
        visited[vertex] = true;
        recStack[vertex] = true;
        
        for (int neighbor : graph.getNeighbors(vertex)) {
            if (!visited[neighbor]) {
                if (hasCycleDirectedUtil(graph, neighbor, visited, recStack)) {
                    return true;
                }
            } else if (recStack[neighbor]) {
                return true;
            }
        }
        
        recStack[vertex] = false;
        return false;
    }
    
    // ==================== CONNECTED COMPONENTS ====================
    
    /**
     * Find connected components in undirected graph
     */
    public static List<List<Integer>> findConnectedComponents(Graph graph) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];
        List<List<Integer>> components = new ArrayList<>();
        
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                List<Integer> component = new ArrayList<>();
                dfsComponent(graph, i, visited, component);
                components.add(component);
            }
        }
        
        return components;
    }
    
    private static void dfsComponent(Graph graph, int vertex, boolean[] visited, List<Integer> component) {
        visited[vertex] = true;
        component.add(vertex);
        
        for (int neighbor : graph.getNeighbors(vertex)) {
            if (!visited[neighbor]) {
                dfsComponent(graph, neighbor, visited, component);
            }
        }
    }
    
    // ==================== MAIN METHOD WITH EXAMPLES ====================
    
    public static void main(String[] args) {
        System.out.println("=== GRAPH DATA STRUCTURES AND ALGORITHMS TUTORIAL ===\n");
        
        // Example 1: Basic Graph Traversal
        System.out.println("1. BASIC GRAPH TRAVERSAL");
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        
        bfs(graph, 0);
        dfsRecursive(graph, 0);
        dfsIterative(graph, 0);
        
        // Example 2: Shortest Path
        System.out.println("\n2. SHORTEST PATH ALGORITHMS");
        WeightedGraph weightedGraph = new WeightedGraph(5);
        weightedGraph.addEdge(0, 1, 4);
        weightedGraph.addEdge(0, 2, 2);
        weightedGraph.addEdge(1, 2, 1);
        weightedGraph.addEdge(1, 3, 5);
        weightedGraph.addEdge(2, 3, 8);
        weightedGraph.addEdge(2, 4, 10);
        weightedGraph.addEdge(3, 4, 2);
        
        int[] dijkstraResult = dijkstra(weightedGraph, 0);
        System.out.println("Dijkstra's shortest distances from vertex 0:");
        for (int i = 0; i < dijkstraResult.length; i++) {
            System.out.println("To vertex " + i + ": " + dijkstraResult[i]);
        }
        
        // Example 3: Minimum Spanning Tree
        System.out.println("\n3. MINIMUM SPANNING TREE");
        List<KruskalMST.Edge> edges = Arrays.asList(
            new KruskalMST.Edge(0, 1, 4),
            new KruskalMST.Edge(0, 2, 2),
            new KruskalMST.Edge(1, 2, 1),
            new KruskalMST.Edge(1, 3, 5),
            new KruskalMST.Edge(2, 3, 8),
            new KruskalMST.Edge(2, 4, 10),
            new KruskalMST.Edge(3, 4, 2)
        );
        
        List<KruskalMST.Edge> mst = KruskalMST.kruskalMST(edges, 5);
        System.out.println("Kruskal's MST edges:");
        int totalWeight = 0;
        for (KruskalMST.Edge edge : mst) {
            System.out.println(edge.src + " - " + edge.dest + " : " + edge.weight);
            totalWeight += edge.weight;
        }
        System.out.println("Total MST weight: " + totalWeight);
        
        // Example 4: Topological Sort
        System.out.println("\n4. TOPOLOGICAL SORTING");
        Graph dag = new Graph(6);
        dag.addDirectedEdge(5, 2);
        dag.addDirectedEdge(5, 0);
        dag.addDirectedEdge(4, 0);
        dag.addDirectedEdge(4, 1);
        dag.addDirectedEdge(2, 3);
        dag.addDirectedEdge(3, 1);
        
        List<Integer> topoSort = topologicalSort(dag);
        System.out.println("Topological sort: " + topoSort);
        
        // Example 5: Cycle Detection
        System.out.println("\n5. CYCLE DETECTION");
        System.out.println("Undirected graph has cycle: " + hasCycleUndirected(graph));
        
        Graph directedGraph = new Graph(4);
        directedGraph.addDirectedEdge(0, 1);
        directedGraph.addDirectedEdge(1, 2);
        directedGraph.addDirectedEdge(2, 3);
        directedGraph.addDirectedEdge(3, 1); // creates cycle
        
        System.out.println("Directed graph has cycle: " + hasCycleDirected(directedGraph));
        
        // Example 6: Connected Components
        System.out.println("\n6. CONNECTED COMPONENTS");
        Graph disconnectedGraph = new Graph(7);
        disconnectedGraph.addEdge(0, 1);
        disconnectedGraph.addEdge(1, 2);
        disconnectedGraph.addEdge(3, 4);
        disconnectedGraph.addEdge(5, 6);
        
        List<List<Integer>> components = findConnectedComponents(disconnectedGraph);
        System.out.println("Connected components:");
        for (int i = 0; i < components.size(); i++) {
            System.out.println("Component " + i + ": " + components.get(i));
        }
        
        System.out.println("\n=== TUTORIAL COMPLETE ===");
    }
}