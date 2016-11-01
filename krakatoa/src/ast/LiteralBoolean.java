// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class LiteralBoolean extends Expr {

    public LiteralBoolean( boolean value ) {
        this.value = value;
    }

    @Override
	public void genC( PW pw, boolean putParenthesis ) {
       pw.print( value ? "1" : "0" );
    }
    
    public void genKra(PW pw, boolean ident) {
    	if(value) {
    		pw.print("true");
    	} else {
    		pw.print("false");
    	}
    }

    @Override
	public Type getType() {
        return Type.booleanType;
    }

    public static LiteralBoolean True  = new LiteralBoolean(true);
    public static LiteralBoolean False = new LiteralBoolean(false);

    private boolean value;
}
