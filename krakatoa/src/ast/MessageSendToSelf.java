package ast;


public class MessageSendToSelf extends MessageSend {
  
	Variable v;
	MethodDec msg;
	ExprList exprList;
	Type t;
	
	public MessageSendToSelf(Variable v, MethodDec msg, ExprList exprList, Type t) {
		super();
		this.v = v;
		this.msg = msg;
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
		if ( msg != null ) {
			pw.print("." + msg.getName());
			pw.print("(");
			if ( exprList != null )
				exprList.genKra(pw, ident);
			pw.print(")");
		}
    }
    
    
}