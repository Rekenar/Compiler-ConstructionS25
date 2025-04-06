package task3;

public class Scanner {
    String input;
    int pos;
    int length;

    Scanner(String input) {
        this.input = input;
        this.pos = 0;
        this.length = input.length();
    }

    // Returns the next token from the input.
    Token getNextToken() {
        skipWhitespace();
        if (pos >= length) {
            return new Token(Token.TokenType.EOF, null, pos);
        }
        char current = input.charAt(pos);

        // Single-character tokens.
        if (current == '{') {
            pos++;
            return new Token(Token.TokenType.LBRACE, "{", pos - 1);
        } else if (current == '}') {
            pos++;
            return new Token(Token.TokenType.RBRACE, "}", pos - 1);
        } else if (current == '[') {
            pos++;
            return new Token(Token.TokenType.LBRACKET, "[", pos - 1);
        } else if (current == ']') {
            pos++;
            return new Token(Token.TokenType.RBRACKET, "]", pos - 1);
        } else if (current == ',') {
            pos++;
            return new Token(Token.TokenType.COMMA, ",", pos - 1);
        } else if (current == ':') {
            pos++;
            return new Token(Token.TokenType.COLON, ":", pos - 1);
        } else if (current == '"') {
            // Parse string literal.
            pos++; // Skip the opening quote.
            int start = pos;
            StringBuilder sb = new StringBuilder();
            while (pos < length && input.charAt(pos) != '"') {
                sb.append(input.charAt(pos));
                pos++;
            }
            if (pos < length && input.charAt(pos) == '"') {
                pos++; // Skip the closing quote.
                return new Token(Token.TokenType.STRING, sb.toString(), start - 1);
            } else {
                return new Token(Token.TokenType.UNKNOWN, sb.toString(), start - 1);
            }
        } else if (Character.isDigit(current) || current == '-') {
            int start = pos;
            StringBuilder sb = new StringBuilder();
            if (current == '-') {
                sb.append(current);
                pos++;
            }
            while (pos < length && Character.isDigit(input.charAt(pos))) {
                sb.append(input.charAt(pos));
                pos++;
            }
            return new Token(Token.TokenType.NUMBER, sb.toString(), start);
        } else {
            pos++;
            return new Token(Token.TokenType.UNKNOWN, Character.toString(current), pos - 1);
        }
    }

    // Skip any whitespace.
    void skipWhitespace() {
        while (pos < length && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }
}