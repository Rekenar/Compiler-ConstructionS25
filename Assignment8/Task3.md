### Task 3 Operand descriptors – object fields
Consider the expression d.stat.avg. d is a local variable at address 0 pointing to a Dataset object on the heap. The Dataset class has been declared as follows:

class Stat { int min, max, avg; }
class Dataset {
int[] val;
Stat stat;
}

(a) Represent the operand descriptors and generated MJVM instructions of the given expression as on lecture slide 42.

Operand: 
kind=Local, 
adr=0, 
type=Dataset

load0

Resulting Operand:
kind=Stack, 
type=Dataset

Operand (before load): 
kind=Fld, 
adr=1, 
type=Stat

getfield 1

Resulting Operand: 
kind=Stack, 
type=Stat

Operand (before load):
kind=Fld, 
adr=2, 
type=int

getfield 2

Resulting Operand:
kind=Stack, 
type=int


(b) Assume meaningful run-time values for addresses and content of the objects involved. Then represent instruction length (in bytes) and content of the expression stack for each MJVM
instruction implementing the given expression.


The local variable d (at address 0 in the current stack frame) contains the address 1000. This 1000 is a word address on the heap pointing to the start of the Dataset object.

The Dataset object starts at heap address 1000. The Dataset class has a stat field. Let's assume the stat field is stored at offset 1 word within the Dataset object. So, the stat field is located at heap address 1000 + 1 * 4 = 1004 (assuming 1 word = 4 bytes).

The stat field at heap address 1004 contains the address 2000. This 2000 is a word address on the heap pointing to the start of the Stat object.

The Stat object starts at heap address 2000. The Stat class has min, max, and avg fields. Let's assume the avg field is stored at offset 2 words within the Stat object. So, the avg field is located at heap address 2000 + 2*4 = 2008.

Dataset Object is at address 1000.

Stat Object is at address 2000.

The avg field contains the integer value 42.


| Adress 	| Instruction 	| Length 	| EStack 	|
|--------	|-------------	|--------	|--------	|
| 0      	| load0      	| 1      	| 1000     	|
| 1      	| getfield 1   	| 1 + 2    	| 2000     	|
| 2      	| getfield 2   	| 1 + 2    	| 42      	|