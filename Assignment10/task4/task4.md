27: enter       |                   | enter main method
30: const1      | 1                 | 
31: const3      | 1 3               | 
32: call        | 1 3               | call sum(1,3) min = 1, max = 3 get stored in address 0 and 1 of sum method
0: enter        |                   | enter sum(1,3)


3: const0       | 0                 | 
4: store3       |                   | s = 0


5: load0        | 1                 | 
6: store2       |                   | i = min


7: load2        | 1                 | 
8: load1        | 1 3               | 
9: jgt          |                   | while (1 <= max)


12: load3       | 0                 | 
13: load2       | 0 1               | 
14: add         | 1                 | 
15: store3      |                   | s = s + i


16: inc         |                   | i++


19: jmp         |                   |


7: load2        | 2                 | 
8: load1        | 2 3               | 
9: jgt          |                   | while (1 <= max)


12: load3       | 1                 | 
13: load2       | 1 2               | 
14: add         | 3                 | 
15: store3      |                   | s = s + i


16: inc         |                   | i++


19: jmp         |                   |


7: load2        | 3                 | 
8: load1        | 3 3               | 
9: jgt          |                   | while (1 <= max)


12: load3       | 3                 | 
13: load2       | 3 3               | 
14: add         | 6                 | 
15: store3      |                   | s = s + i


16: inc         |                   | i++


19: jmp         |                   | 


7: load2        | 4                 | 
8: load1        | 4 3               | 
9: jgt          |                   | while (1 <= max)


22: load3       | 6                 | 
23: exit        | 6                 | exit sum method
24: return      | 6                 | return s and go back to where we were in main


35: const0      | 6 0               | 
36: print       | 6                 | print(sum(1, 3))


37: exit        |                   | exit main method
38: return      |                   | no method to go back to, program stops