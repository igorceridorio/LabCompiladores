package ast;

public class NullExpr extends Expr {
    
   public void genC( PW pw, boolean putParenthesis ) {
      pw.printIdent("NULL");
   }
   
   public void genKra(PW pw, boolean ident) {	
   }
   
   public Type getType() {
      return new TypeNull();
   }
}