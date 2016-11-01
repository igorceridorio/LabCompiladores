// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti
package ast;

public class TypeString extends Type {
    
    public TypeString() {
        super("String");
    }
    
   public String getCname() {
      return "char *";
   }

}