### Task 3 Compiling assignments
Using the attributed grammar described on lecture slides 40-56, determine the sequence of MJVM
instructions generated for the assignment sequence below. Moreover, for each generated instruction,
give the production and semantic action (method call of Code class) of the attributed grammar that
emits the instruction. Also reference the lecture slide number where each semantic action is visible.

n = 3;
obj = new Dataset;
obj.val = new int[10];
obj.val[0] = n;

n is a local variable at address 0, obj is a global variable at address 1, Dataset is a class with 2 fields,
where val is its first field. All variables and fields have been declared such that the given assignments
are valid.

### n = 3;

const3
store0

Grammar Flow:

Slide 51 – Factor → number

→ Operand x = new Operand(3);

→ Code.load(x); → const3 emitted

Slide 56 – Assignment = Designator "=" Expr ";"

→ Code.assignTo(x); where x.kind = Local

→ emits store0 (address 0 for n)

| Instruction | Semantic Action              | Slide |
| ----------- | ---------------------------- | ----- |
| `const3`    | `Code.load(new Operand(3));` | 39    |
| `store0`    | `Code.assignTo(x);`          | 56    |

### obj = new Dataset;

new 2
putstatic 1

Grammar Flow:

Slide 51 – Factor → "new" ident (class)

→ Code.put(Code.new_); Code.put2(2); (2 = size of Dataset)

→ creates an Operand with kind = Stack

Slide 56 – Assignment = Designator "=" Expr ";"

Designator obj → Operand of kind Static, address 1

Code.assignTo(x); → emits putstatic 1

| Instruction   | Semantic Action                       | Slide |
| ------------- | ------------------------------------- | ----- |
| `new 2`       | `Code.put(Code.new_); Code.put2(2);`  | 51    |
| `putstatic 1` | `Code.assignTo(x);` (Static variable) | 56    |

### obj.val = new int[10];

getstatic 1       ; load obj
const10           ; array size
newarray 1        ; word array
putfield 0        ; field 0 (val)

Grammar Flow:

Designator is obj.val, so:

Slide 40 → load obj from global (→ getstatic 1)

Slide 41 → Fld operand created with adr = 0 (val)

Expr → new int[10]:

Slide 51 → load constant 10 → const10

→ Code.put(Code.newarray); Code.put(1); → word array

Then Code.assignTo(x); where x.kind = Fld → emits putfield 0


| Instruction   | Semantic Action                         | Slide |
| ------------- | --------------------------------------- | ----- |
| `getstatic 1` | `Code.load(objOperand);`                | 40    |
| `const10`     | `Code.load(new Operand(10));`           | 39    |
| `newarray 1`  | `Code.put(Code.newarray); Code.put(1);` | 51    |
| `putfield 0`  | `Code.assignTo(x);` for `Fld` operand   | 56    |

### obj.val[0] = n;

getstatic 1       ; load obj
getfield 0        ; obj.val
const0            ; index 0
load0             ; load n
astore            ; store into array

Slide 41 → load obj from global (getstatic 1), then field (getfield 0)

Slide 44–46 → Designator = a[i]:

Load array address, then index → const0

Slide 38 → load local var n → load0

Slide 56 → Code.assignTo(x); where x.kind = Elem → emits astore

| Instruction   | Semantic Action                           | Slide |
| ------------- | ----------------------------------------- | ----- |
| `getstatic 1` | `Code.load(objOperand);`                  | 40    |
| `getfield 0`  | `Code.load(x);` where `x.kind = Fld`      | 41    |
| `const0`      | `Code.load(new Operand(0));`              | 39    |
| `load0`       | `Code.load(nOperand);`                    | 38    |
| `astore`      | `Code.assignTo(x);` where `x.kind = Elem` | 56    |


Final Instruction Sequence with Annotations

| Step | Instruction   | Purpose                            | Slide | Emitting Action  |
| ---- | ------------- | ---------------------------------- | ----- | ---------------- |
| 1    | `const3`      | load constant 3                    | 39    | `Code.load(...)` |
| 2    | `store0`      | store to local n                   | 56    | `Code.assignTo`  |
| 3    | `new 2`       | allocate Dataset object (2 fields) | 51    | `Code.put(...)`  |
| 4    | `putstatic 1` | store to global obj                | 56    | `Code.assignTo`  |
| 5    | `getstatic 1` | load obj                           | 40    | `Code.load(...)` |
| 6    | `const10`     | load constant 10                   | 39    | `Code.load(...)` |
| 7    | `newarray 1`  | create int\[] array                | 51    | `Code.put(...)`  |
| 8    | `putfield 0`  | store array into obj.val           | 56    | `Code.assignTo`  |
| 9    | `getstatic 1` | load obj                           | 40    | `Code.load(...)` |
| 10   | `getfield 0`  | access obj.val                     | 41    | `Code.load(...)` |
| 11   | `const0`      | index 0                            | 39    | `Code.load(...)` |
| 12   | `load0`       | load local n                       | 38    | `Code.load(...)` |
| 13   | `astore`      | store value in obj.val\[0]         | 56    | `Code.assignTo`  |
