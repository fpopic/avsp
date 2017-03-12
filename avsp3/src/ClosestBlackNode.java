import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * AVSP_02_Map_Reduce_Slide_49_/_66_Idea_taken
 * Iterative Message Passing (Graph Processing)
 */
public class ClosestBlackNode {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(args.length != 0 ?
                new FileReader(args[0]) : //komp
                new InputStreamReader(System.in)); //sprut

        String[] split = br.readLine().split("\\s");
        int N = Integer.parseInt(split[0]);
        int E = Integer.parseInt(split[1]);

        Node[] nodes = new Node[N];
        Collection<Node> blackNodes = new ArrayList<>();

        for (int i = 0; i < N; i++) {  // Read Nodes
            boolean isBlack = br.readLine().equals("1");
            Node node = new Node(i, isBlack);
            if (isBlack) blackNodes.add(node);
            nodes[i] = node;
        }

        for (int i = 0; i < E; i++) { // Read Edges
            String part[] = br.readLine().split("\\s");
            Node NodeA = nodes[Integer.parseInt(part[0])];
            Node NodeB = nodes[Integer.parseInt(part[1])];
            NodeA.neighbours.add(NodeB);
            NodeB.neighbours.add(NodeA);
        }
        br.close();

        Queue<Node> queue = new ArrayDeque<>(N); // FIFO
        Set<Integer> visited = new HashSet<>(N);
        queue.addAll(blackNodes);

        while (!queue.isEmpty()) { // BFS from sorted black nodes (water wave)
            Node node = queue.remove();
            visited.add(node.index);
            for (Node neighbour : node.neighbours) {
                if (!visited.contains(neighbour.index)) {
                    int newDistance = node.distance + 1;
                    if (newDistance < neighbour.distance) {
                        neighbour.distance = newDistance;
                        neighbour.blackIndex = node.blackIndex;
                    }
                    queue.add(neighbour);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            if (node.distance <= 10)
                sb.append(node.blackIndex).append(" ").append(node.distance);
            else
                sb.append("-1 -1");
            sb.append(System.lineSeparator());
        }
        System.out.print(sb.toString());
    }

}

class Node {

    int index;
    int blackIndex;
    int distance;
    Collection<Node> neighbours;

    Node(int index, boolean isBlack) {
        this.index = index;
        if (isBlack) {
            this.distance = 0;
            this.blackIndex = index;
        }
        else {
            this.distance = Integer.MAX_VALUE;
            this.blackIndex = -1;
        }
        this.neighbours = new ArrayList<>();
    }
}