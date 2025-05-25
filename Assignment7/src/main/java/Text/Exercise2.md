## Class Struct

Should be working with changes from Exercise1.

## Class Obj

Should be working with changes from Exercise1.

## Class Tab

Should be working with changes from Exercise1.

## Class Parser

The float keyword needs to be added to the list of token codes and names. 

For example, add float_ = 42;

ConstDecl() Method: This method handles final declarations. 
It currently only expects number (for int) or charCon (for char) literals. 
It needs to be extended to recognize floating-point literals.


Factor() Method: This method handles literals (number, charCon). 
It would need to be extended to handle the new float literal token and create an 
Operand with the correct floatType.

Term() and Expr() Methods: These methods parse arithmetic expressions and contain
the crucial type checks for binary operations (+, -, *, /, %).
To enforce the "no mixed expressions" rule, the type check must be modified.

+ Currently, Expr and Term check if(x.type != Tab.intType || y.type != Tab.intType) error("operands must be of type int");. This needs to be updated.

+ The new check should verify that both operands have the same type, and that type is either intType or floatType.
+ Unary minus (-) in Expr() currently checks if (x.type != Tab.intType) error("integer operand required");. This must be extended to also allow float.

