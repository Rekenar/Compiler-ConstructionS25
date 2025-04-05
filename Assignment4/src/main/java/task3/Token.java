package task3;

public class Token {
    // --- Token types ---
    enum TokenType {
        LBRACE,     // {
        RBRACE,     // }
        LBRACKET,   // [
        RBRACKET,   // ]
        COMMA,      // ,
        COLON,      // :
        STRING,     // "..."
        NUMBER,     // e.g., 123, -456
        EOF,        // end-of-file/input
        UNKNOWN     // any unrecognized token
    }

    TokenType type;
    String value;
    int position;  // position in input for error reporting

    Token(TokenType type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    @Override
    public String toString() {
        return (value != null) ? type + "(" + value + ")" : type.toString();
    }
}