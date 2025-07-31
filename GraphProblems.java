import java.util.*;

/**
 * Common Graph Problems and Solutions
 * Collection of popular graph algorithms and problem-solving techniques
 */
public class GraphProblems {
    
    // Problem 1: Find all connected components in an undirected graph
    public static List<List<Integer>> findConnectedComponents(Graph graph) {
        List<List<Integer>> components = new ArrayList<>();
        boolean[] visited = new boolean[graph.getVertices()];
        
        for (int i = 0; i < graph.getVertices(); i++) {
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
    
    // Problem 2: Find if there's a path between two vertices
    public static boolean hasPath(Graph graph, int source, int destination) {
        if (source == destination) return true;
        
        boolean[] visited = new boolean[graph.getVertices()];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[source] = true;
        queue.offer(source);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : graph.getNeighbors(current)) {
                if (neighbor == destination) return true;
                
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        
        return false;
    }
    
    // Problem 3: Find shortest path length between two vertices (unweighted)
    public static int shortestPathLength(Graph graph, int source, int destination) {
        if (source == destination) return 0;
        
        boolean[] visited = new boolean[graph.getVertices()];
        Queue<Integer> queue = new LinkedList<>();
        Queue<Integer> distances = new LinkedList<>();
        
        visited[source] = true;
        queue.offer(source);
        distances.offer(0);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDistance = distances.poll();
            
            for (int neighbor : graph.getNeighbors(current)) {
                if (neighbor == destination) {
                    return currentDistance + 1;
                }
                
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    distances.offer(currentDistance + 1);
                }
            }
        }
        
        return -1; // No path found
    }
    
    // Problem 4: Clone a graph
    public static Graph cloneGraph(Graph original) {
        Graph cloned = new Graph(original.getVertices(), false); // Assuming undirected for simplicity
        
        for (int i = 0; i < original.getVertices(); i++) {
            for (int neighbor : original.getNeighbors(i)) {
                if (i < neighbor) { // Avoid duplicate edges in undirected graph
                    cloned.addEdge(i, neighbor);
                }
            }
        }
        
        return cloned;
    }
    
    // Problem 5: Find bridges in a graph (edges whose removal increases connected components)
    public static List<int[]> findBridges(Graph graph) {
        List<int[]> bridges = new ArrayList<>();
        boolean[] visited = new boolean[graph.getVertices()];
        int[] discovery = new int[graph.getVertices()];
        int[] low = new int[graph.getVertices()];
        int[] parent = new int[graph.getVertices()];
        int[] time = {0};
        
        Arrays.fill(parent, -1);
        
        for (int i = 0; i < graph.getVertices(); i++) {
            if (!visited[i]) {
                bridgesDFS(graph, i, visited, discovery, low, parent, bridges, time);
            }
        }
        
        return bridges;
    }
    
