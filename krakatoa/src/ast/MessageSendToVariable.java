// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;


public class MessageSendToVariable extends MessageSend { 

	private Variable v;
	private MethodDec m;
	private ExprList exprList;
	
	public MessageSendToVariable(Variable v, MethodDec msg, ExprList exprList) {
		this.v = v;
		this.m = msg;
		this.exprList = exprList;
	}
	
    public Type getType() { 
        return v.getType();
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    
    public void genKra(PW pw, boolean ident) {
    	if (ident) pw.printIdent("");
    	pw.print(v.getName() + "." + m.getName() + "(");
    	if (exprList != null) {
    		exprList.genKra(pw, false);
    	}
    	pw.print(")");
    	
    }

    
}    