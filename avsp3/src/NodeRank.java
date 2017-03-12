import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 *  AVSP_06_Link_Analysis_Slide_48_/_66_Pseudocode
 *  Google PageRank Algorithm (page -> node)
 */
public class NodeRank {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(args.length != 0 ?
                new FileReader(args[0]) : //komp
                new InputStreamReader(System.in)); //sprut

        String[] split = br.readLine().split("\\s");
        double beta = Double.parseDouble(split[1]);

        // Inverted Adjacency node list <dest, [src]>
        Map<Integer, Collection<Integer>> pointsToNode = new HashMap<>();

        int N = Integer.parseInt(split[0]);
        int[] d = new int[N]; //degrees (outgoing links)
        for (int src = 0; src < N; src++) {
            String[] part = br.readLine().split("\\s");
            d[src] = part.length;
            for (String j : part) {
                int dest = Integer.parseInt(j);
                pointsToNode.putIfAbsent(dest, new ArrayList<>());
                pointsToNode.get(dest).add(src);
            }
        }

        int Q = Integer.parseInt(br.readLine());
        int[][] queries = new int[Q][];
        for (int i = 0; i < Q; i++) {
            String[] part = br.readLine().split("\\s");
            int[] row = new int[part.length];
            for (int j = 0; j < part.length; j++) {
                row[j] = Integer.parseInt(part[j]);
            }
            queries[i] = row;
        }
        br.close();

        final int T = 101; // iter
        double[][] r = new double[T][N];
        for (int j = 0; j < N; j++) {
            r[0][j] = 1.0 / N; // t=0
        }
        for (int t = 1; t < T; t++) {
            double S = 0.0;
            for (int j = 0; j < N; j++) {
                if (pointsToNode.containsKey(j)) {
                    for (int i : pointsToNode.get(j)) { // i -> j
                        r[t][j] += beta * (r[t - 1][i] / d[i]);
                    }
                    S += r[t][j];
                }
            }
            for (int j = 0; j < N; j++) {
                r[t][j] += (1.0 - S) / N;
            }
        }
        for (int[] q : queries) { // n t
            System.out.println(String.format(Locale.getDefault(), "%.10f", r[q[1]][q[0]]));
        }
    }
}