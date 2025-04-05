package task4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java JSONParser <input-file1> <input-file2> ...");
            return;
        }

        for (String fileName : args) {
            System.out.println("Parsing file: " + fileName);
            try {
                String content = new String(Files.readAllBytes(Paths.get("src/main/java/task4/files/" + fileName)));
                Lexer lexer = new Lexer(content, fileName);
                Parser parser = new Parser(lexer, fileName);
                parser.parse();
                List<String> errors = parser.getErrors();
                if (errors.isEmpty()) {
                    System.out.println("Parsed successfully with no errors.");
                } else {
                    System.out.println("Found " + errors.size() + " error(s):");
                    for (String err : errors) {
                        System.out.println("  " + err);
                    }
                }
                System.out.println();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
