package no.ntnu.idi.krisvaa.idatt2101;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class Graph<T> {

    int nodeCount, edgeCount;
    Node<T>[] nodes;

    public Graph(int nodeCount, int edgeCount) {
        nodes = new Node[nodeCount];

        for(int i = 0; i < nodes.length; i++) { nodes[i] = new Node<T>(); nodes[i].id = i; }

        this.nodeCount = nodeCount;
        this.edgeCount = edgeCount;
    }

    public static Graph<String> readFromStream(InputStream is) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int nodeCount = Integer.parseInt(st.nextToken());
            int edgeCount = Integer.parseInt(st.nextToken());

            Graph<String> g = new Graph<>(nodeCount, edgeCount);

            for(int i = 0; i < edgeCount; i++) {
                st = new StringTokenizer(br.readLine());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());

                Edge e = new Edge(g.nodes[to], g.nodes[from].edge1);
                g.nodes[from].edge1 = e;
            }

            return g;
        } catch (Exception e) {
            System.out.println("Unable to read input stream... Reason: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void bfs(Node<?> s) {
        initPredecessor(s);

        Queue<Node<?>> queue = new Queue<>(nodeCount - 1);
        queue.addToQueue(s);

        while (!queue.empty()) {
            Node<?> n = queue.getNextInQueue();

            for(Edge e = n.edge1; e != null; e = e.next) {
                Predecessor p = (Predecessor)e.to.nodeData;

                if(p.distance == Predecessor.infinity) {
                    p.distance = ((Predecessor) n.nodeData).distance + 1;
                    p.predecessor = n;
                    queue.addToQueue(e.to);
                }
            }
        }

    }

    private void initPredecessor(Node<?> s) {
        for(Node n : nodes) {
            n.nodeData = new Predecessor();
        }
        ((Predecessor) s.nodeData).distance = 0;
    }

    private Node dfTopo(Node n, Node l) {
        Topo_lst nd = (Topo_lst) n.nodeData;
        if(nd.found) return l;
        nd.found = true;

        for(Edge e = n.edge1; e != null; e = e.next) {
            l = dfTopo(e.to, l);
        }

        nd.next = l;
        return n;
    }

    public Node topologicalSort() {
        Node l = null;

        for(Node n : nodes) {
            n.nodeData = new Topo_lst();
        }

        for (int i = nodeCount; i-- > 0;) {
            l = dfTopo(nodes[i], l);
        }

        return l;
    }

}

class Node<T> {
    Edge edge1;
    T data;
    Object nodeData;
    int id;
}

class Edge {
     Edge next;
     Node to;

     public Edge(Node<?> n, Edge next) {
         this.to = n;
         this.next = next;
     }
}

//Datastructure for BFS
class Predecessor {
    public static final int infinity = 100000000;

    int distance;
    Node<?> predecessor;

    public int getDistance() {
        return distance;
    }

    public Node<?> findPredecessor() {
        return predecessor;
    }

    public Predecessor() {
        distance = infinity;
    }
}

//Datastructure for topological sorting
class Topo_lst {
    boolean found;
    Node next;
}