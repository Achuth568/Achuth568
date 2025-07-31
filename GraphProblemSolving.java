import java.util.*;

/**
 * Practical Graph Problem Solving Guide in Java
 * 
 * This file contains solutions to common graph problems with:
 * - Problem description
 * - Approach/Algorithm used
 * - Time and Space complexity
 * - Step-by-step solution
 */

public class GraphProblemSolving {
    
    // ==================== PROBLEM 1: NUMBER OF ISLANDS ====================
    
    /**
     * Problem: Given a 2D grid of '1's (land) and '0's (water), count the number of islands.
     * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
     * 
     * Approach: DFS or BFS to explore connected land cells
     * Time Complexity: O(m*n) where m and n are grid dimensions
     * Space Complexity: O(m*n) for recursion stack
     */
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfsIslands(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private static void dfsIslands(char[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;
        
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] == '0') {
            return;
        }
        
        grid[i][j] = '0'; // mark as visited
        
        // explore all 4 directions
        dfsIslands(grid, i + 1, j);
        dfsIslands(grid, i - 1, j);
        dfsIslands(grid, i, j + 1);
        dfsIslands(grid, i, j - 1);
    }
    
    // ==================== PROBLEM 2: COURSE SCHEDULE ====================
    
    /**
     * Problem: Given the total number of courses and a list of prerequisite pairs,
     * determine if it's possible to finish all courses.
     * 
     * Approach: Detect cycle in directed graph using DFS
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] prereq : prerequisites) {
            graph.get(prereq[1]).add(prereq[0]);
        }
        
        // Check for cycles
        boolean[] visited = new boolean[numCourses];
        boolean[] recStack = new boolean[numCourses];
        
        for (int i = 0; i < numCourses; i++) {
            if (!visited[i]) {
                if (hasCycle(graph, i, visited, recStack)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private static boolean hasCycle(List<List<Integer>> graph, int vertex, boolean[] visited, boolean[] recStack) {
        visited[vertex] = true;
        recStack[vertex] = true;
        
        for (int neighbor : graph.get(vertex)) {
            if (!visited[neighbor]) {
                if (hasCycle(graph, neighbor, visited, recStack)) {
                    return true;
                }
            } else if (recStack[neighbor]) {
                return true;
            }
        }
        
        recStack[vertex] = false;
        return false;
    }
    
    // ==================== PROBLEM 3: SHORTEST PATH IN BINARY MATRIX ====================
    
    /**
     * Problem: Given an n x n binary matrix grid, return the length of the shortest clear path
     * from top-left to bottom-right. A clear path is a path from top-left to bottom-right
     * consisting only of 0s.
     * 
     * Approach: BFS (guarantees shortest path in unweighted graph)
     * Time Complexity: O(n^2)
     * Space Complexity: O(n^2)
     */
    public static int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0] == 1) return -1;
        
        int n = grid.length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[n][n];
        
        queue.offer(new int[]{0, 0, 1}); // {row, col, distance}
        visited[0][0] = true;
        
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int distance = current[2];
            
            if (row == n - 1 && col == n - 1) {
                return distance;
            }
            
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n && 
                    grid[newRow][newCol] == 0 && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol, distance + 1});
                }
            }
        }
        
        return -1;
    }
    
    // ==================== PROBLEM 4: ALL PATHS FROM SOURCE TO TARGET ====================
    
    /**
     * Problem: Given a directed acyclic graph (DAG) of n nodes labeled from 0 to n-1,
     * find all possible paths from node 0 to node n-1.
     * 
     * Approach: DFS with backtracking
     * Time Complexity: O(2^V * V) in worst case
     * Space Complexity: O(V) for recursion stack
     */
    public static List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        path.add(0);
        dfsAllPaths(graph, 0, graph.length - 1, path, result);
        return result;
    }
    
    private static void dfsAllPaths(int[][] graph, int current, int target, 
                                   List<Integer> path, List<List<Integer>> result) {
        if (current == target) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int neighbor : graph[current]) {
            path.add(neighbor);
            dfsAllPaths(graph, neighbor, target, path, result);
            path.remove(path.size() - 1); // backtrack
        }
    }
    
    // ==================== PROBLEM 5: REDUNDANT CONNECTION ====================
    
    /**
     * Problem: Given a graph that started as a tree with n nodes labeled from 1 to n,
     * with one additional edge added. The added edge has two different vertices chosen
     * from 1 to n, and was not an edge that already existed. Return an edge that can
     * be removed so that the resulting graph is a tree.
     * 
     * Approach: Union-Find (Disjoint Set)
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1];
        int[] rank = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            
            if (!union(parent, rank, u, v)) {
                return edge;
            }
        }
        
        return new int[0];
    }
    
    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }
    
    private static boolean union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        
        if (rootX == rootY) return false;
        
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        return true;
    }
    
    // ==================== PROBLEM 6: GRAPH VALID TREE ====================
    
    /**
     * Problem: Given n nodes labeled from 0 to n-1 and a list of undirected edges,
     * write a function to check whether these edges make up a valid tree.
     * 
     * Approach: Check if graph is connected and has no cycles
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public static boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1) return false; // tree must have exactly n-1 edges
        
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        // Check if connected using DFS
        boolean[] visited = new boolean[n];
        dfsValidTree(graph, 0, visited);
        
        for (boolean v : visited) {
            if (!v) return false; // not connected
        }
        
        return true;
    }
    
    private static void dfsValidTree(List<List<Integer>> graph, int vertex, boolean[] visited) {
        visited[vertex] = true;
        
        for (int neighbor : graph.get(vertex)) {
            if (!visited[neighbor]) {
                dfsValidTree(graph, neighbor, visited);
            }
        }
    }
    
    // ==================== PROBLEM 7: CLONE GRAPH ====================
    
    /**
     * Problem: Given a reference of a node in a connected undirected graph,
     * return a deep copy (clone) of the graph.
     * 
     * Approach: DFS with HashMap to track visited nodes
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    static class Node {
        public int val;
        public List<Node> neighbors;
        
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }
    
    public static Node cloneGraph(Node node) {
        if (node == null) return null;
        
        Map<Node, Node> visited = new HashMap<>();
        return dfsClone(node, visited);
    }
    
    private static Node dfsClone(Node node, Map<Node, Node> visited) {
        if (visited.containsKey(node)) {
            return visited.get(node);
        }
        
        Node clone = new Node(node.val);
        visited.put(node, clone);
        
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(dfsClone(neighbor, visited));
        }
        
        return clone;
    }
    
    // ==================== PROBLEM 8: PACIFIC ATLANTIC WATER FLOW ====================
    
    /**
     * Problem: Given an m x n matrix of non-negative integers representing the height
     * of each unit cell in a continent, find the list of grid coordinates where water
     * can flow to both the Pacific and Atlantic oceans.
     * 
     * Approach: DFS from ocean boundaries
     * Time Complexity: O(m*n)
     * Space Complexity: O(m*n)
     */
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        if (heights == null || heights.length == 0) return result;
        
        int m = heights.length;
        int n = heights[0].length;
        
        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];
        
        // DFS from Pacific boundary (top and left)
        for (int i = 0; i < m; i++) {
            dfsPacificAtlantic(heights, i, 0, pacific, Integer.MIN_VALUE);
        }
        for (int j = 0; j < n; j++) {
            dfsPacificAtlantic(heights, 0, j, pacific, Integer.MIN_VALUE);
        }
        
        // DFS from Atlantic boundary (bottom and right)
        for (int i = 0; i < m; i++) {
            dfsPacificAtlantic(heights, i, n - 1, atlantic, Integer.MIN_VALUE);
        }
        for (int j = 0; j < n; j++) {
            dfsPacificAtlantic(heights, m - 1, j, atlantic, Integer.MIN_VALUE);
        }
        
        // Find cells that can reach both oceans
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacific[i][j] && atlantic[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }
        
        return result;
    }
    
    private static void dfsPacificAtlantic(int[][] heights, int i, int j, boolean[][] ocean, int prevHeight) {
        int m = heights.length;
        int n = heights[0].length;
        
        if (i < 0 || i >= m || j < 0 || j >= n || ocean[i][j] || heights[i][j] < prevHeight) {
            return;
        }
        
        ocean[i][j] = true;
        
        dfsPacificAtlantic(heights, i + 1, j, ocean, heights[i][j]);
        dfsPacificAtlantic(heights, i - 1, j, ocean, heights[i][j]);
        dfsPacificAtlantic(heights, i, j + 1, ocean, heights[i][j]);
        dfsPacificAtlantic(heights, i, j - 1, ocean, heights[i][j]);
    }
    
    // ==================== MAIN METHOD WITH TESTING ====================
    
    public static void main(String[] args) {
        System.out.println("=== GRAPH PROBLEM SOLVING EXAMPLES ===\n");
        
        // Test 1: Number of Islands
        System.out.println("1. NUMBER OF ISLANDS");
        char[][] grid = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        System.out.println("Number of islands: " + numIslands(grid));
        
        // Test 2: Course Schedule
        System.out.println("\n2. COURSE SCHEDULE");
        int numCourses = 4;
        int[][] prerequisites = {{1,0}, {2,0}, {3,1}, {3,2}};
        System.out.println("Can finish courses: " + canFinish(numCourses, prerequisites));
        
        // Test 3: Shortest Path in Binary Matrix
        System.out.println("\n3. SHORTEST PATH IN BINARY MATRIX");
        int[][] binaryMatrix = {
            {0,0,0},
            {1,1,0},
            {1,1,0}
        };
        System.out.println("Shortest path length: " + shortestPathBinaryMatrix(binaryMatrix));
        
        // Test 4: All Paths from Source to Target
        System.out.println("\n4. ALL PATHS FROM SOURCE TO TARGET");
        int[][] graph = {{1,2}, {3}, {3}, {}};
        List<List<Integer>> paths = allPathsSourceTarget(graph);
        System.out.println("All paths: " + paths);
        
        // Test 5: Redundant Connection
        System.out.println("\n5. REDUNDANT CONNECTION");
        int[][] edges = {{1,2}, {1,3}, {2,3}};
        int[] redundant = findRedundantConnection(edges);
        System.out.println("Redundant edge: [" + redundant[0] + ", " + redundant[1] + "]");
        
        // Test 6: Graph Valid Tree
        System.out.println("\n6. GRAPH VALID TREE");
        int n = 5;
        int[][] treeEdges = {{0,1}, {0,2}, {0,3}, {1,4}};
        System.out.println("Is valid tree: " + validTree(n, treeEdges));
        
        // Test 7: Pacific Atlantic Water Flow
        System.out.println("\n7. PACIFIC ATLANTIC WATER FLOW");
        int[][] heights = {
            {1,2,2,3,5},
            {3,2,3,4,4},
            {2,4,5,3,1},
            {6,7,1,4,5},
            {5,1,1,2,4}
        };
        List<List<Integer>> flowPoints = pacificAtlantic(heights);
        System.out.println("Water flow points: " + flowPoints);
        
        System.out.println("\n=== PROBLEM SOLVING COMPLETE ===");
    }
}