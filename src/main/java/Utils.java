import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    public static void naBinarna(byte znak, byte[] wiadomosc, int liczbaBitow) {
        for (int i = liczbaBitow - 1; i >= 0; i--) {
            wiadomosc[i] = (byte) (znak % 2);
            znak /= 2;
        }
    }

    public static void wyczyscTablice(byte[] tablica, int liczbaBitow) {
        for (byte i = 0; i < liczbaBitow; i++) {
            tablica[i] = 0;
        }
    }

    public static void wpiszDoPliku(byte[] tablica, int liczbaBitow, FileWriter writer) throws IOException {
        for (byte i = 0; i < liczbaBitow; i++) {
            writer.write(tablica[i] + 48);
        }
    }

    public static void obliczWektor(byte[] wiadomosc, byte[] wektor, byte[][] macierz) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                wektor[x] += wiadomosc[y] * macierz[x][y];
            }
            wektor[x] %= 2;
        }
    }

}
