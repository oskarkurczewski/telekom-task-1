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
        byte[] sprawdz = new byte[8];

        //System.out.println(Arrays.toString(wiadomosc));
        //System.out.println(Arrays.toString(sprawdz));
        while ((znak = (byte) reader.read()) != -1) {

            Utils.wyczyscTablice(sprawdz, 8);
            Utils.naBinarna(znak, wiadomosc, 8);

            System.out.println(Arrays.toString(wiadomosc));
            System.out.println(Arrays.toString(sprawdz));

            // Wylicz
            Utils.obliczWektor(wiadomosc, sprawdz, MACIERZ);
            //for (int i = 0; i < 8; i++) {
            //    for (int j = 0; j < 8; j++) {
            //        sprawdz[i] += wiadomosc[j] * MACIERZ[i][j];
            //    }
            //    sprawdz[i] %= 2;
            //}

            Utils.wpiszDoPliku(wiadomosc, 8, writer);
            Utils.wpiszDoPliku(sprawdz, 8, writer);

            writer.write("\n");

        }
        reader.close();
        writer.close();
        System.out.println("File encoded succesfully");
    }

    public void decodeFile() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("zakodowany.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("odkodowany.txt"));

        byte[] encodedFileArray = new byte[16];
        byte[] mistakesArray = new byte[8];
        byte sign, counter = 0, signCounter = 1, mistakesCounter = 0;


        while ((sign = (byte) reader.read()) != -1) {
            if (sign != '\n') {
                encodedFileArray[counter] = (byte) (sign - 48);
                counter++;
            } else {
                int firstBitIndex;
                int secondBitIndex;
                for (int i = 0; i < 8; i++) {
                    mistakesArray[i] = 0;
                    for (int j = 0; j < (8 * 2); j++) {
                        mistakesArray[i] += encodedFileArray[j] * MACIERZ[i][j];
                    }
                    mistakesArray[i] %= 2;
                    if (mistakesArray[i] == 1) {
                        mistakesCounter = 1;
                    }
                }
                if (mistakesCounter != 0) {
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
                                if (encodedFileArray[firstBitIndex] != 0) {
                                    encodedFileArray[firstBitIndex] = 0;
                                } else {
                                    encodedFileArray[firstBitIndex] = 1;
                                }

                                if (encodedFileArray[secondBitIndex] != 0) {
                                    encodedFileArray[secondBitIndex] = 0;
                                } else {
                                    encodedFileArray[secondBitIndex] = 1;
                                }
                                i = (8 * 2);
                                break;
                            }
                        }
                    }
                    if (mistakesCounter == 1) {
                        for (int i = 0; i < (8 * 2); i++) {
                            for (int j = 0; j < 8; j++) {
                                if (MACIERZ[j][i] !=
                                        mistakesArray[j]) {
                                    break;
                                }

                                if (j == 7) {
                                    if (encodedFileArray[i] != 0) {
                                        encodedFileArray[i] = 0;
                                    } else {
                                        encodedFileArray[i] = 1;
                                    }
                                    i = (8 * 2);
                                }
                            }
                        }
                    }
                }
                counter = 0;
                signCounter++;
                mistakesCounter = 0;
                int a = 128;
                char kod = 0;
                for (int i = 0; i < 8; i++) {
                    kod += a * encodedFileArray[i];
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
