package task3;

import java.io.IOException;
import java.io.Reader;

class Scanner {
    private final String input;
    private int pos;
    private final int len;

    Scanner(Reader r) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[4096];
        int n;
        while ((n = r.read(buf)) != -1) sb.append(buf, 0, n);
        input = sb.toString();
        pos = 0; len = input.length();
    }

    Token next() {
        // skip whitespace
        while (pos < len && Character.isWhitespace(input.charAt(pos))) pos++;
        if (pos >= len) return new Token(TokenType.EOF, "");

        char c = input.charAt(pos);
        // identifier or keyword
        if (Character.isLetter(c)) {
            int st = pos;
            while (pos < len && Character.isLetterOrDigit(input.charAt(pos)))
                pos++;
            String w = input.substring(st, pos);
            return switch (w) {
                case "boolean" -> new Token(TokenType.BOOLEAN, w);
                case "char" -> new Token(TokenType.CHAR, w);
                case "int" -> new Token(TokenType.INT, w);
                case "double" -> new Token(TokenType.DOUBLE, w);
                default -> new Token(TokenType.IDENT, w);
            };
        }
        // single‐character tokens
        pos++;
        return switch (c) {
            case ',' -> new Token(TokenType.COMMA, ",");
            case ';' -> new Token(TokenType.SEMICOLON, ";");
            default -> next();  // skip anything else
        };
    }
}