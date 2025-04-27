package task3;

public class Parser {

        private final Scanner scanner;
        private Token token;
        int offset = 0;          // inherited running offset

        Parser(Scanner scanner) { this.scanner = scanner; scan(); }

        void scan()  { token = scanner.next(); }
        void check(TokenType t) {
            if (token.type != t) error("expected " + t + " but found " + token.type);
            scan();
        }
        void error(String msg) {
            throw new RuntimeException("Parse error: " + msg);
        }

        void VarDecList(){
            while (token.type != TokenType.EOF) {
                VarDecl();
            }
        }



        void VarDecl() {
            // ↓offset available in field

            String typeName;
            int unitSize;
            int count;


            Struct t = Type();              // ↑typeName, ↑unitSize
            typeName = t.name;
            unitSize = t.size;

            count    = IdentList();  // ↑count

            check(TokenType.SEMICOLON);

            for (int k = 0; k < count; k++) {
                System.out.printf("%6d %6d %s%n",
                        offset + k*unitSize, unitSize, typeName);
            }
            offset += unitSize * count;     // ↑size accumulated in offset
        }


        int IdentList() {
            int count = 1;

            check(TokenType.IDENT);

            while (token.type == TokenType.COMMA) {
                scan();
                check(TokenType.IDENT);
                count++;
            }
            return count;
        }

        Struct Type() {
            Struct t = new Struct();
            switch (token.type) {
                case BOOLEAN: scan(); t.name = "boolean"; t.size = 1; break;
                case CHAR:    scan(); t.name = "char";    t.size = 2; break;
                case INT:     scan(); t.name = "int";     t.size = 4; break;
                case DOUBLE:  scan(); t.name = "double";  t.size = 8; break;
                default: error("unknown type: " + token.text);
            }
            return t;
        }

}
