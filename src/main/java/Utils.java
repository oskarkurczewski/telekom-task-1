public class Utils {

    public static void naBinarna(int znak, int[] wiadomosc, int liczbaBitow) {
        for (int i = liczbaBitow - 1; i >= 0; i--) {
            wiadomosc[i] = znak % 2;
            znak /= 2;
        }
    }

    public static void wyczyscTablice(int[] tablica, int liczbaBitow) {
        for (int i = 0; i < liczbaBitow; i++) {
            tablica[i] = 0;
        }
    }

}
