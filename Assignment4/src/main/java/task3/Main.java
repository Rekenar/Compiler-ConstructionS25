package task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java JSONParser <input-file1> <input-file2> ...");
            return;
        }

        for (String fileName : args) {
            System.out.println("Parsing file: " + fileName);
            try {
                String content = new String(Files.readAllBytes(Paths.get("src/main/java/task3/files/" + fileName)));
                Scanner scanner = new Scanner(content);
                Parser parser = new Parser(scanner);
                parser.parse();
                System.out.println("Parsing successful: " + fileName);
            } catch (IOException e) {
                System.err.println("Error reading file: " + fileName);
            } catch (Parser.ParseException e) {
                System.err.println("Parse error in file " + fileName + ": " + e.getMessage());
            }
            System.out.println();
        }
    }
}
