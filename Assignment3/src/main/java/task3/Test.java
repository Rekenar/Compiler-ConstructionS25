package task3;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String input = "Vector2D v = new Vector2D(3,5);";
        ObjectDeclarationParser parser = new ObjectDeclarationParser(input);
        
        try {
            parser.parse();
            System.out.println(Arrays.toString(parser.getTokens()));
            System.out.println("Parsing successful!");
        } catch (RuntimeException e) {
            System.out.println("Parsing failed: " + e.getMessage());
        }
    }
}
