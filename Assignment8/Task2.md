### Task 2 MicroJava byte code
Give the MicroJava byte code implementing the following statement sequence. Assume that x and y
are local variables at addresses 0 and 1, respectively.
For each MJVM instruction, additionally represent its code address (starting at 0), instruction length (in
bytes), and the resulting content of the expression stack at run time, assuming an initial value of 7 for
x. Mark all instructions that will NOT be executed for this value of x.

if (x <= 1) {
    y = 1;
}else if (x % 2 == 0) {
    y = x / 2;
}else {
    y = x * 3 + 1;
}


| Adress 	| Instruction 	| Length 	| EStack 	| Executed  |
|--------	|-------------	|--------	|--------	|--------   |

if (x <= 1)
| 0      	| load0      	| 1      	| 7      	|yes        |
| 1      	| const1      	| 1     	| 7, 1     	|yes        |
| 2      	| jgt 8       	| 1 + 2    	|       	|yes        |

y = 1;
| 5      	| const1        | 1         | 1      	|no         |
| 6      	| store1        | 1         |       	|no         |
| 7     	| jmp 23        | 1 + 2     |       	|no         |

else if (x % 2 == 0)
| 10      	| load0       	| 1     	| 7       	|yes        |
| 11      	| const2      	| 1      	| 2       	|yes        |
| 12     	| rem        	| 1     	| 1     	|yes        |
| 13     	| const0       	| 1     	| 1, 0     	|yes        |
| 14      	| jne 10       	| 1 + 2   	|       	|yes        |

y = x / 2;
| 17      	| load0         | 1         | 7      	|no         |
| 18      	| const2        | 1         | 7, 2    	|no         |
| 19      	| div           | 1         | 3      	|no         |
| 20     	| store1        | 1         |       	|no         |
| 21     	| jmp 9         | 1 + 2     |       	|no         |

y = x * 3 + 1;
| 24      	| load0       	| 1        	| 7      	|yes        |
| 25      	| const3       	| 1   	    | 7, 3     	|yes        |
| 26      	| mul         	| 1   	    | 21      	|yes        |
| 27      	| const1      	| 1 	    | 21, 1    	|yes        |
| 28      	| add    	    | 1         | 22       	|yes        |
| 29      	| store1        | 1         |       	|yes        |
| 30     	|               |           |       	|           |
