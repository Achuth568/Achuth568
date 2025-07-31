import java.util.*;

/**
 * Graph DSA Demo - Comprehensive examples of graph algorithms and problems
 * This class demonstrates how to use all the graph implementations and solve common problems
 */
public class GraphDemo {
    
    public static void main(String[] args) {
        System.out.println("=== GRAPH DATA STRUCTURES AND ALGORITHMS DEMO ===\n");
        
        // Demo 1: Basic Graph Operations
        demonstrateBasicGraphOperations();
        
        // Demo 2: Graph Traversals
        demonstrateGraphTraversals();
        
        // Demo 3: Weighted Graph and Shortest Paths
        demonstrateWeightedGraphAlgorithms();
        
        // Demo 4: Common Graph Problems
        demonstrateGraphProblems();
        
        // Demo 5: Advanced Graph Algorithms
        demonstrateAdvancedAlgorithms();
        
        System.out.println("\n=== DEMO COMPLETED ===");
    }
    
    private static void demonstrateBasicGraphOperations() {
        System.out.println("1. BASIC GRAPH OPERATIONS");
        System.out.println("========================");
        
        // Create an undirected graph
        Graph undirectedGraph = new Graph(6, false);
        
        // Add edges
        undirectedGraph.addEdge(0, 1);
        undirectedGraph.addEdge(0, 2);
        undirectedGraph.addEdge(1, 3);
        undirectedGraph.addEdge(2, 4);
        undirectedGraph.addEdge(3, 4);
        undirectedGraph.addEdge(4, 5);
        
        System.out.println("Undirected Graph:");
        undirectedGraph.printGraph();
        
        System.out.println("\nChecking connectivity:");
        System.out.println("Is graph connected? " + undirectedGraph.isConnected());
        System.out.println("Has cycle? " + undirectedGraph.hasCycleUndirected());
        
        // Create a directed graph
        Graph directedGraph = new Graph(4, true);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(1, 2);
        directedGraph.addEdge(2, 3);
        directedGraph.addEdge(3, 1); // Creates a cycle
        
        System.out.println("\nDirected Graph:");
        directedGraph.printGraph();
        System.out.println("Has cycle? " + directedGraph.hasCycleDirected());
        
        // Topological sort on DAG
        Graph dag = new Graph(6, true);
        dag.addEdge(5, 2);
        dag.addEdge(5, 0);
        dag.addEdge(4, 0);
        dag.addEdge(4, 1);
        dag.addEdge(2, 3);
        dag.addEdge(3, 1);
        
        System.out.println("\nDirected Acyclic Graph (DAG):");
        dag.printGraph();
        System.out.println("Topological Sort: " + dag.topologicalSort());
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
    
    private static void demonstrateGraphTraversals() {
        System.out.println("2. GRAPH TRAVERSAL ALGORITHMS");
        System.out.println("=============================");
        
        // Create a sample graph for traversals
        Graph graph = new Graph(7, false);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(2, 6);
        
        System.out.println("Sample Graph for Traversals:");
        graph.printGraph();
        
        System.out.println("\nTraversal Results:");
        graph.bfs(0);
        graph.dfsIterative(0);
        graph.dfsRecursive(0);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
    
    private static void demonstrateWeightedGraphAlgorithms() {
        System.out.println("3. WEIGHTED GRAPH AND SHORTEST PATH ALGORITHMS");
        System.out.println("==============================================");
        
        // Create a weighted graph
        WeightedGraph weightedGraph = new WeightedGraph(5, false);
        weightedGraph.addEdge(0, 1, 2);
        weightedGraph.addEdge(0, 3, 6);
        weightedGraph.addEdge(1, 2, 3);
        weightedGraph.addEdge(1, 3, 8);
        weightedGraph.addEdge(1, 4, 5);
        weightedGraph.addEdge(2, 4, 7);
        weightedGraph.addEdge(3, 4, 9);
        
        System.out.println("Weighted Graph:");
        weightedGraph.printGraph();
        
        // Dijkstra's Algorithm
        System.out.println("\nDijkstra's Algorithm from vertex 0:");
        Map<Integer, Integer> distances = weightedGraph.dijkstra(0);
        for (Map.Entry<Integer, Integer> entry : distances.entrySet()) {
            System.out.println("Distance to vertex " + entry.getKey() + ": " + entry.getValue());
        }
        
        // Shortest path between two vertices
        System.out.println("\nShortest path from 0 to 4:");
        List<Integer> path = weightedGraph.getShortestPath(0, 4);
        System.out.println("Path: " + path);
        
        // Floyd-Warshall Algorithm
        System.out.println("\nFloyd-Warshall Algorithm (All-pairs shortest paths):");
        int[][] allPairsDistances = weightedGraph.floydWarshall();
        weightedGraph.printDistanceMatrix(allPairsDistances);
        
        // Minimum Spanning Tree
        System.out.println("\nMinimum Spanning Tree (Kruskal's Algorithm):");
        List<WeightedGraph.Edge> mst = weightedGraph.kruskalMST();
        int totalWeight = 0;
        for (WeightedGraph.Edge edge : mst) {
            System.out.println("Edge: " + edge);
            totalWeight += edge.weight;
        }
        System.out.println("Total MST weight: " + totalWeight);
        
        // Bellman-Ford Algorithm (with negative weights)
        WeightedGraph graphWithNegativeWeights = new WeightedGraph(4, true);
        graphWithNegativeWeights.addEdge(0, 1, 1);
        graphWithNegativeWeights.addEdge(1, 2, -3);
        graphWithNegativeWeights.addEdge(2, 3, 2);
        graphWithNegativeWeights.addEdge(3, 1, 1);
        
        System.out.println("\nBellman-Ford Algorithm (handles negative weights):");
        Map<Integer, Integer> bellmanFordDistances = graphWithNegativeWeights.bellmanFord(0);
        if (bellmanFordDistances != null) {
            for (Map.Entry<Integer, Integer> entry : bellmanFordDistances.entrySet()) {
                System.out.println("Distance to vertex " + entry.getKey() + ": " + entry.getValue());
            }
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
    
    private static void demonstrateGraphProblems() {
        System.out.println("4. COMMON GRAPH PROBLEMS");
        System.out.println("========================");
        
        // Problem 1: Connected Components
        Graph disconnectedGraph = new Graph(7, false);
        disconnectedGraph.addEdge(0, 1);
        disconnectedGraph.addEdge(1, 2);
        disconnectedGraph.addEdge(3, 4);
        // Vertex 5 and 6 are isolated
        
        System.out.println("Graph with multiple components:");
        disconnectedGraph.printGraph();
        
        List<List<Integer>> components = GraphProblems.findConnectedComponents(disconnectedGraph);
        GraphProblems.printConnectedComponents(components);
        
        // Problem 2: Path Finding
        System.out.println("\nPath Finding:");
        System.out.println("Path exists between 0 and 2? " + 
                          GraphProblems.hasPath(disconnectedGraph, 0, 2));
        System.out.println("Path exists between 0 and 5? " + 
                          GraphProblems.hasPath(disconnectedGraph, 0, 5));
        System.out.println("Shortest path length from 0 to 2: " + 
                          GraphProblems.shortestPathLength(disconnectedGraph, 0, 2));
        
        // Problem 3: Bipartite Check
        Graph bipartiteGraph = new Graph(4, false);
        bipartiteGraph.addEdge(0, 1);
        bipartiteGraph.addEdge(1, 2);
        bipartiteGraph.addEdge(2, 3);
        bipartiteGraph.addEdge(3, 0);
        
        System.out.println("\nBipartite Graph Check:");
        System.out.println("Is graph bipartite? " + GraphProblems.isBipartite(bipartiteGraph));
        
        // Problem 4: Graph Coloring
        Graph coloringGraph = new Graph(5, false);
        coloringGraph.addEdge(0, 1);
        coloringGraph.addEdge(0, 2);
        coloringGraph.addEdge(1, 2);
        coloringGraph.addEdge(1, 3);
        coloringGraph.addEdge(2, 3);
        coloringGraph.addEdge(3, 4);
        
        System.out.println("\nGraph Coloring:");
        int minColors = GraphProblems.graphColoring(coloringGraph);
        System.out.println("Minimum colors needed: " + minColors);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
    
    private static void demonstrateAdvancedAlgorithms() {
        System.out.println("5. ADVANCED GRAPH ALGORITHMS");
        System.out.println("============================");
        
        // Bridges and Articulation Points
        Graph bridgeGraph = new Graph(7, false);
        bridgeGraph.addEdge(0, 1);
        bridgeGraph.addEdge(1, 2);
        bridgeGraph.addEdge(2, 0);
        bridgeGraph.addEdge(1, 3);
        bridgeGraph.addEdge(3, 4);
        bridgeGraph.addEdge(4, 5);
        bridgeGraph.addEdge(5, 6);
        bridgeGraph.addEdge(6, 4);
        
        System.out.println("Graph for Bridges and Articulation Points:");
        bridgeGraph.printGraph();
        
        List<int[]> bridges = GraphProblems.findBridges(bridgeGraph);
        GraphProblems.printBridges(bridges);
        
        List<Integer> articulationPoints = GraphProblems.findArticulationPoints(bridgeGraph);
        GraphProblems.printArticulationPoints(articulationPoints);
        
        // Strongly Connected Components
        Graph sccGraph = new Graph(5, true);
        sccGraph.addEdge(1, 0);
        sccGraph.addEdge(0, 2);
        sccGraph.addEdge(2, 1);
        sccGraph.addEdge(0, 3);
        sccGraph.addEdge(3, 4);
        
        System.out.println("\nDirected Graph for Strongly Connected Components:");
        sccGraph.printGraph();
        
        List<List<Integer>> sccs = GraphProblems.findStronglyConnectedComponents(sccGraph);
        System.out.println("Strongly Connected Components:");
        for (int i = 0; i < sccs.size(); i++) {
            System.out.println("SCC " + (i + 1) + ": " + sccs.get(i));
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
    
    // Additional utility methods for demonstration
    public static void demonstrateSpecificProblem(String problemType) {
        System.out.println("SPECIFIC PROBLEM DEMONSTRATION: " + problemType);
        System.out.println("=".repeat(40));
        
        switch (problemType.toLowerCase()) {
            case "shortest_path":
                demonstrateShortestPathProblem();
                break;
            case "cycle_detection":
                demonstrateCycleDetection();
                break;
            case "topological_sort":
                demonstrateTopologicalSort();
                break;
            case "mst":
                demonstrateMST();
                break;
            default:
                System.out.println("Unknown problem type: " + problemType);
        }
    }
    
    private static void demonstrateShortestPathProblem() {
        // Classic shortest path problem
        WeightedGraph graph = new WeightedGraph(6, false);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 2);
        graph.addEdge(4, 5, 6);
        
        System.out.println("Finding shortest path from 0 to 5:");
        List<Integer> path = graph.getShortestPath(0, 5);
        System.out.println("Shortest path: " + path);
        
        Map<Integer, Integer> distances = graph.dijkstra(0);
        System.out.println("Distance to destination: " + distances.get(5));
    }
    
    private static void demonstrateCycleDetection() {
        // Cycle detection in both directed and undirected graphs
        System.out.println("Cycle Detection Examples:");
        
        Graph undirected = new Graph(4, false);
        undirected.addEdge(0, 1);
        undirected.addEdge(1, 2);
        undirected.addEdge(2, 3);
        undirected.addEdge(3, 0);
        System.out.println("Undirected graph has cycle: " + undirected.hasCycleUndirected());
        
        Graph directed = new Graph(4, true);
        directed.addEdge(0, 1);
        directed.addEdge(1, 2);
        directed.addEdge(2, 3);
        directed.addEdge(3, 1);
        System.out.println("Directed graph has cycle: " + directed.hasCycleDirected());
    }
    
    private static void demonstrateTopologicalSort() {
        // Course prerequisite problem
        System.out.println("Course Prerequisite Problem (Topological Sort):");
        Graph courses = new Graph(6, true);
        
        // Course dependencies: 0->1 means course 0 is prerequisite for course 1
        courses.addEdge(5, 2); // Course 5 -> Course 2
        courses.addEdge(5, 0); // Course 5 -> Course 0
        courses.addEdge(4, 0); // Course 4 -> Course 0
        courses.addEdge(4, 1); // Course 4 -> Course 1
        courses.addEdge(2, 3); // Course 2 -> Course 3
        courses.addEdge(3, 1); // Course 3 -> Course 1
        
        List<Integer> courseOrder = courses.topologicalSort();
        System.out.println("Course completion order: " + courseOrder);
    }
    
    private static void demonstrateMST() {
        // Network connection problem
        System.out.println("Network Connection Problem (Minimum Spanning Tree):");
        WeightedGraph network = new WeightedGraph(6, false);
        
        // Cities with connection costs
        network.addEdge(0, 1, 4);
        network.addEdge(0, 2, 4);
        network.addEdge(1, 2, 2);
        network.addEdge(1, 3, 6);
        network.addEdge(2, 3, 8);
        network.addEdge(3, 4, 9);
        network.addEdge(4, 5, 10);
        network.addEdge(2, 4, 2);
        network.addEdge(2, 5, 1);
        
        List<WeightedGraph.Edge> mst = network.kruskalMST();
        int totalCost = 0;
        System.out.println("Minimum cost network connections:");
        for (WeightedGraph.Edge edge : mst) {
            System.out.println("Connection: " + edge + " (cost: " + edge.weight + ")");
            totalCost += edge.weight;
        }
        System.out.println("Total minimum cost: " + totalCost);
    }
}