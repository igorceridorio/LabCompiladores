package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print(literalString);
    }
    
    public void genKra(PW pw, boolean ident) {	
    }
    
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}
