package task4;

import task4.Token.TokenType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class Parser {
    private final Scanner scanner;
    private Token currentToken;
    private final List<String> errors = new ArrayList<>();
    private final String fileName;

    public Parser(Scanner scanner, String fileName) {
        this.scanner = scanner;
        this.fileName = fileName;
        advance();
    }

    public List<String> getErrors() {
        return errors;
    }

    // Start parsing the input as an object.
    public void parse() {
        parseObject();
        if (currentToken.type != TokenType.EOF) {
            reportError("Extra content after valid input.");
        }
    }

    // parseObject = "{" [ parsePair { "," parsePair } ] "}".
    private void parseObject() {
        if (!match(TokenType.LBRACE)) {
            reportError("Expected '{' at the beginning of an object.");
            synchronize(createAnchorSet(TokenType.RBRACE));
        }
        // Optional pairs.
        if (!check(TokenType.RBRACE)) {
            do {
                parsePair();
            } while (match(TokenType.COMMA));
        }
        if (!match(TokenType.RBRACE)) {
            reportError("Expected '}' at the end of object.");
            synchronize(createAnchorSet(TokenType.RBRACE));
            match(TokenType.RBRACE);
        }
    }

    // parsePair = string ":" parseValue.
    private void parsePair() {
        if (!match(TokenType.STRING)) {
            reportError("Expected string as key in pair.");
            synchronize(createAnchorSet(TokenType.STRING, TokenType.COLON, TokenType.COMMA, TokenType.RBRACE));
        }
        if (!match(TokenType.COLON)) {
            reportError("Expected ':' after key.");
            synchronize(createAnchorSet(TokenType.STRING, TokenType.NUMBER, TokenType.LBRACE, TokenType.LBRACKET, TokenType.COMMA, TokenType.RBRACE));
        }
        parseValue();
    }

    // parseValue = string | number | parseObject | parseArray.
    private void parseValue() {
        if (check(TokenType.STRING)) {
            match(TokenType.STRING);
        } else if (check(TokenType.NUMBER)) {
            match(TokenType.NUMBER);
        } else if (check(TokenType.LBRACE)) {
            parseObject();
        } else if (check(TokenType.LBRACKET)) {
            parseArray();
        } else {
            reportError("Invalid value. Expected a string, number, object, or array.");
            synchronize(createAnchorSet(TokenType.COMMA, TokenType.RBRACE, TokenType.RBRACKET));
        }
    }

    // parseArray = "[" [ parseValue { "," parseValue } ] "]".
    private void parseArray() {
        if (!match(TokenType.LBRACKET)) {
            reportError("Expected '[' at beginning of array.");
            synchronize(createAnchorSet(TokenType.RBRACKET, TokenType.COMMA));
        }
        if (!check(TokenType.RBRACKET)) {
            do {
                parseValue();
            } while (match(TokenType.COMMA));
        }
        if (!match(TokenType.RBRACKET)) {
            reportError("Expected ']' at the end of array.");
            synchronize(createAnchorSet(TokenType.RBRACKET));
            match(TokenType.RBRACKET);
        }
    }

    // Helper methods for token management
    private boolean match(TokenType expected) {
        if (check(expected)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType expected) {
        return currentToken.type == expected;
    }

    private void advance() {
        currentToken = scanner.getNextToken();
    }

    // Report an error message including token position.
    private void reportError(String message) {
        String err = "[" + fileName + "] Error at position " + currentToken.position + " : " + message;
        errors.add(err);
    }


    private void synchronize(Set<TokenType> anchors) {
        while (currentToken.type != TokenType.EOF && !anchors.contains(currentToken.type)) {
            advance();
        }
    }


    private Set<TokenType> createAnchorSet(TokenType... tokens) {
        return EnumSet.of(tokens[0], tokens);
    }
}
