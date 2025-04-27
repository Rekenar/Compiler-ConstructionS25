package task3;// ATGMemoryCalculator.java

import java.io.*;

public class ATGMemoryCalculator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ATGMemoryCalculator <inputFile>");
            System.exit(1);
        }
        System.out.printf("%6s %6s %s%n", "OFFSET", "SIZE", "TYPE");

        try (Reader r = new FileReader(args[0])) {
            Parser p = new Parser(new Scanner(r));
            p.VarDecList();
            // final total
            System.out.println(p.offset);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            System.exit(2);
        }
    }
}
