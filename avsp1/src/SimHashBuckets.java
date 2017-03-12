import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

@SuppressWarnings("Duplicates")
class SimHashBuckets {

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

        // LSH
        Map<Integer, Set<Integer>> candidates = LSH(hashes);

        // compare
        StringBuilder results = new StringBuilder(N * 3);
        for (String query : queries) {
            String[] q = query.split("\\s");
            int I = parseInt(q[0]); //rbr hash-a
            int K = parseInt(q[1]); //distanca

            String hash = hashes[I];

            int numOfSimilar = 0;
            Set<Integer> candidatesForI = candidates.get(I);
            for (Integer h : candidatesForI) {
                if (hammingDistance(hash.getBytes(), hashes[h].getBytes()) <= K) {
                    numOfSimilar += 1;
                }
            }
            results.append(numOfSimilar).append(System.lineSeparator());

        }
        // results
        System.out.print(results.toString());
    }

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
        return sb.toString(); //"1010.....0101"
    }

    private static Map<Integer, Set<Integer>> LSH(String[] hashes) {
        // K - redni broj teksta
        // V - skup rednih brojeva tekstova kandidata za slicnost
        Map<Integer, Set<Integer>> kandidati = new HashMap<>(hashes.length);
        for (int pojas = 1; pojas <= 8; pojas++) {

            // K - Integer val pretinca
            // V - skup id_tekstova ciji se sazetci iz ovog pojasa hashiraju u pretinac
            Map<Integer, Set<Integer>> pretinci = new HashMap<>();
            for (int trenutni_id = 0; trenutni_id < hashes.length; trenutni_id++) {

                //128-bitni binarni string "1010.....0101"
                String hash = hashes[trenutni_id];
                int start = 127 - 16 * pojas + 1;
                int val = parseInt(hash.substring(start, start + 16), 2);

                Set<Integer> tekstovi_u_pretincu;
                if (pretinci.get(val) != null) {
                    tekstovi_u_pretincu = pretinci.get(val);
                    for (Integer tekst_id : tekstovi_u_pretincu) {
                        kandidati.putIfAbsent(trenutni_id, new HashSet<>());
                        kandidati.get(trenutni_id).add(tekst_id);
                        kandidati.putIfAbsent(tekst_id, new HashSet<>());
                        kandidati.get(tekst_id).add(trenutni_id);
                    }
                }
                else {
                    tekstovi_u_pretincu = new HashSet<>();
                }
                tekstovi_u_pretincu.add(trenutni_id);
                pretinci.put(val, tekstovi_u_pretincu);
            }
        }
        return kandidati;
    }

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