package ast;

public class MessageSendToSuper extends MessageSend { 

	MethodDec msg;
	ExprList exprList;
	
	public MessageSendToSuper(MethodDec msg, ExprList exprList) {
		this.msg = msg;
		this.exprList = exprList;
	}
	
    public Type getType() { 
        return msg.getType();
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    
    public void genKra(PW pw, boolean ident) {
		if (ident) pw.printIdent("");
		pw.print("super." + msg.getName() + "(");
		if (exprList != null) exprList.genKra(pw, false);
		pw.print(" )");
    }
    
}