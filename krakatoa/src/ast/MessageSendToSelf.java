// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import lexer.Symbol;

public class MessageSendToSelf extends MessageSend {
  
	Variable v;
	MethodDec m;
	ExprList exprList;
	Type t;
	KraClass kraClass;
	
	public MessageSendToSelf(Variable v, MethodDec m, ExprList exprList, Type t, KraClass kraClass) {
		super();
		this.v = v;
		this.m = m;
		this.exprList = exprList;
		this.t = t;
		this.kraClass = kraClass;
	}
	
    public Type getType() { 
        return t;
    }
    
    public void genC( PW pw, boolean putParenthesis, String className ) {
    	if (v != null) {
    		pw.printIdent("this->_" + className + v.getCname());
    	}
    	if (m != null) {
    		//KraClass thisClass = (KraClass)m.getType();
    		
    		if (m.getQualifier() == Symbol.PUBLIC) {
    			pw.print("( (" + m.getType().getCname() + " (*)(" + kraClass.getCname() + " *" );
    			if (m.getFormalParamDec() != null) {
    				pw.print(", ");
    				m.getFormalParamDec().genC(pw);
    			}
    			pw.print(")) this->vt[" + kraClass.getMethodIndex(m.getName(), className) + "]) ( (" + kraClass.getCname() + "*) this");
    			if (exprList != null) {
    				pw.print(", ");
    				exprList.genC(pw, className);
    			}
    			pw.print(")");
    		}
    		else {
    			pw.print("_" + className + "_" + m.getName() + "(this");
    			if (exprList != null) {
    				pw.print(", ");
    				exprList.genC(pw, className);
    			}
    			pw.print(")");
    		}
    	}
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