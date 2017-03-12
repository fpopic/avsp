import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


class DGIM {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(args.length != 0 ?
                new FileReader(args[0]) : //komp
                new InputStreamReader(System.in)); //sprut

        int N = Integer.parseInt(br.readLine());

        LinkedList<Bucket> buckets = new LinkedList<>();
        Map<Integer, Integer> ofSize = new HashMap<>();

        long T = 0;

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {

            if (!line.startsWith("q")) {

                for (int bit = 0; bit < line.length(); bit += 1, T += 1) {

                    if (!buckets.isEmpty() && T - buckets.peekLast().T > N) {
                        Bucket last = buckets.peekLast();
                        ofSize.put(last.ones, ofSize.get(last.ones) - 1);
                        buckets.removeLast();
                    }

                    if (line.charAt(bit) == '1') {
                        buckets.addFirst(new Bucket(T));
                        ofSize.putIfAbsent(1, 0);
                        ofSize.put(1, ofSize.get(1) + 1);

                        int i = 0;
                        while (i < buckets.size()) {
                            Bucket a = buckets.get(i);
                            if (ofSize.get(a.ones) < 3) break;

                            Bucket b = buckets.get(i + 1);
                            Bucket c = buckets.get(i + 2);
                            b.ones += c.ones;

                            ofSize.putIfAbsent(b.ones, 0);
                            ofSize.put(b.ones, ofSize.get(b.ones) + 1);
                            ofSize.put(a.ones, ofSize.get(a.ones) - 2);

                            buckets.remove(i + 2);

                            i += 1;
                        }
                    }
                }
            }
            else {
                int k = Integer.parseInt(line.split("\\s+")[1]);

                int est = 0, last = 0;
                for (Bucket bucket : buckets) {
                    if (bucket.T < T - 1 - k) break; // -1 zbog T++
                    est += bucket.ones;
                    last = bucket.ones;
                }
                est = est - last + (int) Math.floor(last / 2.0);
                sb.append(est).append(System.lineSeparator());
            }
        }
        br.close();
        System.out.print(sb.toString());
    }

    private static class Bucket {

        final long T;
        int ones;

        Bucket(long T) {
            this.T = T;
            this.ones = 1;
        }

        @Override
        public String toString() {
            return String.format("(1's=%d, T=%d)", ones, T);
        }
    }
}