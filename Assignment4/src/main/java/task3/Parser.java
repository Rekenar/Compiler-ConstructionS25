package task3;


public class Parser {


    // --- Custom Exception for Parsing Errors ---
    static class ParseException extends Exception {
        int position;
        ParseException(String message, int position) {
            super("Syntax error at position " + position + ": " + message);
            this.position = position;
        }
    }

    // --- Task3.Parser ---

        Scanner scanner;
        Token currentToken;

        Parser(Scanner scanner) throws ParseException {
            this.scanner = scanner;
            currentToken = scanner.getNextToken();
            if (currentToken.type == Token.TokenType.UNKNOWN) {
                throw new ParseException("Unknown token", currentToken.position);
            }
        }

        // Throws a ParseException with the given message.
        void error(String message) throws ParseException {
            throw new ParseException(message, currentToken.position);
        }

        // Compare current token with expected type and advance.
        void match(Token.TokenType type) throws ParseException {
            if (currentToken.type == type) {
                currentToken = scanner.getNextToken();
            } else {
                error("Expected token " + type + " but found " + currentToken.type);
            }
        }

        // Object = "{" [ Pair { "," Pair } ] "}".
        void parseObject() throws ParseException {
            if (currentToken.type == Token.TokenType.LBRACE) {
                match(Token.TokenType.LBRACE);
                // Optional list of pairs.
                if (currentToken.type == Token.TokenType.STRING) {
                    parsePair();
                    while (currentToken.type == Token.TokenType.COMMA) {
                        match(Token.TokenType.COMMA);
                        parsePair();
                    }
                }
                if (currentToken.type == Token.TokenType.RBRACE) {
                    match(Token.TokenType.RBRACE);
                } else {
                    error("Expected '}' at end of object");
                }
            } else {
                error("Expected '{' at beginning of object");
            }
        }

        // Pair = string ":" Value.
        void parsePair() throws ParseException {
            if (currentToken.type == Token.TokenType.STRING) {
                match(Token.TokenType.STRING);
            } else {
                error("Expected string as key in pair");
            }
            if (currentToken.type == Token.TokenType.COLON) {
                match(Token.TokenType.COLON);
            } else {
                error("Expected ':' after key in pair");
            }
            parseValue();
        }

        // Value = string | number | Object | Array.
        void parseValue() throws ParseException {
            switch (currentToken.type) {
                case STRING:
                    match(Token.TokenType.STRING);
                    break;
                case NUMBER:
                    match(Token.TokenType.NUMBER);
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
            if (currentToken.type == Token.TokenType.LBRACKET) {
                match(Token.TokenType.LBRACKET);
                if (currentToken.type != Token.TokenType.RBRACKET) {
                    parseValue();
                    while (currentToken.type == Token.TokenType.COMMA) {
                        match(Token.TokenType.COMMA);
                        parseValue();
                    }
                }
                if (currentToken.type == Token.TokenType.RBRACKET) {
                    match(Token.TokenType.RBRACKET);
                } else {
                    error("Expected ']' at end of array");
                }
            } else {
                error("Expected '[' at beginning of array");
            }
        }

        // Start parsing.
        void parse() throws ParseException {
            parseObject();
            if (currentToken.type != Token.TokenType.EOF) {
                error("Unexpected tokens after end of object");
            }
        }
    }
