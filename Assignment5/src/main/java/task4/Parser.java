package task4;

import java.util.HashMap;

public class Parser {
    private final Scanner scanner;
    private Token token;
    private final HashMap<String,Integer> varMap = new HashMap<>();

    Parser(Scanner scanner) { this.scanner = scanner; scan(); }

    void scan()  { token = scanner.next(); }
    void check(TokenType t) {
        if (token.type != t) error("expected " + t + " but found " + token.type);
        scan();
    }
    void error(String msg) {
        throw new RuntimeException("Parse error: " + msg);
    }


    void Program() {
        while (token.type != TokenType.EOF) {
            Statement();
        }
    }


    void Statement() {
        if (token.type == TokenType.IDENT) {
            String name = token.text;
            scan();
            check(TokenType.ASSIGN);
            int val = Expr();
            check(TokenType.SEMICOLON);
            varMap.put(name, val);     // semantic action
        }
        else if (token.type == TokenType.PRINT) {
            scan();
            int val = Expr();
            check(TokenType.SEMICOLON);
            System.out.println(val);   // semantic action
        }
        else {
            error("expected statement");
        }
    }


    int Expr() {
        int val = Term();
        while (token.type == TokenType.PLUS || token.type == TokenType.MINUS) {
            if (token.type == TokenType.PLUS) {
                scan();
                int v = Term();
                val = val + v;       // semantic action
            } else {
                scan();
                int v = Term();
                val = val - v;       // semantic action
            }
        }
        return val;
    }


    int Term() {
        int val = Factor();
        while (token.type == TokenType.MUL || token.type == TokenType.DIV) {
            if (token.type == TokenType.MUL) {
                scan();
                int v = Factor();
                val = val * v;       // semantic action
            } else {
                scan();
                int v = Factor();
                val = val / v;       // semantic action
            }
        }
        return val;
    }


    int Factor() {
        int val;
        switch (token.type) {
            case NUMBER:
                val = Integer.parseInt(token.text);
                scan();
                break;
            case IDENT:
                val = varMap.getOrDefault(token.text, 0);
                scan();
                break;
            case LPAREN:
                scan();
                val = Expr();
                check(TokenType.RPAREN);
                break;
            case MINUS:
                scan();
                int v = Factor();
                val = -v;
                break;
            default:
                error("expected factor");
                return 0; // unreachable
        }
        return val;
    }
}