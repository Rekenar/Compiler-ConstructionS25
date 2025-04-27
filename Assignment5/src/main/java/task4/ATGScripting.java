package task4;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ATGScripting {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ATGScripting <inputFile>");
            System.exit(1);
        }
        try (Reader r = new FileReader(args[0])) {
            Parser p = new Parser(new Scanner(r));
            p.Program();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            System.exit(2);
        }
    }
}
