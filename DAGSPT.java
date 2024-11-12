package Graph.DirectedAcyclicgraphShortestPathTree;

import java.util.*;

class Node {
    Map<Node, Edge> childrenEdges = new HashMap<>();
    boolean ancestor = true;
    String id;

    public Node(String id) {
        this.id = id;
    }

    public int getWeightTo(Node node) {
        Edge edge = this.childrenEdges.get(node);
        return edge != null ? edge.weight : Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return this.id;
    }
}

class Edge {
    Node father;
    Node children;
    int weight;

    Edge(Node father, Node children, int weight) {
        this.father = father;
        this.children = children;
        this.father.childrenEdges.put(children, this);
        this.weight = weight;
        this.children.ancestor = false;
    }
}

public class DAGSPT {
    static void DFS(Node root, ArrayList<Node> queue) {
        if (!root.childrenEdges.isEmpty()) {
            for (Node child : root.childrenEdges.keySet()) {
                DFS(child, queue);
            }
        }
        queue.add(root);
    }

    static ArrayList<Node> DFSsort(Node[] nodes) {
        ArrayList<Node> queue = new ArrayList<>();
        ArrayList<Node> ancestors = new ArrayList<>();
        for (Node node : nodes) {
            if (node.ancestor) {
                ancestors.add(node);
            }
        }
        for (Node node : ancestors) {
            DFS(node, queue);
        }
        Collections.reverse(queue);
        return queue;
    }

    static void ShortestPathTree(ArrayList<Node> queue, Node root, Node target) {
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> parent = new HashMap<>();
        distance.put(root, 0);
        for (Node node : queue) {
            for (Node child : node.childrenEdges.keySet()) {
                int newDistance = distance.get(node) + node.getWeightTo(child);
                if (newDistance < distance.getOrDefault(child, Integer.MAX_VALUE)) {
                    distance.put(child, newDistance);
                    parent.put(child, node);
                }
            }
        }
        System.out.println("The shortest distance to Node " + target + " is " + distance.get(target));
        System.out.println("The shortest path is ");
        Node current = target;
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        for (int i = 0; i < path.size() - 1; i++) {
            Node from = path.get(i);
            Node to = path.get(i + 1);
            System.out.println(from + " -> " + to + ": " + from.getWeightTo(to));
        }
    }

    public static void main(String[] args) {
        Node[] nodes = new Node[8];
        for (int i = 0; i < 8; i++) {
            nodes[i] = new Node(String.valueOf(i));
        }
        new Edge(nodes[0], nodes[1], 3);
        new Edge(nodes[0], nodes[2], -2);
        new Edge(nodes[1], nodes[3], 4);
        new Edge(nodes[1], nodes[4], -1);
        new Edge(nodes[2], nodes[3], 8);
        new Edge(nodes[2], nodes[5], 2);
        new Edge(nodes[3], nodes[6], 5);
        new Edge(nodes[4], nodes[6], 2);
        new Edge(nodes[5], nodes[6], -3);
        new Edge(nodes[6], nodes[7], 4);
        ShortestPathTree(DFSsort(nodes), nodes[0], nodes[4]);
    }
}
