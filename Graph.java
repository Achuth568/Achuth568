import java.util.*;

/**
 * Graph Data Structure Implementation using Adjacency List
 * Supports both directed and undirected graphs
 */
public class Graph {
    private int vertices;
    private boolean isDirected;
    private Map<Integer, List<Integer>> adjacencyList;
    
    // Constructor
    public Graph(int vertices, boolean isDirected) {
        this.vertices = vertices;
        this.isDirected = isDirected;
        this.adjacencyList = new HashMap<>();
        
        // Initialize adjacency list for all vertices
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }
    
    // Add edge between two vertices
    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
        
        // If undirected, add edge in both directions
        if (!isDirected) {
            adjacencyList.get(destination).add(source);
        }
    }
    
    // Remove edge between two vertices
    public void removeEdge(int source, int destination) {
        adjacencyList.get(source).remove(Integer.valueOf(destination));
        
        if (!isDirected) {
            adjacencyList.get(destination).remove(Integer.valueOf(source));
        }
    }
    
    // Get neighbors of a vertex
    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }
    
    // Check if edge exists
    public boolean hasEdge(int source, int destination) {
        return adjacencyList.get(source).contains(destination);
    }
    
    // Get number of vertices
    public int getVertices() {
        return vertices;
    }
    
    // Print the graph
    public void printGraph() {
        System.out.println("Graph representation:");
        for (int i = 0; i < vertices; i++) {
            System.out.print("Vertex " + i + ": ");
            for (int neighbor : adjacencyList.get(i)) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }
    
    // Breadth-First Search (BFS)
    public void bfs(int startVertex) {
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[startVertex] = true;
        queue.offer(startVertex);
        
        System.out.print("BFS traversal starting from vertex " + startVertex + ": ");
        
        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();
            System.out.print(currentVertex + " ");
            
            // Visit all unvisited neighbors
            for (int neighbor : adjacencyList.get(currentVertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        System.out.println();
    }
    
    // Depth-First Search (DFS) - Iterative
    public void dfsIterative(int startVertex) {
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        
        stack.push(startVertex);
        System.out.print("DFS (Iterative) traversal starting from vertex " + startVertex + ": ");
        
        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();
            
            if (!visited[currentVertex]) {
                visited[currentVertex] = true;
                System.out.print(currentVertex + " ");
                
                // Add all unvisited neighbors to stack
                for (int neighbor : adjacencyList.get(currentVertex)) {
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        System.out.println();
    }
    
    // Depth-First Search (DFS) - Recursive
    public void dfsRecursive(int startVertex) {
        boolean[] visited = new boolean[vertices];
        System.out.print("DFS (Recursive) traversal starting from vertex " + startVertex + ": ");
        dfsRecursiveHelper(startVertex, visited);
        System.out.println();
    }
    
    private void dfsRecursiveHelper(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(vertex + " ");
        
        // Recursively visit all unvisited neighbors
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsRecursiveHelper(neighbor, visited);
            }
        }
    }
    
    // Check if graph is connected (for undirected graphs)
    public boolean isConnected() {
        if (isDirected) {
            System.out.println("Connectivity check is for undirected graphs only");
            return false;
        }
        
        boolean[] visited = new boolean[vertices];
        dfsRecursiveHelper(0, visited);
        
        // Check if all vertices were visited
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }
    
    // Detect cycle in undirected graph
    public boolean hasCycleUndirected() {
        if (isDirected) {
            System.out.println("This method is for undirected graphs only");
            return false;
        }
        
        boolean[] visited = new boolean[vertices];
        
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                if (hasCycleUndirectedHelper(i, -1, visited)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasCycleUndirectedHelper(int vertex, int parent, boolean[] visited) {
        visited[vertex] = true;
        
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                if (hasCycleUndirectedHelper(neighbor, vertex, visited)) {
                    return true;
                }
            } else if (neighbor != parent) {
                return true; // Back edge found, cycle detected
            }
        }
        return false;
    }
    
    // Detect cycle in directed graph using DFS
    public boolean hasCycleDirected() {
        if (!isDirected) {
            System.out.println("This method is for directed graphs only");
            return false;
        }
        
        boolean[] visited = new boolean[vertices];
        boolean[] recursionStack = new boolean[vertices];
        
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                if (hasCycleDirectedHelper(i, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasCycleDirectedHelper(int vertex, boolean[] visited, boolean[] recursionStack) {
        visited[vertex] = true;
        recursionStack[vertex] = true;
        
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                if (hasCycleDirectedHelper(neighbor, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack[neighbor]) {
                return true; // Back edge in recursion stack, cycle detected
            }
        }
        
        recursionStack[vertex] = false; // Remove from recursion stack
        return false;
    }
    
    // Topological Sort (for directed acyclic graphs)
    public List<Integer> topologicalSort() {
        if (!isDirected) {
            System.out.println("Topological sort is for directed graphs only");
            return new ArrayList<>();
        }
        
        if (hasCycleDirected()) {
            System.out.println("Cannot perform topological sort on a graph with cycles");
            return new ArrayList<>();
        }
        
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                topologicalSortHelper(i, visited, stack);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        
        return result;
    }
    
    private void topologicalSortHelper(int vertex, boolean[] visited, Stack<Integer> stack) {
        visited[vertex] = true;
        
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                topologicalSortHelper(neighbor, visited, stack);
            }
        }
        
        stack.push(vertex);
    }
}