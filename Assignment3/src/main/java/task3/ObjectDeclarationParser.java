package task3;

import java.util.Arrays;

public class ObjectDeclarationParser {

    private String[] tokens;
    private int currentTokenIndex = 0;

    public ObjectDeclarationParser(String input) {
        this.tokens = Arrays.stream(input.split("\\s+|(?<=[=();,])|(?=[=();,])"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }


    public void parse() {
        parseObjDecl();
    }

    private void parseObjDecl() {
        parseObjType();
        if (match(TokenType.IDENT)) {
            consume();
        } else {
            error("Expected identifier after ObjType");
        }

        if (match(TokenType.EQUALS)) {
            consume();
            parseObjInit();
        }

        if (match(TokenType.SEMICOLON)) {
            consume();
        } else {
            error("Expected semicolon at the end of ObjDecl");
        }
    }

    private void parseObjType() {
        if (match(TokenType.IDENT)) {
            consume();
        } else {
            error("Expected identifier for ObjType");
        }
    }

    private void parseObjInit() {
        if (match(TokenType.NEW)) {
            consume();
        } else {
            error("Expected 'new' in ObjInit");
        }

        parseObjType();

        if (match(TokenType.LPAREN)) {
            consume();
        } else {
            error("Expected '(' in ObjInit");
        }

        if (match(TokenType.IDENT) || match(TokenType.NUMBER)) {
            parseObjArgs();
        }

        if (match(TokenType.RPAREN)) {
            consume();
        } else {
            error("Expected ')' in ObjInit");
        }
    }

    private void parseObjArgs() {
        parseExpr();

        while (match(TokenType.COMMA)) {
            consume();
            parseExpr();
        }
    }

    private void parseExpr() {
        if (match(TokenType.IDENT) || match(TokenType.NUMBER)) {
            consume();
        }
    }

    private String getCurrentToken() {
        if (currentTokenIndex < tokens.length) {
            return tokens[currentTokenIndex];
        }
        return null;
    }

    private boolean match(TokenType type) {
        String token = getCurrentToken();
        if (token == null) {
            return false;
        }
        return switch (type) {
            case IDENT -> token.matches("[a-zA-Z][a-zA-Z0-9]*");
            case EQUALS -> token.equals("=");
            case SEMICOLON -> token.equals(";");
            case NEW -> token.equals("new");
            case LPAREN -> token.equals("(");
            case RPAREN -> token.equals(")");
            case COMMA -> token.equals(",");
            case NUMBER -> token.matches("[0-9]+");
        };
    }

    private void consume() {
        if (currentTokenIndex < tokens.length) {
            currentTokenIndex++;
        }
    }

    private enum TokenType {
        IDENT, EQUALS, SEMICOLON, NEW, LPAREN, RPAREN, COMMA, NUMBER
    }

    private void error(String message) {
        throw new RuntimeException("Parser Error: " + message);
    }


    public String[] getTokens(){
        return tokens;
    }
}