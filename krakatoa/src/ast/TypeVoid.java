// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class TypeVoid extends Type {
    
    public TypeVoid() {
        super("void");
    }
    
   public String getCname() {
      return "void";
   }

}