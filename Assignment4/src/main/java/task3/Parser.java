package task3;

import task3.Token.TokenType;

public class Parser {


    // --- Custom Exception for Parsing Errors ---
    static class ParseException extends Exception {
        ParseException(String message, int position) {
            super("Syntax error at position " + position + ": " + message);
        }
    }

    // --- Task3.Parser ---

    Scanner scanner;
    Token currentToken;

    Parser(Scanner scanner) throws ParseException {
        this.scanner = scanner;
        currentToken = scanner.getNextToken();
        if (currentToken.type == TokenType.UNKNOWN) {
            throw new ParseException("Unknown token", currentToken.position);
        }
    }

    // Throws a ParseException with the given message.
    void error(String message) throws ParseException {
        throw new ParseException(message, currentToken.position);
    }

    // Compare current token with expected type and advance.
    void match(TokenType type) throws ParseException {
        if (check(type)) {
            currentToken = scanner.getNextToken();
        } else {
            error("Expected token " + type + " but found " + currentToken.type);
        }
    }

    boolean check(TokenType expected) {
        return currentToken.type == expected;
    }


    // Object = "{" [ Pair { "," Pair } ] "}".
    void parseObject() throws ParseException {
        match(TokenType.LBRACE);
        if (check(TokenType.STRING)) {
            parsePair();
            while (check(TokenType.COMMA)) {
                match(TokenType.COMMA);
                parsePair();
            }
        }
        match(TokenType.RBRACE);
    }


    // Pair = string ":" Value.
    void parsePair() throws Parser.ParseException {
        match(TokenType.STRING);
        match(TokenType.COLON);
        parseValue();
    }

    // Value = string | number | Object | Array.
    void parseValue() throws ParseException {
        switch (currentToken.type) {
            case STRING:
                match(TokenType.STRING);
                break;
            case NUMBER:
                match(TokenType.NUMBER);
                break;
            case LBRACE:
                parseObject();
                break;
            case LBRACKET:
                parseArray();
                break;
            default:
                error("Unexpected token " + currentToken.type + " in value");
        }
    }

    // Array = "[" [ Value { "," Value } ] "]".
    void parseArray() throws ParseException {
        match(TokenType.LBRACKET);
        if (!check(TokenType.RBRACKET)) {
            parseValue();
            while (check(TokenType.COMMA)) {
                match(TokenType.COMMA);
                parseValue();
            }
        }
        match(TokenType.RBRACKET);
    }

    // Start parsing.
    void parse() throws ParseException {
        parseObject();
        if (currentToken.type != TokenType.EOF) {
            error("Unexpected tokens after end of object");
        }
    }
}
