// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print( v.getName() );
    }
    
    public void genKra(PW pw, boolean ident) {
    	if (ident) {
    		pw.printIdent("");
    	}
    	pw.print(v.getName());
    }
    
    public Type getType() {
        return v.getType();
    }
    
    private Variable v;
}