// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class VariableExpr extends Expr {
	
	private Variable v;
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }
    
    public void genC( PW pw, boolean putParenthesis, String className ) {
        pw.print("_" + v.getName() );
    }
    
    public void genKra(PW pw, boolean ident) {
    	if (ident) {
    		pw.printIdent(v.getName());
    	} else {
    		pw.print(v.getName());
    	}
    }
    
    public Type getType() {
        return v.getType();
    }
    
}