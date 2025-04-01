package task2;

public class Test {
    public static void main(String[] args) {
        String expression = "(A && !B) || !(C || D)";
        BooleanExpressionParser parser = new BooleanExpressionParser(expression);
        
        try {
            parser.parse();
            System.out.println(parser.getTokens());
            System.out.println("Parsing successful!");
        } catch (RuntimeException e) {
            System.out.println("Parsing failed: " + e.getMessage());
        }
    }
}
