# Graph Data Structures and Algorithms Study Guide in Java

## Table of Contents
1. [Graph Fundamentals](#graph-fundamentals)
2. [Graph Representations](#graph-representations)
3. [Graph Traversal](#graph-traversal)
4. [Shortest Path Algorithms](#shortest-path-algorithms)
5. [Minimum Spanning Tree](#minimum-spanning-tree)
6. [Advanced Graph Algorithms](#advanced-graph-algorithms)
7. [Problem-Solving Strategies](#problem-solving-strategies)
8. [Common Patterns](#common-patterns)
9. [Practice Problems](#practice-problems)
10. [Interview Tips](#interview-tips)

## Graph Fundamentals

### What is a Graph?
A graph is a data structure consisting of:
- **Vertices (Nodes)**: Points in the graph
- **Edges**: Connections between vertices
- **Directed/Undirected**: Edges may have direction
- **Weighted/Unweighted**: Edges may have weights

### Graph Types
1. **Undirected Graph**: Edges have no direction
2. **Directed Graph (Digraph)**: Edges have direction
3. **Weighted Graph**: Edges have weights/costs
4. **Tree**: Connected acyclic graph
5. **DAG**: Directed Acyclic Graph

## Graph Representations

### 1. Adjacency List
```java
// Space: O(V + E)
List<List<Integer>> adjList = new ArrayList<>();
```

**Pros:**
- Space efficient for sparse graphs
- Fast iteration over neighbors
- Easy to add/remove edges

**Cons:**
- Slower edge existence check: O(V)
- More complex to implement

### 2. Adjacency Matrix
```java
// Space: O(V²)
boolean[][] adjMatrix = new boolean[V][V];
```

**Pros:**
- Fast edge existence check: O(1)
- Simple to implement

**Cons:**
- Space inefficient for sparse graphs
- Slow iteration over neighbors

### 3. Edge List
```java
// Space: O(E)
List<int[]> edges = new ArrayList<>();
```

**Pros:**
- Simple representation
- Good for algorithms that process all edges

**Cons:**
- Slow neighbor lookup
- Not suitable for most graph algorithms

## Graph Traversal

### Breadth-First Search (BFS)
```java
// Time: O(V + E), Space: O(V)
public void bfs(Graph graph, int start) {
    Queue<Integer> queue = new LinkedList<>();
    boolean[] visited = new boolean[graph.getVertices()];
    
    queue.offer(start);
    visited[start] = true;
    
    while (!queue.isEmpty()) {
        int current = queue.poll();
        // Process current vertex
        
        for (int neighbor : graph.getNeighbors(current)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.offer(neighbor);
            }
        }
    }
}
```

**Use Cases:**
- Shortest path in unweighted graphs
- Level-order traversal
- Web crawling
- Social network connections

### Depth-First Search (DFS)
```java
// Time: O(V + E), Space: O(V)
public void dfs(Graph graph, int start) {
    boolean[] visited = new boolean[graph.getVertices()];
    dfsUtil(graph, start, visited);
}

private void dfsUtil(Graph graph, int vertex, boolean[] visited) {
    visited[vertex] = true;
    // Process vertex
    
    for (int neighbor : graph.getNeighbors(vertex)) {
        if (!visited[neighbor]) {
            dfsUtil(graph, neighbor, visited);
        }
    }
}
```

**Use Cases:**
- Topological sorting
- Cycle detection
- Connected components
- Maze solving

## Shortest Path Algorithms

### Dijkstra's Algorithm
```java
// Time: O((V + E) log V), Space: O(V)
public int[] dijkstra(WeightedGraph graph, int start) {
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
    int[] distances = new int[V];
    Arrays.fill(distances, Integer.MAX_VALUE);
    
    pq.offer(new int[]{start, 0});
    distances[start] = 0;
    
    while (!pq.isEmpty()) {
        int[] current = pq.poll();
        int vertex = current[0];
        int distance = current[1];
        
        if (distance > distances[vertex]) continue;
        
        for (Edge edge : graph.getNeighbors(vertex)) {
            int newDist = distances[vertex] + edge.weight;
            if (newDist < distances[edge.destination]) {
                distances[edge.destination] = newDist;
                pq.offer(new int[]{edge.destination, newDist});
            }
        }
    }
    return distances;
}
```

**Key Points:**
- Works only with positive weights
- Uses Priority Queue for efficiency
- Guarantees shortest path

### Bellman-Ford Algorithm
```java
// Time: O(VE), Space: O(V)
public int[] bellmanFord(WeightedGraph graph, int start) {
    int[] distances = new int[V];
    Arrays.fill(distances, Integer.MAX_VALUE);
    distances[start] = 0;
    
    // Relax all edges V-1 times
    for (int i = 0; i < V - 1; i++) {
        for (Edge edge : allEdges) {
            if (distances[edge.src] != Integer.MAX_VALUE && 
                distances[edge.src] + edge.weight < distances[edge.dest]) {
                distances[edge.dest] = distances[edge.src] + edge.weight;
            }
        }
    }
    
    // Check for negative cycles
    for (Edge edge : allEdges) {
        if (distances[edge.src] != Integer.MAX_VALUE && 
            distances[edge.src] + edge.weight < distances[edge.dest]) {
            return null; // Negative cycle detected
        }
    }
    return distances;
}
```

**Key Points:**
- Works with negative weights
- Detects negative cycles
- Slower than Dijkstra's

## Minimum Spanning Tree

### Kruskal's Algorithm
```java
// Time: O(E log E), Space: O(V)
public List<Edge> kruskalMST(List<Edge> edges, int V) {
    Collections.sort(edges);
    DisjointSet ds = new DisjointSet(V);
    List<Edge> result = new ArrayList<>();
    
    for (Edge edge : edges) {
        if (ds.find(edge.src) != ds.find(edge.dest)) {
            result.add(edge);
            ds.union(edge.src, edge.dest);
        }
    }
    return result;
}
```

### Prim's Algorithm
```java
// Time: O(E log V), Space: O(V)
public List<Edge> primMST(WeightedGraph graph, int start) {
    PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> a.weight - b.weight);
    boolean[] visited = new boolean[V];
    List<Edge> mst = new ArrayList<>();
    
    visited[start] = true;
    for (Edge edge : graph.getNeighbors(start)) {
        pq.offer(edge);
    }
    
    while (!pq.isEmpty() && mst.size() < V - 1) {
        Edge edge = pq.poll();
        if (visited[edge.destination]) continue;
        
        visited[edge.destination] = true;
        mst.add(edge);
        
        for (Edge neighborEdge : graph.getNeighbors(edge.destination)) {
            if (!visited[neighborEdge.destination]) {
                pq.offer(neighborEdge);
            }
        }
    }
    return mst;
}
```

## Advanced Graph Algorithms

### Topological Sorting
```java
// Time: O(V + E), Space: O(V)
public List<Integer> topologicalSort(Graph graph) {
    Stack<Integer> stack = new Stack<>();
    boolean[] visited = new boolean[V];
    
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
```

### Cycle Detection
```java
// Undirected Graph
public boolean hasCycleUndirected(Graph graph) {
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

// Directed Graph
public boolean hasCycleDirected(Graph graph) {
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
```

## Problem-Solving Strategies

### 1. Identify the Problem Type
- **Traversal**: BFS/DFS
- **Shortest Path**: Dijkstra/Bellman-Ford
- **Connectivity**: Union-Find, DFS
- **Cycles**: DFS with visited tracking
- **Topological Order**: DFS with stack

### 2. Choose the Right Data Structure
- **Adjacency List**: Most common, efficient
- **Adjacency Matrix**: Dense graphs, fast edge checks
- **Priority Queue**: Dijkstra's, Prim's
- **Stack**: DFS, Topological sort
- **Queue**: BFS

### 3. Handle Edge Cases
- Empty graphs
- Single node graphs
- Disconnected components
- Cycles
- Negative weights

### 4. Optimization Techniques
- **Early termination**: Stop when target found
- **Visited tracking**: Avoid revisiting nodes
- **Memoization**: Cache results
- **Union-Find**: Efficient connectivity checks

## Common Patterns

### 1. Grid Problems
```java
// 4-directional movement
int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

// 8-directional movement
int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
```

### 2. BFS with Distance Tracking
```java
Queue<int[]> queue = new LinkedList<>();
queue.offer(new int[]{startRow, startCol, 0}); // {row, col, distance}
```

### 3. DFS with Backtracking
```java
public void dfsWithBacktracking(int vertex, List<Integer> path) {
    path.add(vertex);
    // Process current state
    
    for (int neighbor : graph.getNeighbors(vertex)) {
        dfsWithBacktracking(neighbor, path);
    }
    
    path.remove(path.size() - 1); // backtrack
}
```

### 4. Union-Find Template
```java
class DisjointSet {
    int[] parent, rank;
    
    DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    
    void union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX == rootY) return;
        if (rank[rootX] < rank[rootY]) parent[rootX] = rootY;
        else if (rank[rootX] > rank[rootY]) parent[rootY] = rootX;
        else { parent[rootY] = rootX; rank[rootX]++; }
    }
}
```

## Practice Problems

### Easy Level
1. **Number of Islands** - DFS/BFS on grid
2. **Flood Fill** - DFS on grid
3. **Graph Valid Tree** - Union-Find or DFS
4. **Clone Graph** - DFS with HashMap

### Medium Level
1. **Course Schedule** - Topological sort
2. **Redundant Connection** - Union-Find
3. **All Paths from Source to Target** - DFS with backtracking
4. **Pacific Atlantic Water Flow** - DFS from boundaries
5. **Shortest Path in Binary Matrix** - BFS

### Hard Level
1. **Word Ladder** - BFS with word transformation
2. **Alien Dictionary** - Topological sort
3. **Reconstruct Itinerary** - Hierholzer's algorithm
4. **Minimum Height Trees** - BFS from leaves
5. **Critical Connections** - Tarjan's algorithm

## Interview Tips

### 1. Clarify the Problem
- Ask about graph type (directed/undirected)
- Clarify edge weights (positive/negative)
- Understand input format
- Identify constraints

### 2. Start with Brute Force
- Always start with a simple solution
- Explain your thought process
- Then optimize step by step

### 3. Draw Examples
- Visualize the graph
- Trace through your algorithm
- Handle edge cases

### 4. Discuss Trade-offs
- Time vs Space complexity
- Different approaches
- When to use each algorithm

### 5. Common Mistakes to Avoid
- Not handling cycles
- Forgetting visited tracking
- Incorrect BFS/DFS implementation
- Not considering disconnected components

### 6. Optimization Strategies
- **Bidirectional BFS**: For shortest path problems
- **A* Algorithm**: For pathfinding with heuristics
- **Tarjan's Algorithm**: For finding strongly connected components
- **Hierholzer's Algorithm**: For Eulerian paths

## Time Complexity Cheat Sheet

| Algorithm | Time Complexity | Space Complexity | Use Case |
|-----------|----------------|------------------|----------|
| BFS | O(V + E) | O(V) | Shortest path (unweighted) |
| DFS | O(V + E) | O(V) | Connectivity, cycles |
| Dijkstra's | O((V + E) log V) | O(V) | Shortest path (positive weights) |
| Bellman-Ford | O(VE) | O(V) | Shortest path (negative weights) |
| Kruskal's MST | O(E log E) | O(V) | Minimum spanning tree |
| Prim's MST | O(E log V) | O(V) | Minimum spanning tree |
| Topological Sort | O(V + E) | O(V) | DAG ordering |
| Union-Find | O(α(n)) | O(n) | Connectivity |

## Space Complexity Considerations

1. **Adjacency List**: O(V + E)
2. **Adjacency Matrix**: O(V²)
3. **Visited Array**: O(V)
4. **Queue/Stack**: O(V) in worst case
5. **Priority Queue**: O(V)
6. **Recursion Stack**: O(V) for DFS

## Key Takeaways

1. **Graph representation choice** depends on graph density and operations needed
2. **BFS** for shortest path in unweighted graphs
3. **DFS** for connectivity, cycles, and topological sorting
4. **Dijkstra's** for shortest path with positive weights
5. **Union-Find** for connectivity problems
6. **Always consider edge cases** and disconnected components
7. **Practice pattern recognition** for common graph problems
8. **Understand trade-offs** between different algorithms

Remember: Practice is key! Work through many graph problems to build intuition and pattern recognition skills.