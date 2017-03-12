import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Integer.parseInt;

@SuppressWarnings("Duplicates")
class SimHash {

    public static void main(String[] args) throws IOException {
        int N, Q;
        String[] texts, queries;

        // parse
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            N = parseInt(br.readLine());
            texts = new String[N]; // "unwvcjexln spsshavkog ..."
            for (int i = 0; i < N; i++) texts[i] = br.readLine();

            Q = parseInt(br.readLine());
            queries = new String[Q]; // "16 3" I=16 K=3 od 17.tog teksta
            for (int i = 0; i < Q; i++) queries[i] = br.readLine();
        }

        // simhash
        String[] hashes = new String[N]; // "f27c6b49c8fcec47ebeef2de783eaf57"
        for (int i = 0; i < N; i++) hashes[i] = simHash(texts[i]);

        texts = null;
        System.gc();

        // compare
        StringBuilder results = new StringBuilder(N * 3);
        for (String query : queries) {
            String[] q = query.split("\\s");
            int I = parseInt(q[0]); //hash
            int K = parseInt(q[1]); //distanca

            String hash = hashes[I];
            int numOfSimiliar = 0;
            for (int h = 0; h < hashes.length; h++) {
                if (I == h) continue;
                if (hammingDistance(hash.getBytes(), hashes[h].getBytes()) <= K) {
                    numOfSimiliar += 1;
                }
            }
            results.append(numOfSimiliar).append(System.lineSeparator());
        }
        // results
        System.out.print(results.toString());
    }

    /**
     * @param text (plain)
     * @return 128 character long binary string fingerprint
     */
    private static String simHash(String text) {
        String[] words = text.split("\\s");
        int[] sh = new int[128];

        for (String word : words) {
            byte[] hash = DigestUtils.md5(word);

            for (int b = 0; b < hash.length; b++) { // 16 bajtova
                for (int i = 0; i < 8; i++) { // 8 bita
                    if ((hash[b] >> 7 - i & 1) == 1) { // izdvoji i provjeri
                        sh[8 * b + i] += 1;
                    }
                    else {
                        sh[8 * b + i] -= 1;
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder(128);
        for (int i = 0; i < sh.length; i++) {
            sh[i] = sh[i] >= 0 ? 1 : 0;
            sb.append(sh[i]);
        }
        return sb.toString();
    }

    /**
     * @param h1 first simhashed text
     * @param h2 second simhashed text
     * @return hamming distance between two hashes of same length (bits)
     */
    private static int hammingDistance(byte[] h1, byte[] h2) {
        int distance = 0;
        for (int b = 0; b < h1.length; b++) {
            byte xor = (byte) (h1[b] ^ h2[b]);
            for (int i = 0; i < 8; i++) { //prebroji razlike po bitovima
                if ((xor >> 7 - i & 1) == 1) distance += 1;
            }
        }
        return distance;
    }
}