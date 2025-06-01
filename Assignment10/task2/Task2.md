### Task 2
Write a production of an attributed grammar for translating a switch statement to MicroJava byte code.
An example of a switch statement is given below. The switch expression should be restricted to the
integer data type, and case labels should be numbers (constants). Note that cases do NOT contain
break statements, but each case finally jumps to the end of the switch statement (no “fall through”) –
as opposed to semantics in well-known programming languages like Java or C.

switch (x % 5) {
 case 0: y = x / 5; n0++;
 case 1: y = (x-1) / 5; n1++;
 default: y = x;
}

Statement
= "switch" "(" Expr <↑e> ")" (. Code.load(e); .)
  "{" (. Label endLabel = new Label(); .)
    { "case" number <↑val> ":" 
      (. Label caseLabel = new Label();
         Code.put(Code.dup);                     // duplicate expr for comparison
         Code.load(new Operand(val));           // load case constant
         Code.put(Code.jeq); caseLabel.putAdr(); // jump if equal

         caseLabel.here();                       // mark case label
      .)
      StatementList                             // statements inside case
      (. Code.jump(endLabel); .)                // jump to end
    }
    [ "default" ":" 
      (. Code.put(Code.pop); .)                 // clean up duplicated expr
      StatementList                              // default block
    ]
  "}" (. endLabel.here(); .)
;


| Line                          | Bytecode                                               | Purpose |
| ----------------------------- | ------------------------------------------------------ | ------- |
| `Code.load(e)`                | Evaluate and load `switch` expression (e.g. `x % 5`)   |         |
| `Code.put(Code.dup)`          | Keep a copy of the expression for multiple comparisons |         |
| `Code.load(new Operand(val))` | Load the `case` constant for comparison                |         |
| `Code.put(Code.jeq)`          | Jump to case label if expression == constant           |         |
| `caseLabel.here()`            | Marks the start of the case body                       |         |
| `Code.jump(endLabel)`         | Jump to end after executing this case                  |         |
| `Code.put(Code.pop)`          | Discard expression if `default` is executed            |         |


load0              ; x
const5
rem               ; x % 5
dup
const0
jeq case0
dup
const1
jeq case1
pop
jmp default

case0:
load0
const5
div
store1            ; y = x / 5
getstatic n0
inc n0, 1
jmp end

case1:
load0
const1
sub
const5
div
store1            ; y = (x-1)/5
getstatic n1
inc n1, 1
jmp end

default:
load0
store1            ; y = x

end:
