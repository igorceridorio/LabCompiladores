// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;


public class MessageSendToSelf extends MessageSend {
  
	Variable v;
	MethodDec m;
	ExprList exprList;
	Type t;
	
	public MessageSendToSelf(Variable v, MethodDec m, ExprList exprList, Type t) {
		super();
		this.v = v;
		this.m = m;
		this.exprList = exprList;
		this.t = t;
	}
	
    public Type getType() { 
        return t;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    }
    
    public void genKra(PW pw, boolean ident) {	
    	if ( ident ) pw.printIdent("");
		pw.print("this");
		if ( v != null) {
			pw.print("." + v.getName());
		}
		if ( m != null ) {
			pw.print("." + m.getName() + "(");
			if ( exprList != null )
				exprList.genKra(pw, ident);
			pw.print(")");
		}
    }
    
    
}