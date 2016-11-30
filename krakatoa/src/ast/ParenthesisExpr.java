// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class ParenthesisExpr extends Expr {
    
	private Expr expr;
	
    public ParenthesisExpr( Expr expr ) {
        this.expr = expr;
    }
    
    public Type getType() {
        return expr.getType();
    }
    
    public void genC( PW pw, boolean putParenthesis, String className ) {
        pw.print("(");
        expr.genC(pw, false, className);
        pw.printIdent(")");
    }
    
    public void genKra(PW pw, boolean ident) {	
    	if (ident) {
    		pw.printIdent("(");
    	} else {
    		pw.print("(");
    	}
    	if (expr != null) {
    		expr.genKra(pw, ident);
    	}
    	pw.print(")");
    } 

}