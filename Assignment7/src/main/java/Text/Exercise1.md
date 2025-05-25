## Class Struct

A new constant would need to be added within the structure kinds to 
identify the float type. 

For example: 
public static final int Float = 5; 
(following the pattern None=0, Int=1, Char=2, Arr=3, Class=4).



## Class Tab

A new static variable would need to be added to predefine an instance of the Struct 
object for the float type. 

For example: public static Struct floatType;.


In the static initialization block (static {}), which builds the "universe" scope, 
the new floatType would need to be instantiated and inserted into the symbol table 
as a Type object:

+ floatType = new Struct(Struct.Float); (using the new Struct.Float kind).
+ insert(Obj.Type, "float", floatType); (using Obj.Type for types and inserting it into the universe scope).



## Class Obj


For constants (Obj.Con), the value is stored in the val field, which is currently of type int.

Either changing the type to double or float, or adding a field for float constants.