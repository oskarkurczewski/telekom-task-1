import java.io.*;

public class App {
    public static void main(String[] args) throws IOException {

        System.out.println(System.getProperty("user.dir"));

        FileManager fileManager = new FileManager();

        System.out.println("1. Zakodowanie wiadomosci\n" +
                            "2. Odkodowanie wiadomości\n" +
                            "Wybierz opcje: ");

        //Scanner in = new Scanner(System.in);
        //int wybor;
        //do {
        //    wybor = in.nextInt();
        //    System.out.println(wybor);
        //} while(wybor != 1 && wybor != 2);
        //
        //switch (wybor) {
        //    case 1 -> {
        //        System.out.println("Wprowadz nazwe pliku: ");
        //        String filePath = in.next();
        //        fileManager.openFile(filePath);
        //    }
        //    case 2 -> {
        //        System.out.println("XD");
        //    }
        //}
        fileManager.codeFile("data.txt");
        //in.close();

    }
}
