// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import lexer.*;

public class SignalExpr extends Expr {

    public SignalExpr( Symbol oper, Expr expr ) {
       this.oper = oper;
       this.expr = expr;
    }

    @Override
	public void genC( PW pw, boolean putParenthesis, String className ) {
       if ( putParenthesis )
          pw.print("(");
       pw.print( oper == Symbol.PLUS ? "+" : "-" );
       expr.genC(pw, true, className);
       if ( putParenthesis )
          pw.print(")");
    }
    
    public void genKra(PW pw, boolean ident) {	
    	if (ident) {
    		pw.printIdent("");
    	}
    	pw.print(" " + oper.toString());
    	if (expr != null) {
    		expr.genKra(pw, ident);
    	}
    }

    @Override
	public Type getType() {
       return expr.getType();
    }

    private Expr expr;
    private Symbol oper;
}
