package task4;


import java.io.FileReader;
import java.io.Reader;
import java.io.FileNotFoundException;

public class MicroJavaParser {
    private static final int  // token codes
            none      = 0,  // error token
            ident     = 1,  // identifier
            number    = 2,  // number
            charCon   = 3,  // character constant
            plus      = 4,  // +
            minus     = 5,  // -
            times     = 6,  // *
            slash     = 7,  // /
            rem       = 8,  // %
            pplus     = 9,  // ++
            mminus    = 10, // --
            eql       = 11, // ==
            neq       = 12, // !=
            lss       = 13, // <
            leq       = 14, // <=
            gtr       = 15, // >
            geq       = 16, // >=
            and       = 17, // &&
            or        = 18, // ||
            lpar      = 19, // (
            rpar      = 20, // )
            lbrack    = 21, // [
            rbrack    = 22, // ]
            lbrace    = 23, // {
            rbrace    = 24, // }
            assign    = 25, // =
            semicolon = 26, // ;
            comma     = 27, // ,
            period    = 28, // .
            break_    = 29, // ... keywords ...
            class_    = 30,
            else_     = 31,
            final_    = 32,
            if_       = 33,
            new_      = 34,
            print_    = 35,
            program_  = 36,
            read_     = 37,
            return_   = 38,
            void_     = 39,
            while_    = 40,
            eof       = 41, // end-of-file token
            power	  = 42;

    private Token currentToken;

    private void nextToken() {
        currentToken = Scanner.next();
    }

    private void expect(String expected) {
        int expectedKind = getTokenKindFor(expected);
        if (currentToken.kind != expectedKind) {
            error("Expected '" + expected + "' but found token of kind " + currentToken.kind);
        }
        nextToken();
    }

    private int getTokenKindFor(String s) {
        switch (s) {
            case "program": return program_;
            case "final":   return final_;
            case "class":   return class_;
            case "{":       return lbrace;
            case "}":       return rbrace;
            case ";":       return semicolon;
            case "=":       return assign;
            case "[":       return lbrack;
            case "]":       return rbrack;
            case ",":       return comma;
            default:
                error("Unknown token mapping for: " + s);
        }
        return 0;
    }

    private void error(String msg) {
        throw new RuntimeException("Syntax error (line " + currentToken.line +
                ", col " + currentToken.col + "): " + msg);
    }

    private boolean isIdentifier() {
        return currentToken.kind == ident;
    }

    private boolean isNumber() {
        return currentToken.kind == number;
    }

    private boolean isCharConst() {
        return currentToken.kind == charCon;
    }

    public void parseProgram() {
        expect("program");
        if (isIdentifier()) {
            nextToken();
        } else {
            error("Expected program identifier");
        }
        parseDeclList();
        expect("{");
        expect("}");
        if (currentToken.kind != eof) {
            error("Extra tokens after program end");
        }
    }


    private void parseDeclList() {
        // The declaration list continues until we encounter the '{' that begins the program block.
        while (currentToken.kind != lbrace) {
            if (currentToken.kind == final_) {
                parseConstDecl();
            } else if (currentToken.kind == class_) {
                parseClassDecl();
            } else if (currentToken.kind == ident) {
                // If it starts with an identifier (and is not "final" or "class"), assume VarDecl.
                parseVarDecl();
            } else {
                error("Unexpected token in declaration list, token kind: " + currentToken.kind);
            }
        }
    }

    private void parseConstDecl() {
        expect("final");
        parseType();
        if (isIdentifier()) {
            nextToken();
        } else {
            error("Expected identifier in constant declaration");
        }
        expect("=");
        parseLiteral();
        expect(";");
    }

    private void parseLiteral() {
        if (isNumber() || isCharConst()) {
            nextToken();
        } else {
            error("Expected literal (number or char constant)");
        }
    }

    private void parseType() {
        if (isIdentifier()) {
            nextToken();
        } else {
            error("Expected type identifier");
        }
        if (currentToken.kind == lbrack) {
            nextToken();
            expect("]");
        }
    }

    private void parseVarDecl() {
        parseType();
        if (isIdentifier()) {
            nextToken();
        } else {
            error("Expected identifier in variable declaration");
        }
        while (currentToken.kind == comma) {
            nextToken();
            if (isIdentifier()) {
                nextToken();
            } else {
                error("Expected identifier after comma in variable declaration");
            }
        }
        expect(";");
    }

    private void parseClassDecl() {
        expect("class");
        if (isIdentifier()) {
            nextToken();
        } else {
            error("Expected class identifier");
        }
        expect("{");
        while (currentToken.kind != rbrace) {
            parseVarDecl();
        }
        expect("}");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java task3.MicroJavaParser <source file>");
            return;
        }
        String filename = args[0];
        try {
            Reader r = new FileReader(filename);
            // Initialize the Scanner with the file reader.
            Scanner.init(r);
            MicroJavaParser parser = new MicroJavaParser();
            parser.nextToken(); // Load the first token.
            parser.parseProgram();
            System.out.println("OK");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
