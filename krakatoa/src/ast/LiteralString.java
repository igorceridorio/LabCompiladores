// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti
package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print(literalString);
    }
    
    public void genKra(PW pw, boolean ident) {
    	pw.print("\"" + literalString + "\"");
    }
    
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}
