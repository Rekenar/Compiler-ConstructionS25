package task4;

import task4.Token.TokenType;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Parser {
    private Lexer lexer;
    private Token currentToken;
    private List<String> errors = new ArrayList<>();
    private String fileName;

    public Parser(Lexer lexer, String fileName) {
        this.lexer = lexer;
        advance();
        this.fileName = fileName;
    }

    public List<String> getErrors() {
        return errors;
    }

    // Start parsing the input as an object.
    public void parse() {
        object();
        if (currentToken.type != TokenType.EOF) {
            reportError("Extra content after valid input.");
        }
    }

    // object = "{" [ Pair { "," Pair } ] "}".
    private void object() {
        if (!match(TokenType.LBRACE)) {
            reportError("Expected '{' at the beginning of an object.");
            synchronize(createAnchorSet(TokenType.RBRACE, TokenType.COMMA));
            if (!match(TokenType.LBRACE)) {
                return;
            }
        }
        // Optional pairs.
        if (!check(TokenType.RBRACE)) {
            pair();
            while (match(TokenType.COMMA)) {
                // If the next token cannot start a pair, attempt recovery.
                if (!check(TokenType.STRING)) {
                    reportError("Expected a string as key in pair.");
                    synchronize(createAnchorSet(TokenType.COMMA, TokenType.RBRACE));
                } else {
                    pair();
                }
            }
        }
        if (!match(TokenType.RBRACE)) {
            reportError("Expected '}' at the end of object.");
            synchronize(createAnchorSet(TokenType.RBRACE));
            match(TokenType.RBRACE); // try to consume the anchor
        }
    }

    // Pair = string ":" Value.
    private void pair() {
        if (!match(TokenType.STRING)) {
            reportError("Expected string as key in pair.");
            synchronize(createAnchorSet(TokenType.COLON, TokenType.COMMA, TokenType.RBRACE));
        }
        if (!match(TokenType.COLON)) {
            reportError("Expected ':' after key.");
            synchronize(createAnchorSet(TokenType.STRING, TokenType.NUMBER, TokenType.LBRACE, TokenType.LBRACKET, TokenType.COMMA, TokenType.RBRACE));
        }
        value();
    }

    // Value = string | number | Object | Array.
    private void value() {
        if (check(TokenType.STRING)) {
            match(TokenType.STRING);
        } else if (check(TokenType.NUMBER)) {
            match(TokenType.NUMBER);
        } else if (check(TokenType.LBRACE)) {
            object();
        } else if (check(TokenType.LBRACKET)) {
            array();
        } else {
            reportError("Invalid value. Expected a string, number, object, or array.");
            // Recover by skipping until an anchor is found.
            synchronize(createAnchorSet(TokenType.COMMA, TokenType.RBRACE, TokenType.RBRACKET));
        }
    }

    // Array = "[" [ Value { "," Value } ] "]".
    private void array() {
        if (!match(TokenType.LBRACKET)) {
            reportError("Expected '[' at beginning of array.");
            synchronize(createAnchorSet(TokenType.RBRACKET, TokenType.COMMA));
            if (!match(TokenType.LBRACKET)) {
                return;
            }
        }
        if (!check(TokenType.RBRACKET)) {
            value();
            while (match(TokenType.COMMA)) {
                // If the next token isn’t a valid value, recover.
                if (!check(TokenType.STRING) && !check(TokenType.NUMBER)
                        && !check(TokenType.LBRACE) && !check(TokenType.LBRACKET)) {
                    reportError("Expected a valid value after comma in array.");
                    synchronize(createAnchorSet(TokenType.COMMA, TokenType.RBRACKET));
                } else {
                    value();
                }
            }
        }
        if (!match(TokenType.RBRACKET)) {
            reportError("Expected ']' at the end of array.");
            synchronize(createAnchorSet(TokenType.RBRACKET));
            match(TokenType.RBRACKET); // try to consume the anchor
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
        currentToken = lexer.getNextToken();
    }

    // Report an error message including token position.
    private void reportError(String message) {
        String err = "[" +fileName + "] Error at position " + currentToken.position +" : " + message;
        errors.add(err);
    }

    /**
     * Synchronize (recover) by advancing tokens until one of the anchor tokens is found.
     *
     * @param anchors A BitSet representing tokens to synchronize on.
     */
    private void synchronize(BitSet anchors) {
        while (currentToken.type != TokenType.EOF && !anchors.get(currentToken.type.ordinal())) {
            advance();
        }
    }

    /**
     * Helper method to create a BitSet from a list of TokenTypes.
     *
     * @param tokens The tokens to add to the BitSet.
     * @return A BitSet with bits set for each provided token.
     */
    private BitSet createAnchorSet(TokenType... tokens) {
        BitSet bitset = new BitSet(TokenType.values().length);
        for (TokenType t : tokens) {
            bitset.set(t.ordinal());
        }
        return bitset;
    }
}