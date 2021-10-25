import java.net.URL;

public class Main {

    public static void main(String[] args) {
        //Load graph-data from file
        GraphTable g1 = new GraphTable();
        GraphTable g2 = new GraphTable();
        GraphTable g3 = new GraphTable();
        try {
            g1.newGraphTable(new URL("http://www.iie.ntnu.no/fag/_alg/v-graf/flytgraf1").openStream());
            g2.newGraphTable(new URL("http://www.iie.ntnu.no/fag/_alg/v-graf/flytgraf2").openStream());
            g3.newGraphTable(new URL("http://www.iie.ntnu.no/fag/_alg/v-graf/flytgraf3").openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Flytgraf 1:");
        g1.edmondsKarp(g1.nodes[0], g1.nodes[g1.nodeCount-1]);
        System.out.println();
        System.out.println("Flytgraf 2:");
        g2.edmondsKarp(g2.nodes[0], g2.nodes[g2.nodeCount-1]);
        System.out.println();
        System.out.println("Flytgraf 3:");
        g3.edmondsKarp(g3.nodes[0], g3.nodes[g3.nodeCount-1]);
    }
}