    private static void bridgesDFS(Graph graph, int u, boolean[] visited, int[] discovery, 
                                   int[] low, int[] parent, List<int[]> bridges, int[] time) {
        visited[u] = true;
        discovery[u] = low[u] = ++time[0];
        
        for (int v : graph.getNeighbors(u)) {
            if (!visited[v]) {
                parent[v] = u;
                bridgesDFS(graph, v, visited, discovery, low, parent, bridges, time);
                
                low[u] = Math.min(low[u], low[v]);
                
                // If low[v] > discovery[u], then u-v is a bridge
                if (low[v] > discovery[u]) {
                    bridges.add(new int[]{u, v});
                }
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], discovery[v]);
            }
        }
    }
    
    // Problem 6: Find articulation points (vertices whose removal increases connected components)
    public static List<Integer> findArticulationPoints(Graph graph) {
        List<Integer> articulationPoints = new ArrayList<>();
        boolean[] visited = new boolean[graph.getVertices()];
        int[] discovery = new int[graph.getVertices()];
        int[] low = new int[graph.getVertices()];
        int[] parent = new int[graph.getVertices()];
        boolean[] isArticulation = new boolean[graph.getVertices()];
        int[] time = {0};
        
        Arrays.fill(parent, -1);
        
        for (int i = 0; i < graph.getVertices(); i++) {
            if (!visited[i]) {
                articulationDFS(graph, i, visited, discovery, low, parent, isArticulation, time);
            }
        }
        
        for (int i = 0; i < graph.getVertices(); i++) {
            if (isArticulation[i]) {
                articulationPoints.add(i);
            }
        }
        
        return articulationPoints;
    }
    
    private static void articulationDFS(Graph graph, int u, boolean[] visited, int[] discovery,
                                        int[] low, int[] parent, boolean[] isArticulation, int[] time) {
        int children = 0;
        visited[u] = true;
        discovery[u] = low[u] = ++time[0];
        
        for (int v : graph.getNeighbors(u)) {
            if (!visited[v]) {
                children++;
                parent[v] = u;
                articulationDFS(graph, v, visited, discovery, low, parent, isArticulation, time);
                
                low[u] = Math.min(low[u], low[v]);
                
                // Root is articulation point if it has more than one child
                if (parent[u] == -1 && children > 1) {
                    isArticulation[u] = true;
                }
                
                // Non-root is articulation point if low[v] >= discovery[u]
                if (parent[u] != -1 && low[v] >= discovery[u]) {
                    isArticulation[u] = true;
                }
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], discovery[v]);
            }
        }
    }
    
    // Problem 7: Check if graph is bipartite (can be colored with 2 colors)
    public static boolean isBipartite(Graph graph) {
        int[] colors = new int[graph.getVertices()];
        Arrays.fill(colors, -1);
        
        for (int i = 0; i < graph.getVertices(); i++) {
            if (colors[i] == -1) {
                if (!bipartiteBFS(graph, i, colors)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private static boolean bipartiteBFS(Graph graph, int start, int[] colors) {
        Queue<Integer> queue = new LinkedList<>();
        colors[start] = 0;
        queue.offer(start);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : graph.getNeighbors(current)) {
                if (colors[neighbor] == -1) {
                    colors[neighbor] = 1 - colors[current];
                    queue.offer(neighbor);
                } else if (colors[neighbor] == colors[current]) {
                    return false; // Same color for adjacent vertices
                }
            }
        }
        
        return true;
    }
    
    // Problem 8: Find strongly connected components (for directed graphs)
    public static List<List<Integer>> findStronglyConnectedComponents(Graph graph) {
        List<List<Integer>> sccs = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[graph.getVertices()];
        
        // Step 1: Fill stack with vertices in finishing time order
        for (int i = 0; i < graph.getVertices(); i++) {
            if (!visited[i]) {
                fillOrder(graph, i, visited, stack);
            }
        }
        
        // Step 2: Create transpose graph
        Graph transpose = createTranspose(graph);
        
        // Step 3: Process vertices in order defined by stack
        Arrays.fill(visited, false);
        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!visited[vertex]) {
                List<Integer> scc = new ArrayList<>();
                dfsComponent(transpose, vertex, visited, scc);
                sccs.add(scc);
            }
        }
        
        return sccs;
    }
    
    private static void fillOrder(Graph graph, int vertex, boolean[] visited, Stack<Integer> stack) {
        visited[vertex] = true;
        
        for (int neighbor : graph.getNeighbors(vertex)) {
            if (!visited[neighbor]) {
                fillOrder(graph, neighbor, visited, stack);
            }
        }
        
        stack.push(vertex);
    }
    
    private static Graph createTranspose(Graph graph) {
        Graph transpose = new Graph(graph.getVertices(), true);
        
        for (int i = 0; i < graph.getVertices(); i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                transpose.addEdge(neighbor, i); // Reverse the edge
            }
        }
        
        return transpose;
    }
    
    // Problem 9: Find minimum number of colors needed to color graph (Graph Coloring)
    public static int graphColoring(Graph graph) {
        int[] colors = new int[graph.getVertices()];
        Arrays.fill(colors, -1);
        
        int maxColors = 0;
        
        for (int vertex = 0; vertex < graph.getVertices(); vertex++) {
            boolean[] usedColors = new boolean[graph.getVertices()];
            
            // Mark colors used by neighbors
            for (int neighbor : graph.getNeighbors(vertex)) {
                if (colors[neighbor] != -1) {
                    usedColors[colors[neighbor]] = true;
                }
            }
            
            // Find first available color
            int color = 0;
            while (color < graph.getVertices() && usedColors[color]) {
                color++;
            }
            
            colors[vertex] = color;
            maxColors = Math.max(maxColors, color + 1);
        }
        
        return maxColors;
    }
    
    // Problem 10: Detect negative cycle using Bellman-Ford
    public static boolean hasNegativeCycle(WeightedGraph graph, int source) {
        Map<Integer, Integer> distances = graph.bellmanFord(source);
        return distances == null; // bellmanFord returns null if negative cycle exists
    }
    
    // Utility method to print solutions
    public static void printConnectedComponents(List<List<Integer>> components) {
        System.out.println("Connected Components:");
        for (int i = 0; i < components.size(); i++) {
            System.out.println("Component " + (i + 1) + ": " + components.get(i));
        }
    }
    
    public static void printBridges(List<int[]> bridges) {
        System.out.println("Bridges in the graph:");
        for (int[] bridge : bridges) {
            System.out.println("Bridge: " + bridge[0] + " - " + bridge[1]);
        }
    }
    
    public static void printArticulationPoints(List<Integer> points) {
        System.out.println("Articulation Points: " + points);
    }
}