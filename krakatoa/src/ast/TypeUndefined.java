// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class TypeUndefined extends Type {
    // variables that are not declared have this type
    
   public TypeUndefined() { super("undefined"); }
   
   public String getCname() {
      return "int";
   }
   
}
