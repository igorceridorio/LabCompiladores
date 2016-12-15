// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class MessageSendToSuper extends MessageSend { 

	MethodDec msg;
	ExprList exprList;
	KraClass kraClass;
	
	public MessageSendToSuper(MethodDec msg, ExprList exprList, KraClass kraClass) {
		this.msg = msg;
		this.exprList = exprList;
		this.kraClass = kraClass;
	}
	
    public Type getType() { 
        return msg.getType();
    }
    

    public void genC( PW pw, boolean putParenthesis, String className ) {
    	String methodClassName = kraClass.getSuperclass().getMethod(msg.getName()).getKraClass().getName();
    	String methodClassCName = kraClass.getSuperclass().getMethod(msg.getName()).getKraClass().getCname();
        
    	pw.print("_" + methodClassName + "_" + msg.getName() + "((");
    	pw.print(methodClassCName + "*) this");
    	if ( exprList != null ) {
			pw.print(", ");
			exprList.genC(pw, methodClassName);
		}
		pw.print(")");
    }
    
    public void genKra(PW pw, boolean ident) {
		if (ident) pw.printIdent("");
		pw.print("super." + msg.getName() + "(");
		if (exprList != null) exprList.genKra(pw, false);
		pw.print(")");
    }
    
}