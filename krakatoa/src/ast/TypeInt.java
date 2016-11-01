// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class TypeInt extends Type {
    
    public TypeInt() {
        super("int");
    }
    
   public String getCname() {
      return "int";
   }

}