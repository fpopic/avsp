import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class PCY {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(args.length != 0 ?
                new FileReader(args[0]) : //komp
                new InputStreamReader(System.in)); //sprut

        int brKosara = Integer.parseInt(br.readLine());
        int prag = (int) Math.floor(Double.parseDouble(br.readLine()) * brKosara); //delac ispravio
        int brPretinaca = Integer.parseInt(br.readLine());

        short[][] kosare = new short[brKosara][];
        Map<Short, Integer> brPredmeta = new HashMap<>();

        // 1.Brojanje predmeta po kosarama
        for (int k = 0; k < brKosara; k++) {
            String[] part = br.readLine().split("\\s");
            kosare[k] = new short[part.length];
            for (int j = 0; j < part.length; j++) {
                short i = Short.parseShort(part[j]); //parsiranje
                kosare[k][j] = i;

                brPredmeta.putIfAbsent(i, 0); //brojanje
                brPredmeta.put(i, brPredmeta.get(i) + 1);
            }
        }
        br.close();

        int[] pretinci = new int[brPretinaca];

        // 2.Sazimanje parova predmeta (i,j) u pretince
        for (short[] kosara : kosare) {
            for (int ii = 0; ii < kosara.length; ii++) {
                short i = kosara[ii];
                for (int jj = ii + 1; jj < kosara.length; jj++) {
                    short j = kosara[jj];
                    // sazmi par predmeta (i,j) u pretinac k
                    if (brPredmeta.get(i) >= prag && brPredmeta.get(j) >= prag) {
                        int k = (i * brPredmeta.size() + j) % brPretinaca;
                        pretinci[k] += 1;
                    }
                }
            }
        }

        Map<String, Integer> parovi = new HashMap<>(); // kompozitni kljuc <(i,j),brojac>

        // 3.Brojanje cestih parova predmeta
        for (short[] kosara : kosare) {
            for (int ii = 0; ii < kosara.length; ii++) {
                short i = kosara[ii];
                for (int jj = ii + 1; jj < kosara.length; jj++) {
                    short j = kosara[jj];
                    if (brPredmeta.get(i) >= prag && brPredmeta.get(j) >= prag) {
                        int k = (i * brPredmeta.size() + j) % brPretinaca;
                        if (pretinci[k] >= prag) {
                            String kljuc = i + "," + j;
                            parovi.putIfAbsent(kljuc, 0);
                            parovi.put(kljuc, parovi.get(kljuc) + 1);
                        }
                    }
                }
            }
        }

        //rezultat
        long m = brPredmeta.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(count -> count >= prag) //*ovo je falilo bilo*
                .count(); // m = broj cestih predmeta

        System.out.println((m * (m - 1)) / 2); // A
        System.out.println(parovi.size()); // P

        parovi.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(count -> count >= prag) //*ovo je falilo bilo*
                .sorted((i, j) -> j.compareTo(i)) //silazno
                .forEach(System.out::println);
    }
}