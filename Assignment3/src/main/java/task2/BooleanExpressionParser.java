package task2;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BooleanExpressionParser {
    private List<String> tokens;
    private int currentTokenIndex;

    public BooleanExpressionParser(String input) {
        tokens = tokenize(input);
        currentTokenIndex = 0;
    }

    private List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        String regex = "\\(|\\)|!|&&|\\|\\||[a-zA-Z]+"; // Matches (, ), !, &&, ||, and identifiers
        Matcher matcher = Pattern.compile(regex).matcher(input);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
}


    private String nextToken() {
        return currentTokenIndex < tokens.size() ? tokens.get(currentTokenIndex) : null;
    }

    private void consumeToken() {
        currentTokenIndex++;
    }

    public void parse() {
        expr();
        if (currentTokenIndex < tokens.size()) {
            throw new RuntimeException("Unexpected token: " + nextToken());
        }
    }

    private void expr() {
        andExpr();
        while (nextToken() != null && nextToken().equals("||")) {
            consumeToken();
            andExpr();
        }
    }

    private void andExpr() {
        notExpr();
        while (nextToken() != null && nextToken().equals("&&")) {
            consumeToken();
            notExpr();
        }
    }

    private void notExpr() {
        if (nextToken() != null && nextToken().equals("!")) {
            consumeToken(); // Consume '!'
        }
        if (nextToken() != null && nextToken().matches("[a-zA-Z]+")) { // Identifier
            consumeToken();
        } else if (nextToken() != null && nextToken().equals("(")) { // Opening parenthesis
            consumeToken(); // Consume '('
            expr(); // Parse the inner expression
            if (nextToken() == null || !nextToken().equals(")")) { // Ensure closing parenthesis
                throw new RuntimeException("Expected closing parenthesis but found: " + nextToken());
            }
            consumeToken(); // Consume ')'
        } else {
            throw new RuntimeException("Unexpected token: " + nextToken());
        }
    }


    public List<String> getTokens(){
        return tokens;
    }
    
}

// Explanation of parsing (A && !B) || !(C || D):
// 1. Parse Expr -> AndExpr { "||" AndExpr }
// 2. Parse AndExpr -> NotExpr { "&&" NotExpr }
// 3. Parse NotExpr: "("
//    - Inside parentheses, parse AndExpr: "A"
//    - "&&" operator found, parse NotExpr: "!B"
//    - "B" is an identifier
// 4. End parentheses and complete first AndExpr
// 5. "||" operator found, parse AndExpr -> NotExpr "!(C || D)"
//    - "!" operator found, parse "(C || D)"
//    - Inside parentheses, parse AndExpr "C"
//    - "||" operator found, parse AndExpr "D"
// 6. End parentheses and complete expression

// It ignores not recognized tokens
