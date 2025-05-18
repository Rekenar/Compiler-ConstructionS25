### Task 4 Operand descriptors – array elements
Consider the expression d.val[i+1]. d is the same local variable as in Task 3, i is a local variable at address 1.

class Stat { int min, max, avg; }
class Dataset {
int[] val;
Stat stat;
}

(a) Represent the operand descriptors and generated MJVM instructions of the given expression as
on lecture slide 47.

Operand: 
kind=Local, 
adr=0, 
type=Dataset

load0

Resulting Operand:
kind=Stack, 
type=Dataset

getfield 0






load0, 
getfield 0, 
load1, 
const1, 
add, 
aload




(b) Assume meaningful run-time values for addresses and content of the objects involved. Then
represent instruction length (in bytes) and content of the expression stack for each MJVM
instruction implementing the given expression.


The local variable d (at address 0 in the current stack frame) contains the address 1000. This 1000 is a word address on the heap pointing to the start of the object that d refers to.

The object starts at heap address 1000. Based on the structure for class objects, fields occupy 1 word (4 bytes) and are addressed by word numbers relative to the object's start address. Let's assume the val field is stored at offset 0 words within this object. So, the val field is located at heap address 1000 + 0 * 4 = 1000.

The val field at heap address 1000 contains the address 3000. This 3000 is a word address on the heap pointing to the start of an array object (pointed to by d.val).

The local variable i (at address 1 in the current stack frame) contains the integer value 5.

The array object starts at heap address 3000. Array objects in MicroJava store their length at the beginning, followed by the elements.

The expression requires calculating the index i + 1. With i holding the value 5, the calculated index is 5 + 1 = 6.

We are accessing the array element at index 6. Array elements are located at a heap address derived from the array's base address, the size of elements, and the index, accounting for the length word at the start. For a word array, the element at index i is at heap[base_address + 1 + i] when using word addressing. So, the element at index 6 is at heap[3000 + 1 + 6] = heap using word addressing notation from source. In terms of byte addressing (assuming 4 bytes per word), this corresponds to byte address 3000 + 4 * (1 + 6) = 3000 + 4 * 7 = 3000 + 28 = 3028.

Let's assume the integer value stored at heap address 3028 is 99. This is the final value of the expression d.val[i+1].



| Adress 	| Instruction 	| Length 	| EStack 	|
|--------	|-------------	|--------	|--------	|
| 0      	| load0      	| 1      	| 1000     	|
| 1      	| getfield 0   	| 1 + 2    	| 3000     	|
| 4      	| load1        	| 1     	| 3000, 5  	|
| 5      	| const1        | 1     	| 3000, 5, 1|
| 6      	| add        	| 1     	| 3000, 6  	|
| 7      	| aload        	| 1     	| 99      	|
