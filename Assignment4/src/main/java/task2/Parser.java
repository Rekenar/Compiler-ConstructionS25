package task2;

import java.util.BitSet;

public class Parser {

    // Token constants
    public static final int T_EOF = 0;
    public static final int T_a   = 1;
    public static final int T_c   = 2;
    public static final int T_d   = 3;
    public static final int T_e   = 4;
    public static final int T_f   = 5;
    public static final int T_g   = 6;

    // The current token
    private int token;

    // Dummy method: advance to the next token in the input.
    private void nextToken() {
    }

    // Matches the expected token and advances; otherwise, reports an error.
    private void match(int expected) {
        if (token == expected) {
            nextToken();
        } else {
            error("Expected " + tokenName(expected) + " but found " + tokenName(token));
        }
    }

    // Reports a syntax error.
    private void error(String msg) {
        System.err.println("Syntax error: " + msg);
    }

    // Recovers by skipping tokens until a token in the successor set is found.
    private void recover(BitSet sux) {
        while (!sux.get(token) && token != T_EOF) {
            nextToken();
        }
    }

    // Returns a human-readable token name.
    private String tokenName(int t) {
        return switch (t) {
            case T_a -> "'a'";
            case T_c -> "'c'";
            case T_d -> "'d'";
            case T_e -> "'e'";
            case T_f -> "'f'";
            case T_g -> "'g'";
            case T_EOF -> "EOF";
            default -> "unknown";
        };
    }

    // ---------------------- Task3.Parser Methods ----------------------------

    // S = A B | C .
    public void parseS(BitSet sux) {
        // Build the local successor set for S.
        BitSet sSux = (BitSet) sux.clone();
        sSux.set(T_EOF); // Ensure EOF is in the follow set.

        if (token == T_a) {
            // For A, the follow set should include FIRST(B) (i.e. 'c' or 'd') and the current sux.
            BitSet aSux = (BitSet) sux.clone();
            aSux.set(T_c); // Because B begins with optional 'c'
            aSux.set(T_d); // Because if 'c' is absent, 'd' must follow
            parseA(aSux);
            parseB(sSux);
        } else if (token == T_e || token == T_f) {
            parseC(sSux);
        } else {
            error("Expected token 'a', 'e', or 'f'");
            recover(sSux);
        }
    }

    // A = "a" { C } .
    public void parseA(BitSet sux) {
        // Local successor set for A: after "a" comes zero or more C's.
        // Since C starts with 'e' or 'f', add those tokens.
        BitSet aSux = (BitSet) sux.clone();
        aSux.set(T_e);
        aSux.set(T_f);

        // Expect literal 'a'
        if (token == T_a) {
            match(T_a);
        } else {
            error("Expected token 'a'");
            recover(aSux);
        }

        // Loop: parse zero or more occurrences of C.
        while (token == T_e || token == T_f) {
            parseC(aSux);
        }
    }

    // B = [ c ] d .
    public void parseB(BitSet sux) {
        // Local successor set for B.
        BitSet bSux = (BitSet) sux.clone();

        if (token == T_c) {
            match(T_c);
        }
        if (token == T_d) {
            match(T_d);
        } else {
            error("Expected token 'd'");
            recover(bSux);
        }
    }

    // C = e | f g .
    public void parseC(BitSet sux) {
        // Local successor set for C.
        BitSet cSux = (BitSet) sux.clone();

        if (token == T_e) {
            match(T_e);
        } else if (token == T_f) {
            match(T_f);
            if (token == T_g) {
                match(T_g);
            } else {
                error("Expected token 'g' after 'f'");
                recover(cSux);
            }
        } else {
            error("Expected token 'e' or 'f'");
            recover(cSux);
        }
    }
}