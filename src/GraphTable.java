import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

class GraphTable {
    int nodeCount;
    Node[] nodes;
    Edge[][] edgeTable;

    public void newGraphTable(InputStream is) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            nodeCount = Integer.parseInt(st.nextToken());
            int edgeCount = Integer.parseInt(st.nextToken());

            nodes = new Node[nodeCount];
            edgeTable = new Edge[nodeCount][nodeCount];

            for(int i = 0; i < nodeCount; i++) { nodes[i] = new Node(); nodes[i].id = i; }
            for(int i = 0; i < nodeCount; i++) for(int j = 0; j < nodeCount; j++) edgeTable[i][j] = new Edge();


            for(int i = 0; i < edgeCount; i++) {
                st = new StringTokenizer(br.readLine());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                int capacity = Integer.parseInt(st.nextToken());

                Edge e = edgeTable[from][to];
                e.exist = true;
                e.capacity = capacity;
            }
        } catch (Exception e) {
            System.out.println("Unable to read input stream... Reason: " + e.getMessage());
            e.printStackTrace();
        }

        for(int i = 0; i < nodeCount; i++) for(int j = 0; j < nodeCount; j++) {
            Edge e = edgeTable[i][j];
            Edge e2 = edgeTable[j][i];

            if(e.exist && !e2.exist) {
                e2.exist = true;
                e2.capacity = 0;
            }
        }
    }

    public void edmondsKarp(Node start, Node end) {
        int maxFlow = 0;

        System.out.printf("Maximum flow from %s to %s using Edmond-Karp:\n", start.id, end.id);
        System.out.printf("Flow\tTraversing road\n");

        while(true) {
            bfs(start);

            //Break loop if there are no more available connections from start to end.
            if(((Predecessor) end.nodeData).getDistance() == Predecessor.infinity) {
                break;
            }

            boolean capacityAvailable = true;
            int flow = 0;
            Node[] road = new Node[((Predecessor) end.nodeData).getDistance()+1];

            //Find highest possible flow-number for this BFS
            while(capacityAvailable) {
                Node c = end;
                Node p = ((Predecessor) c.nodeData).predecessor;

                int r_i = 0;

                while(p != null) {
                    //Find edges, correct way, and reverse
                    Edge e = edgeTable[p.id][c.id]; //Correct way
                    Edge re = edgeTable[c.id][p.id]; //Reverse edge

                    re.capacity++;
                    e.flow++;
                     if(--e.capacity < 1) {
                         //Unable to traverse this specific road anymore when e.capacity is zero.
                         capacityAvailable = false;
                     }

                     if(r_i < road.length) road[r_i++] = c;

                     c = p;
                     p = ((Predecessor) c.nodeData).predecessor;
                }

                road[r_i] = start;
                flow++;
            }

            Collections.reverse(Arrays.asList(road));

            maxFlow += flow;
            System.out.println(flow + "\t\t" + Arrays.toString(road));
        }

        System.out.println("Maximum flow: " + maxFlow);
    }

    public void bfs(Node s) {
        initPredecessor(s);

        Queue<Node> queue = new Queue<>(nodeCount - 1);
        queue.addToQueue(s);

        while (!queue.empty()) {
            Node n = queue.getNextInQueue();
            int nId = n.id;

            for(int i = 0; i < nodeCount; i++) {
                Edge e = edgeTable[nId][i];

                if(e.exist && e.capacity > 0) {
                    Node to = nodes[i];

                    Predecessor p = (Predecessor)to.nodeData;

                    if(p.distance == Predecessor.infinity) {
                        p.distance = ((Predecessor) n.nodeData).distance + 1;
                        p.predecessor = n;
                        queue.addToQueue(to);
                    }
                }
            }
        }
    }

    private void initPredecessor(Node s) {
        for(Node n : nodes) {
            n.nodeData = new Predecessor();
        }
        ((Predecessor) s.nodeData).distance = 0;
    }

}

class Node {
    Object nodeData;
    int id;

    @Override
    public String toString() {
        return ""+id;
    }
}

class Edge {
    boolean exist;
    int capacity;
    int flow;

    @Override
    public String toString() {
        if(exist) return "x";
        else return " ";
    }
}


//Datastructure for BFS
class Predecessor {
    public static final int infinity = 100000000;

    int distance;
    Node predecessor;

    public int getDistance() {
        return distance;
    }

    public Node findPredecessor() {
        return predecessor;
    }

    public Predecessor() {
        distance = infinity;
    }
}
