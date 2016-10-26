package ast;

public class NullExpr extends Expr {
    
   public void genC( PW pw, boolean putParenthesis ) {
      pw.printIdent("NULL");
   }
   
	@Override
	public void genKra(PW pw, boolean ident) {
		if (ident) {
			pw.printIdent("");
		}
		pw.print("null");
	}
   
   public Type getType() {
      return new TypeNull();
   }
}