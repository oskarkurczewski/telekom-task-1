import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileManager {

    static final int LICZBA_BITOW = 8;

    // Macierz 8x16
    static final int[][] MACIERZ = new int[][] {
        {1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
        {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0},
        {1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1}
    };

    public FileManager() {
    }

    public void openFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int znak;
        int[] wiadomosc = new int[LICZBA_BITOW];
        int[] sprawdz = new int[LICZBA_BITOW];


        System.out.println(Arrays.toString(wiadomosc));
        System.out.println(Arrays.toString(sprawdz));
        while ((znak = reader.read()) != -1) {
            System.out.println(znak);


            Utils.wyczyscTablice(sprawdz, LICZBA_BITOW);
            Utils.naBinarna(znak, wiadomosc, LICZBA_BITOW);

            System.out.println(Arrays.toString(wiadomosc));
            System.out.println(Arrays.toString(sprawdz));

//            for (int i = 0; i < LICZBA_BITOW; i++) {
//                for (int j = 0; j < LICZBA_BITOW; j++) {
//                    sprawdz[i] += wiadomosc[j] * MACIERZ[j][i];
//                }
//                sprawdz[i] %= 2;
//            }



        }
    }

}
