### Task 1 MicroJava byte code
Determine the MicroJava byte code implementing the following statement sequence. Assume that i is
a local variable at address 2, and g is a global variable at address 3.
For each MJVM instruction, additionally represent its code address (starting at 0), instruction length (in
bytes), and the resulting content of the expression stack at run time (for the first loop iteration).

g = 2;
i = 1;
while (i < 10) {
    g = g * i;
    i++;
}


| Adress 	| Instruction 	| Length 	| EStack 	|
|--------	|-------------	|--------	|--------	|

g = 2;
| 0      	| const2      	| 1      	| 2      	|
| 1      	| putstatic 3  	| 1 + 2    	|        	|

i = 1;
| 4      	| const1      	| 1      	| 1      	|
| 5      	| store2       	| 1     	|        	|

while (i < 10)
| 6      	| load2      	| 1      	| 1       	|
| 7     	| const 10  	| 1 + 4    	| 1, 10    	|
| 12      	| jge 17      	| 1 + 2   	|       	|

g = g * i;
| 15      	| getstatic 3  	| 1 + 2   	| 2      	|
| 18      	| load2       	| 1   	    | 2, 1     	|
| 19      	| mul         	| 1   	    | 2      	|
| 20      	| putstatic 3  	| 1 + 2	    |       	|

i++;
| 23      	| inc 2, 1 	    | 1 + 1 + 1 |         	|
| 26      	| jmp -20       | 1 + 2     |       	|
| 29      	|               |           |       	|