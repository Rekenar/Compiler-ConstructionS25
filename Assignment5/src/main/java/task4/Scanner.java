package task4;


import java.io.IOException;
import java.io.Reader;
// --- 2) Lexer ---
class Scanner {
    private final String input;
    private int pos, len;

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
        while (pos < len && Character.isWhitespace(input.charAt(pos)))
            pos++;
        if (pos >= len) return new Token(TokenType.EOF, "");

        char c = input.charAt(pos);
        // identifier or keyword
        if (Character.isLetter(c)) {
            int st = pos;
            while (pos < len && Character.isLetterOrDigit(input.charAt(pos)))
                pos++;
            String w = input.substring(st, pos);
            if (w.equals("print")) {
                return new Token(TokenType.PRINT, w);
            } else {
                return new Token(TokenType.IDENT, w);
            }
        }
        // number
        if (Character.isDigit(c)) {
            int st = pos;
            while (pos < len && Character.isDigit(input.charAt(pos)))
                pos++;
            String num = input.substring(st, pos);
            return new Token(TokenType.NUMBER, num);
        }
        // single-char tokens
        pos++;
        switch (c) {
            case '=': return new Token(TokenType.ASSIGN,    "=");
            case '+': return new Token(TokenType.PLUS,      "+");
            case '-': return new Token(TokenType.MINUS,     "-");
            case '*': return new Token(TokenType.MUL,       "*");
            case '/': return new Token(TokenType.DIV,       "/");
            case '(': return new Token(TokenType.LPAREN,    "(");
            case ')': return new Token(TokenType.RPAREN,    ")");
            case ';': return new Token(TokenType.SEMICOLON, ";");
            default:  return next();  // skip any other char
        }
    }
}