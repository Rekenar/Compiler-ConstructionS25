/* MicroJava Virtual Machine   =========================   The MicroJava VM loads an object file and executes it in the interpreter.   It maintains a global data area, a method call stack and a heap (without garbage collector)   Synopsis: java MJ.Run <fileName>.obj [-debug]   The -debug option traces the interpretation and prints the executed instructions and the   expression stack contents to the console.-------------------------------------------------------------------------------------------*/package MJ;import java.io.*;public class Run {  static final int  	heapSize = 100000,		// size of the heap in words  	mStackSize = 400,			// size of the method call stack in words  	eStackSize = 30;			// size of the expression stack in words  static final int				// instruction codes		load        =  1,		load0       =  2,		load1       =  3,		load2       =  4,		load3       =  5,		store       =  6,		store0      =  7,		store1      =  8,		store2      =  9,		store3      = 10,		getstatic   = 11,		putstatic   = 12,		getfield    = 13,		putfield    = 14,		const0      = 15,		const1      = 16,		const2      = 17,		const3      = 18,		const4      = 19,		const5      = 20,		const_m1    = 21,		const_      = 22,		add         = 23,		sub         = 24,		mul         = 25,		div         = 26,		rem         = 27,		neg         = 28,		shl         = 29,		shr         = 30,		inc         = 31,		new_        = 32,		newarray    = 33,		aload       = 34,		astore      = 35,		baload      = 36,		bastore     = 37,		arraylength = 38,		pop         = 39,		dup         = 40,		dup2        = 41,		jmp         = 42,		jeq         = 43,		jne         = 44,		jlt         = 45,		jle         = 46,		jgt         = 47,		jge         = 48,		call        = 49,		return_     = 50,		enter       = 51,		exit        = 52,		read        = 53,		print       = 54,		bread       = 55,		bprint      = 56,		trap		    = 57;  static final int  // compare operators    eq = 0,    ne = 1,    lt = 2,    le = 3,    gt = 4,    ge = 5;  static String[] opcode = {		"???      ", "load     ", "load0    ", "load1    ", "load2    ",		"load3    ", "store    ", "store0   ", "store1   ", "store2   ",		"store3   ", "getstatic", "putstatic", "getfield ", "putfield ",		"const0   ", "const1   ", "const2   ", "const3   ", "const4   ",		"const5   ", "constm1  ", "const    ", "add      ", "sub      ",		"mul      ", "div      ", "rem      ", "neg      ", "shl      ",		"shr      ", "inc      ", "new      ", "newarray ", "aload    ",		"astore   ", "baload   ", "bastore  ", "arraylen ", "pop      ",		"dup      ", "dup2     ", "jmp      ", "jeq      ", "jne      ",		"jlt      ", "jle      ", "jgt      ", "jge      ", "call     ",		"return   ", "enter    ", "exit     ", "read     ", "print    ",		"bread    ", "bprint   ", "trap     "	};  static byte code[];			// code buffer  static int  data[];			// global data area  static int  heap[];			// heap (without garbage collector)  static int  stack[];		// expression stack  static int  local[];		// method call stack  static int  dataSize;		// size of global data area  static int  startPc;		// address of main() method  static int  pc;					// program counter  static int  fp, sp;			// frame pointer, stack pointer on method call stack  static int  esp;				// expression stack pointer  static int  free;				// next free heap address  static boolean debug;		// switches debug output on or off  //----- expression stack  static void push(int val) throws VMError {		if (esp == eStackSize) throw new VMError("expression stack overflow");		stack[esp++] = val;  }  static int pop() throws VMError {		if (esp == 0) throw new VMError("expression stack underflow");    return stack[--esp];  }  //----- method call stack  static void PUSH(int val) throws VMError {		if (sp == mStackSize) throw new VMError("method stack overflow");		local[sp++] = val;	}	static int POP() throws VMError {		if (sp == 0) throw new VMError("method stack underflow");		return local[--sp];	}	//----- instruction fetch  static byte next() {    return code[pc++];  }  static short next2() {		return (short)(((next() << 8) + (next() & 0xff)) << 16 >> 16);	}  static int next4() {		return (next2() << 16) + (next2() & 0xffff);	}  //----- VM internals	// Loads a MicroJava object file	static void load(String name) throws IOException, FormatException {		byte sig[] = new byte[2];		DataInputStream in = new DataInputStream(new FileInputStream(name));		in.read(sig, 0, 2);		if (sig[0] != 'M' || sig[1] != 'J') throw new FormatException("not a MicroJava file");		int codeSize = in.readInt();		if (codeSize <= 0) throw new FormatException("codeSize <= 0");		dataSize = in.readInt();		if (dataSize < 0) throw new FormatException("dataSize < 0");		startPc = in.readInt();		if (startPc < 0 || startPc >= codeSize) throw new FormatException("startPc out of range");		code = new byte[codeSize];		in.read(code, 0, codeSize);  }  // Allocates a block of size bytes on the heap  static int alloc(int size) throws VMError {    int adr = free;    free += size;    if (free > heapSize) throw new VMError("heap overflow");    return adr;  }	// Retrieves byte n from val. Byte 0 is the most significant byte	static byte getByte(int val, int n) {    return (byte)(val << (8*n) >>> 24);  }  // Replaces byte n in val by b  static int setByte(int val, int n, byte b) {    int delta = (3 - n) * 8;    int mask = ~(255 << delta); // mask all 1 except on chosen byte    int by = (((int)b) & 255) << delta;    return (val & mask) ^ by;  }  // Reads an int from the standard input stream  static int readInt() throws IOException {    int val = 0;    boolean neg = false;    int b = System.in.read();    while (b < '0' || b > '9') {      if (b == '-') neg = !neg;      b = System.in.read();    }    while (b >= '0' && b <= '9') {      val = 10 * val + (b - '0');      b = System.in.read();    }    if (neg) val = -val;    return val;  }	//----- debug output  // Prints val with n characters right-adjusted  static void printNum(int val, int n) {		String s = String.valueOf(val);		int len = s.length();		while (len < n) {System.out.print(" "); len++;}		System.out.print(s);	}  // Prints pc and opcode of current instruction  static void printInstr() {		int op = code[pc - 1];		String instr = op > 0 && op <= trap ? opcode[op] : "???      ";		printNum(pc - 1, 4);		System.out.print(": " + instr + "| ");	}	// Prints the contents of the expression stack	static void printStack() {		for (int i = 0; i < esp; i++) System.out.print(stack[i] + " ");		System.out.println();	}	//----- actual interpretation	// The MicroJava bytecode interpreter	static void interpret() {		int op, adr, val, val2, off, idx, len, i;		pc = startPc;		try {			for (;;) { // terminated by return instruction				op = next();				if (debug) printInstr();				switch((int)op) {					// load/store local variables					case load:						push(local[fp + next()]);						break;					case load0: case load1: case load2: case load3:						op -= load0; // map opcode to range 0..3						push(local[fp + op]);						break;					case store:						local[fp + next()] = pop();						break;					case store0: case store1: case store2: case store3:						op -= store0; // map opcode to range 0..3						local[fp + op] = pop();						break;					// load/store global variables					case getstatic:						push(data[next2()]);						break;					case putstatic:						data[next2()] = pop();						break;					// load/store object fields					case getfield:						adr = pop();						if (adr == 0) throw new VMError("null pointer access");						push(heap[adr + next2()]);						break;					case putfield:						val = pop();						adr = pop();						if (adr == 0) throw new VMError("null pointer access");						heap[adr + next2()] = val;						break;					// load constants					case const0: case const1: case const2: case const3: case const4: case const5:						push(op - const0); // map opcode to 0..5						break;					case const_m1:						push(-1);						break;					case const_:						push(next4());						break;					// arithmetic operations					case add:						push(pop() + pop());						break;					case sub:						push(-pop() + pop());						break;					case mul:						push(pop() * pop());						break;					case div:						val = pop();						if (val == 0) throw new VMError("division by zero");						push(pop() / val);						break;					case rem:						val = pop();						if (val == 0) throw new VMError("division by zero");						push(pop() % val);						break;					case neg:						push(-pop());						break;					case shl:						val = pop();						push(pop() << val);						break;					case shr:						val = pop();						push(pop() >> val);						break;					case inc:						local[fp + next()] += next();						break;					// object creation					case new_:						push(alloc(next2()));						break;					case newarray:						val = next();						len = pop();						if (val == 0) adr = alloc(1 + ((len+3)>>2)); else adr = alloc(1 + len);						heap[adr] = len;						push(adr);						break;					// array access					case aload:						idx = pop();						adr = pop();						if (adr == 0) throw new VMError("null pointer access");						len = heap[adr];						if (idx < 0 || idx >= len) throw new VMError("index out of bounds");						push(heap[adr+1+idx]);						break;					case astore:						val = pop();						idx = pop();						adr = pop();						if (adr == 0) throw new VMError("null pointer access");						len = heap[adr];						if (idx < 0 || idx >= len) throw new VMError("index out of bounds");						heap[adr+1+idx] = val;						break;					case baload:						idx = pop();						adr = pop();						if (adr == 0) throw new VMError("null pointer access");						len = heap[adr];						if (idx < 0 || idx >= len) throw new VMError("index out of bounds");						push(getByte(heap[adr+1+idx/4], idx % 4));						break;					case bastore:						val = pop();						idx = pop();						adr = pop();						if (adr == 0) throw new VMError("null pointer access");						len = heap[adr];						if (idx < 0 || idx >= len) throw new VMError("index out of bounds");						heap[adr+1+idx/4] = setByte(heap[adr+1+idx/4], idx % 4, (byte)val);						break;					case arraylength:						adr = pop();						if (adr==0) throw new VMError("null pointer access");						push(heap[adr]);						break;					// stack manipulation					case pop:						pop();						break;					case dup:						val = pop(); push(val); push(val);						break;					case dup2:						val2 = pop(); val = pop();						push(val); push(val2); push(val); push(val2);						break;					// jumps					case jmp:						adr = next2();						pc = (pc - 3) + adr;						break;					case jeq: case jne: case jlt: case jle: case jgt: case jge:						adr = next2();						val2 = pop(); val = pop();						boolean cond = false;						switch(op) {							case jeq: cond = val == val2; break;							case jne: cond = val != val2; break;							case jlt: cond = val < val2;  break;							case jle: cond = val <= val2; break;							case jgt: cond = val > val2;  break;							case jge: cond = val >= val2; break;						}						if (cond) pc = (pc - 3) + adr;						break;					// method calls					case call:						adr = next2();						PUSH(pc);						pc = (pc - 3) + adr;						break;					case return_:						if (sp == 0) return; else pc = POP();						break;					case enter:						int psize = next();						int lsize = next();						PUSH(fp);						fp = sp;						for (i = 0; i < lsize; i++) PUSH(0);						for (i = psize - 1; i >= 0; i--) local[fp + i] = pop();						break;					case exit:						sp = fp;						fp = POP();						break;					// IO					case read:						try {							val = readInt();							push(val);						} catch (IOException ex) {							throw new VMError("error when reading an integer");						}						break;					case print:						len = pop();						val = pop();						String s = String.valueOf(val);						len = len - s.length();						for (i = 0; i < len; i++) System.out.print(' ');						for (i = 0; i < s.length(); i++) System.out.print(s.charAt(i));						break;					case bread:						try {							push(System.in.read());						} catch (IOException ex) {							throw new VMError("error when reading a character");						}						break;					case bprint:						len = pop() - 1;						val = pop();						for (i = 0; i < len; i++) System.out.print(' ');						System.out.print((char)val);						break;					case trap:						val = next();						if (val == 1)							throw new VMError("end of function without a return");						else							throw new VMError("trap " + val);					default:						throw new VMError("wrong opcode " + op);				}				if (debug) printStack();			}		} catch (VMError e) {			System.out.println("\n-- exception at " + (pc-1) + ": " + e.getMessage());;		}	}	public static void main(String[] arg) {		String fileName = null;		debug = false;		for (int i = 0; i < arg.length; i++) {			if (arg[i].equals("-debug")) debug = true;			else fileName = arg[i];		}		if (fileName == null || arg.length > 2) {			System.out.println("Synopsis: java MJ.Run filename [-debug]");			return;		}		try {			load(fileName);			heap  = new int[heapSize];			// heap			data  = new int[dataSize];			// global data as specified in classfile			stack = new int[eStackSize];		// expression stack			local = new int[mStackSize];		// method stack			fp = 0; sp = 0;			esp = 0;			free = 1;												// no block should start at address 0			long startTime = System.currentTimeMillis();			interpret();			System.out.print("\nCompletion took " + (System.currentTimeMillis()-startTime) + " ms");		} catch (FileNotFoundException e) {			System.out.println("-- file " + fileName + " not found");		} catch (IOException e) {			System.out.println("-- error reading file " + fileName);		} catch (FormatException e) {			System.out.println("-- corrupted object file " + fileName + ": " + e.getMessage());		}	}}class FormatException extends Exception {	FormatException(String s) { super(s); }}class VMError extends Exception {	VMError(String s) { super(s); }}