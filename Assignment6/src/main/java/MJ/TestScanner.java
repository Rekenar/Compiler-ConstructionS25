package MJ;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestScanner {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MJ.TestScanner <source-file> [<ground-truth-file>]");
            System.exit(0);
        }

        // Read ground truth tokens if provided.
        List<Integer> expectedTokens = null;
        if (args.length > 1) {
            expectedTokens = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split("\\s+");
                    for (String tok : tokens) {
                        if (!tok.isEmpty()) {
                            expectedTokens.add(Integer.parseInt(tok));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading ground truth file: " + e.getMessage());
                System.exit(1);
            }
        }

        // Scan the source file and collect the actual token codes.
        List<Integer> actualTokens = new ArrayList<>();
        try {
            FileReader fr = new FileReader(args[0]);
            Scanner.init(fr);
            Token t;
            do {
                t = Scanner.next();
                actualTokens.add(t.kind);
                System.out.print("Line " + t.line + ", Col " + t.col + ": Token code = " + t.kind);
                if (t.val != null) {
                    System.out.print(" (\"" + t.val + "\")");
                }
                System.out.println();
            } while (t.kind != 41);
            fr.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            System.exit(1);
        }

        // If a ground truth file was provided, compare the expected token sequence to the actual one.
        if (expectedTokens != null) {
            if (expectedTokens.equals(actualTokens)) {
                System.out.println("Test passed: Actual token sequence matches ground truth.");
            } else {
                System.out.println("Test failed: Actual token sequence does not match ground truth.");
                System.out.println("Expected: " + expectedTokens);
                System.out.println("Actual:   " + actualTokens);
            }
        }
    }
}
