import java.io.*;
import java.util.Arrays;

public class FileManager {


    // MACIERZ BEZ KOLUMNY ZEROWEJ ORAZ BEZ POWTARZAJACYCH SIE KOLUMN
    static final byte[][] MACIERZ = new byte[][] {
        {0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
        {1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1}
    };
    // MACIERZ WYGENEROWANA NA STRONIE http://www.ee.unb.ca/cgi-bin/tervo/polygen2.pl?d=8&p=101011001&s=1&c=1&a=1&g=1



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
        byte[] iloczyn = new byte[8];

        byte znak;
        byte index = 0;
        boolean wystapil_blad = false;

        // WCZYTYWANIE ZAKODOWANEGO PLIKU
        while ((znak = (byte) reader.read()) != -1) {

            if (znak == '\n') {
                wystapil_blad = false;
                index = 0;
                System.out.println("Zakodowana wiadomosc: \n" + Arrays.toString(zakodowanaWiadomosc));

                // OBLICZENIE ILOCZYNU ZAKODOWANEJ WIADOMOSCI Z MACIERZA H
                for (int x = 0; x < 8; x++) {
                    iloczyn[x] = 0;
                    for (int y = 0; y < 16; y++) {
                        iloczyn[x] += zakodowanaWiadomosc[y] * MACIERZ[x][y];
                    }
                    // SPRAWDZENIE CZY OTRZYMANY ILOCZYN JEST SAMYMI 0
                    if ((iloczyn[x] %= 2) != 0) wystapil_blad = true;
                }

                System.out.println("Zakodowany iloczyn: \n" + Arrays.toString(iloczyn));
                System.out.println("Wystapil blad: \n" + wystapil_blad);

                // JESLI ILOCZYN NIE JEST 0 TO WYSTAPIL BLAD
                if (wystapil_blad) {
                    int bladPojedynczy = 0;

                    // SPRAWDZENIE CZY OTRZYMANY BLAD JEST POJEDYNCZY
                    for (int x = 0; x < 8; x++) {
                        bladPojedynczy = 0;
                        // JESLI OTRZYMANY ILOCZYN JEST ROWNY KOLUMNIE TO OZNACZA ZE WYSTAPIL BLAD POJEDYNCZY
                        for (int y = 0; y < 8; y++) {
                            if (iloczyn[y] == MACIERZ[y][x]) {
                                bladPojedynczy++;
                            }
                        }
                        // JESLI WYSTAPIL BLAD POJEDYNCZY TO BIT O NUMERZE KOLUMNY ZANEGUJ
                        if (bladPojedynczy == 8) {
                            if (zakodowanaWiadomosc[x] == 0) {
                                zakodowanaWiadomosc[x] = 1;
                            }
                            else {
                                zakodowanaWiadomosc[x] = 0;
                            }
                            break;
                        }
                    }

                    // JESLI BLAD NIE JEST BLEDEM POJEDYNCZYM
                    if (bladPojedynczy != 8) {

                        int indexPierwszegoBledu;
                        int indexDrugiegoBledu;
                        boolean znaleziono;

                        // SPRAWDZENIE CZY OTRZYMANY ILOCZYN NIE JEST SUMA 2 KOLUMN
                        for (int x = 0; x < 15; x++) {
                            for (int y = x + 1; y < 16; y++) {
                                znaleziono = true;
                                for (int k = 0; k < 8; k++) {
                                    if (iloczyn[k] != (MACIERZ[k][x] ^ MACIERZ[k][y])) {
                                        znaleziono = false;
                                        break;
                                    }
                                }
                                // JESLI BLAD JEST PODWOJNY
                                if (znaleziono) {

                                    indexPierwszegoBledu = x;
                                    indexDrugiegoBledu = y;

                                    // ZANEGUJ BITY O INDEXIE ZNALEZIONEJ KOLUMNY
                                    if (zakodowanaWiadomosc[indexPierwszegoBledu] == 0) {
                                        zakodowanaWiadomosc[indexPierwszegoBledu] = 1;
                                    } else {
                                        zakodowanaWiadomosc[indexPierwszegoBledu] = 0;
                                    }

                                    // ZANEGUJ BITY O INDEXIE ZNALEZIONEJ KOLUMNY
                                    if (zakodowanaWiadomosc[indexDrugiegoBledu] == 0) {
                                        zakodowanaWiadomosc[indexDrugiegoBledu] = 1;
                                    } else {
                                        zakodowanaWiadomosc[indexDrugiegoBledu] = 0;
                                    }
                                    break;
                                }
                            }
                        }
                    }

                }

                // ZAPISANIE ZNAKOW DO PLIKU
                char znakASCII = 0;
                for (int i = 7; i > -1; i--) {
                    znakASCII += zakodowanaWiadomosc[i] * Math.pow(2, 7 - i);
                }
                System.out.println(znakASCII);
                writer.write(znakASCII);

            // JESLI WCZYTANY ZNAK TO NIE KONIEC LINI \N
            } else {
                zakodowanaWiadomosc[index] = (byte) (znak - 48);
                index++;
            }

        }
        reader.close();
        writer.close();
    }
}
