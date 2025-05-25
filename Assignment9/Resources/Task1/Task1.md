### Task 1 Compiling expressions
Using the attributed grammar described on lecture slides 40-51, determine the sequence of MJVM
instructions generated for the expression below. Moreover, represent the concrete syntax tree and
operand descriptors as on lecture slide 52, and explain how operand descriptors are used to generate
the instruction sequence during parsing.

(3 * a[i] + x) % P

a is a local variable at address 0 pointing to an integer array, i is a local variable at address 1, x is a
global variable at address 7, and P is a constant with value 27.

MJVM Instruction Sequence

| Line | Instruction   | Explanation                    |
| ---- | ------------- | ------------------------------ |
| 1    | `const3`      | Push constant 3                |
| 2    | `load0`       | Push address of array `a`      |
| 3    | `load1`       | Push index `i`                 |
| 4    | `aload`       | Load the a[i] from the heap.   |
| 5    | `mul`         | Multiply 3 \* a\[i]            |
| 6    | `getstatic 7` | Load global variable `x`       |
| 7    | `add`         | Add x + result                 |
| 8    | `const 27`    | Push constant P = 27           |
| 9    | `rem`         | Compute remainder (result % P) |


           %
        /     \
      +         P=27 (Con)
    /   \
 *        x (Static @7)
 / \
3 (Con) a[i] (Elem)
        /    \
      a (Local @0)  i (Local @1)


Each subexpression creates an Operand instance.

The Code.load() method uses Operand.kind to decide how to emit the correct instruction:

If Con: emit const

If Local: emit load

If Elem: ensure array + index are on stack, emit aload

If Static: emit getstatic

After loading both operands, Code.put(op) emits add, mul, rem, etc.

Intermediate Operand kinds are always updated to Stack to reflect that they’re now on the expression stack.