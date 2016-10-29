package ast;

public class ParenthesisExpr extends Expr {
    
	private Expr expr;
	
    public ParenthesisExpr( Expr expr ) {
        this.expr = expr;
    }
    
    public Type getType() {
        return expr.getType();
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print("(");
        expr.genC(pw, false);
        pw.printIdent(")");
    }
    
    public void genKra(PW pw, boolean ident) {	
    	if (ident) pw.printIdent("(");
    	else pw.print("(");
    	if (expr != null) expr.genKra(pw, ident);
    	pw.print(")");
    } 

}