package Graph.Dijkstra;

import java.util.*;

class Edge {
    int target;
    int weight;

    public Edge(int target, int weight) {
        this.target = target;
        this.weight = weight;
    }
}

public class Shortpath {
    private Map<Integer, List<Edge>> graph;

    public Shortpath() {
        graph = new HashMap<>();
    }

    public void addEdge(int source, int target, int weight) {// create the graph
        graph.putIfAbsent(source, new ArrayList<>());
        graph.get(source).add(new Edge(target, weight));
    }

    public Map<Integer, Integer> dijkstra(int start) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(arr -> arr[0]));// this queue saves the
                                                                                              // nodes to be dealt with
                                                                                              // in a sequence of
                                                                                              // distance(from near to
                                                                                              // far)
        pq.offer(new int[] { 0, start });
        Map<Integer, Integer> distance = new HashMap<>();// this map saves the distance from the start node to all other
                                                         // nodes
        distance.put(start, 0);
        Map<Integer, Integer> previous = new HashMap<>();// this map saves the previous node of nodes which are on the
                                                         // shortest path
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentDistance = current[0];
            int currentNode = current[1];
            if (currentDistance > distance.getOrDefault(currentNode, Integer.MAX_VALUE)) {// update the distance to the
                                                                                          // current node
                continue;
            }
            for (Edge edge : graph.getOrDefault(currentNode, new ArrayList<>())) {// pop in all the edges of the current
                                                                                  // node in the queue where they are
                                                                                  // going to be dealt with
                int newDistance = currentDistance + edge.weight;
                if (newDistance < distance.getOrDefault(edge.target, Integer.MAX_VALUE)) {
                    distance.put(edge.target, newDistance);
                    pq.offer(new int[] { newDistance, edge.target });
                    previous.put(edge.target, currentNode);
                }

            }
        }
        System.out.println("The shortpath from node " + start + " to other nodes are：");
        for (Map.Entry<Integer, Integer> entry : distance.entrySet()) {
            int node = entry.getKey();
            int dist = entry.getValue();
            System.out.print("The shortpath to node " + node + " is：" + dist + "，the path is：");
            printPath(previous, start, node);
            System.out.println();
        }
        return distance;
    }

    private void printPath(Map<Integer, Integer> previous, int start, int end) {
        List<Integer> path = new ArrayList<>();
        int at = end;
        while (at != start && previous.containsKey(at)) {
            path.add(at);
            at = previous.get(at);
        }
        path.add(start);
        Collections.reverse(path);
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
    }

    public static void main(String[] args) {
        Shortpath graph = new Shortpath();
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 3, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 5);
        graph.addEdge(3, 4, 3);
        graph.addEdge(4, 5, 1);
        graph.addEdge(2, 5, 8);
        graph.addEdge(4, 6, 8);
        int start = 0;
        graph.dijkstra(start);
    }
}