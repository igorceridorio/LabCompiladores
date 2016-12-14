// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class LiteralInt extends Expr {
    
    public LiteralInt( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genC( PW pw, boolean putParenthesis, String className ) {
        pw.print("" + value);
    }
    
    public void genKra(PW pw, boolean ident) {
    	pw.print(Integer.toString(value));
    }
    
    public Type getType() {
        return Type.intType;
    }
    
    private int value;
}
