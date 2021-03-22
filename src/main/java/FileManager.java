import java.io.*;
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

    public void codeFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int znak;
        int[] wiadomosc = new int[LICZBA_BITOW];
        int[] sprawdz = new int[LICZBA_BITOW];

        FileWriter writer = new FileWriter("zakodowany.txt");

        //System.out.println(Arrays.toString(wiadomosc));
        //System.out.println(Arrays.toString(sprawdz));
        while ((znak = reader.read()) != -1) {
            //System.out.println(znak);

            Utils.wyczyscTablice(sprawdz, LICZBA_BITOW);
            Utils.naBinarna(znak, wiadomosc, LICZBA_BITOW);

            System.out.println(Arrays.toString(wiadomosc));
            System.out.println(Arrays.toString(sprawdz));

            for (int i = 0; i < LICZBA_BITOW; i++) {
                for (int j = 0; j < LICZBA_BITOW; j++) {
                    sprawdz[i] += wiadomosc[j] * MACIERZ[i][j];
                }
                sprawdz[i] %= 2;
            }
            for (int i = 0; i < LICZBA_BITOW; i++) {
                writer.write(wiadomosc[i] + 48);
            }
            for (int i = 0; i < LICZBA_BITOW; i++) {
                writer.write(sprawdz[i] + 48);
            }
            writer.write("\n");
            //int a = 128;
            //char kod = 0;
            //for (int i = 0; i < LICZBA_BITOW; i++) {
            //    kod += a * wiadomosc[i];
            //    a /= 2;
            //}
            //fputc(kod, code2);
            //a = 128;
            //kod = 0;
            //for (int i = 0; i < LICZBA_BITOW; i++) {
            //    kod += a * sprawdz[i];
            //    a /= 2;
            //}
            //fputc(kod, code2);

        }
        writer.close();
        System.out.println("File encoded succesfully");

    }

    //public void decodeFile()

}
