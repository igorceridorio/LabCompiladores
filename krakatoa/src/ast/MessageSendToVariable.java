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
    
    public void genC( PW pw, boolean putParenthesis, String className ) {
        
    	// precisamos saber a que classe o objeto pertence
    	
    	// ESSA LINHA ABAIXO TA CAUSANDO OS ERROS NOS TESTES
    	KraClass classObject = (KraClass)v.getType();
    	
    	pw.print("((" + m.getType().getCname() + " (*)(" + classObject.getCname() + " *");
    	
    	// devemos gerar o codigo relativo aos parametros do metodo (se houver)
    	if(m.getFormalParamDec().getSize() > 0) {
    		pw.print(", ");
    		m.getFormalParamDec().genC(pw);
    	}
    	
    	// realizamos a busca pelo indice do metodo desejado
    	int methodIndex = classObject.getMethodIndex(m.getName(), m.getClass().getName());
    	
    	// chamamos o metodo desejado utilizando um apontador para o indice
    	pw.print(")) " + v.getCname() + "->vt[" + methodIndex + "])((" + classObject.getCname() + "*) " + v.getCname());
    	
    	// devemos gerar o codigo relativo a lista de expressoes do metodo (se houver)
    	if(exprList != null) {
    		pw.print(", ");
    		exprList.genKra(pw, false);
    	}
    	
    	pw.print(")");
    	
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