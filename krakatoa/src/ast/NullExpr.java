/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

public class NullExpr extends Expr {
    
   public Type getType() {
      return new TypeNull();
   }
	
   @Override
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
  
}