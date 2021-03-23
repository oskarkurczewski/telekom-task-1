import java.io.*;
import java.util.Arrays;

public class FileManager {


    // Macierz 8x16
    static final byte[][] MACIERZ = new byte[][] {
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
        FileWriter writer = new FileWriter("zakodowany.txt");

        byte znak;
        byte[] wiadomosc = new byte[8];
        byte[] sprawdzajace = new byte[8];

        // WCZYTYWANIE ZNAKOW Z PLIKU
        while ((znak = (byte) reader.read()) != -1) {

            // WYZEROWANIE TABLICY
            Utils.wyczyscTablice(sprawdzajace, 8);

            // ZAMIANA WCZYTANEGO ZNAKU 8 BITOWEGO DO POSTACI BINARNEJ
            Utils.naBinarna(znak, wiadomosc, 8);

            // WYLICZENIE BITOW SPRAWDZAJACYCH
            Utils.obliczWektor(wiadomosc, sprawdzajace, MACIERZ);

            // ZAPISANIE ZAKODOWANEGO ZNAKU WRAZ Z BITAMI SPRAWDZAJACYMI
            Utils.wpiszDoPliku(wiadomosc, 8, writer);
            Utils.wpiszDoPliku(sprawdzajace, 8, writer);

            writer.write("\n");
        }
        reader.close();
        writer.close();
    }

    public void decodeFile() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("zakodowany.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("odkodowany.txt"));

        byte[] zakodowanaWiadomosc = new byte[16];
        byte[] mistakesArray = new byte[8];

        byte znak;
        byte index = 0;
        byte iloscZnakow = 1;
        int iloscBledow = 0;

        // WCZYTYWANIE ZAKODOWANEGO PLIKU
        while ((znak = (byte) reader.read()) != -1) {
            if (znak != '\n') {
                zakodowanaWiadomosc[index] = (byte) (znak - 48);
                index++;
            } else {
                int firstBitIndex;
                int secondBitIndex;
                for (int i = 0; i < 8; i++) {
                    mistakesArray[i] = 0;
                    for (int j = 0; j < (8 * 2); j++) {
                        mistakesArray[i] += zakodowanaWiadomosc[j] * MACIERZ[i][j];
                    }
                    mistakesArray[i] %= 2;
                    if (mistakesArray[i] == 1) {
                        iloscBledow = 1;
                    }
                }
                if (iloscBledow != 0) {
                    int exists = 0;
                    for (int i = 0; i < 15; i++) {
                        for (int j = i + 1; j < (16); j++) {
                            exists = 1;
                            for (int k = 0; k < 8; k++) {
                                if (mistakesArray[k] != (MACIERZ[k][i] ^ MACIERZ[k][j])) {
                                    exists = 0;
                                    break;
                                }
                            }
                            if (exists == 1) { //PRZYPADEK DLA 2 BLEDOW
                                firstBitIndex = i;
                                secondBitIndex = j;
                                if (zakodowanaWiadomosc[firstBitIndex] != 0) {
                                    zakodowanaWiadomosc[firstBitIndex] = 0;
                                } else {
                                    zakodowanaWiadomosc[firstBitIndex] = 1;
                                }

                                if (zakodowanaWiadomosc[secondBitIndex] != 0) {
                                    zakodowanaWiadomosc[secondBitIndex] = 0;
                                } else {
                                    zakodowanaWiadomosc[secondBitIndex] = 1;
                                }
                                i = (8 * 2);
                                break;
                            }
                        }
                    }
                    if (iloscBledow == 1) {
                        for (int i = 0; i < (8 * 2); i++) {
                            for (int j = 0; j < 8; j++) {
                                if (MACIERZ[j][i] !=
                                        mistakesArray[j]) {
                                    break;
                                }

                                if (j == 7) {
                                    if (zakodowanaWiadomosc[i] != 0) {
                                        zakodowanaWiadomosc[i] = 0;
                                    } else {
                                        zakodowanaWiadomosc[i] = 1;
                                    }
                                    i = (8 * 2);
                                }
                            }
                        }
                    }
                }
                index = 0;
                iloscZnakow++;
                iloscBledow = 0;
                int a = 128;
                char kod = 0;
                for (int i = 0; i < 8; i++) {
                    kod += a * zakodowanaWiadomosc[i];
                    a /= 2;
                }
                System.out.println(kod);
                writer.write(kod);
            }
        }
        reader.close();
        writer.close();
        System.out.println("File encoded succesfully");
    }
}
