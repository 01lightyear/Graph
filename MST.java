package Graph.MinimumSpanningTree;

import java.util.*;

class Edge {
    int target;
    int weight;

    Edge(int target, int weight) {
        this.target = target;
        this.weight = weight;
    }
}

class Sourceedge extends Edge {
    int source;

    Sourceedge(int source, int target, int weight) {
        super(target, weight);
        this.source = source;
    }
}

public class MST {
    private Map<Integer, List<Edge>> graph;

    public MST() {
        graph = new HashMap<>();
    }

    public void addEdge(int node1, int node2, int weight) {// create the graph
        graph.putIfAbsent(node1, new ArrayList<>());
        graph.get(node1).add(new Edge(node2, weight));
        graph.putIfAbsent(node2, new ArrayList<>());
        graph.get(node2).add(new Edge(node1, weight));
    }

    public void PrimMST(int start) {
        if (graph.isEmpty()) {
            System.out.println("The graph is empty");
            return;
        }

        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));
        int totalweight = 0;
        List<String> result = new ArrayList<>();
        Map<Integer, Integer> parent = new HashMap<>();
        parent.put(start, -1);
        pq.offer(new Edge(start, 0));
        while (!pq.isEmpty()) {// this progress looks like the process of Dijkstra, except that it cares about
                               // the distance to the entire tree instead of the starting point
            Edge current = pq.poll();
            if (visited.contains(current.target))
                continue;
            visited.add(current.target);
            totalweight += current.weight;
            if (parent.get(current.target) != -1) {
                result.add(parent.get(current.target) + "--" + current.target + "(weight:" + current.weight + ")");
            }
            for (Edge neighbor : graph.get(current.target)) {
                if (!visited.contains(neighbor.target)) {
                    parent.put(neighbor.target, current.target);
                    pq.offer(neighbor);// here we pass in the distance to the entire tree,not to the starting point
                }
            }
        }
        System.out.println("(Prim)The total weight of the MST is：" + totalweight);
        System.out.println("The edges of the MST are：");
        for (String edge : result) {
            System.out.println(edge);
        }
        System.out.println();
    }

    private int find(int[] parent, int node) {// disjoint set
        if (parent[node] != node) {
            parent[node] = find(parent, parent[node]);
        }
        return parent[node];
    }

    private void union(int[] parent, int[] rank, int node1, int node2) {
        int root1 = find(parent, node1);
        int root2 = find(parent, node2);
        if (root1 != root2) {
            if (rank[root1] > rank[root2]) {
                parent[root2] = root1;
            } else if (rank[root1] < rank[root2]) {
                parent[root1] = root2;
            } else {
                parent[root2] = root1;
                rank[root1]++;
            }
        }
    }

    public void KruskalMST() {
        if (graph.isEmpty()) {
            System.out.println("The graph is empty");
            return;
        }
        List<Sourceedge> edges = new ArrayList<>();
        for (int source : graph.keySet()) {// collect all the edges
            for (Edge edge : graph.get(source)) {
                if (source < edge.target) {
                    edges.add(new Sourceedge(source, edge.target, edge.weight));
                }
            }
        }
        edges.sort(Comparator.comparingInt(edge -> edge.weight));
        int[] parent = new int[graph.size()];
        int[] rank = new int[graph.size()];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
        int totalweight = 0;
        List<String> result = new ArrayList<>();
        for (Sourceedge edge : edges) {
            if (find(parent, edge.source) != find(parent, edge.target)) {// pick the edge if it does not form a cycle
                union(parent, rank, edge.source, edge.target);
                totalweight += edge.weight;
                result.add(edge.source + "--" + edge.target + "(weight:" + edge.weight + ")");
            }
        }
        System.out.println("(Kruskal)The total weight of the MST is：" + totalweight);
        System.out.println("The edges of the MST are：");
        for (String edge : result) {
            System.out.println(edge);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MST mst = new MST();
        mst.addEdge(0, 1, 4);
        mst.addEdge(0, 2, 3);
        mst.addEdge(1, 2, 1);
        mst.addEdge(1, 3, 2);
        mst.addEdge(2, 3, 4);
        mst.PrimMST(0);
        mst.KruskalMST();
    }
}
