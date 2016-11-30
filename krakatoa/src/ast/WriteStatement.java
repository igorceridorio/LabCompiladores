// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.Iterator;

public class WriteStatement extends Statement {

	private ExprList exprList;
	private boolean line;
	
	public WriteStatement(ExprList exprList, boolean line) {
		this.exprList = exprList;
		this.line = false;
	}
	
	public boolean isLine() {
		return this.line;
	}
	
	public void setLine(boolean line) {
		this.line = line;
	}
	
	public ExprList getExprList() {
		return this.exprList;
	}
	
	public void setExprList(ExprList exprList) {
		this.exprList = exprList;
	}
	
	@Override
	public void genC(PW pw, String className) {
		
		// gera codigo diferente quando o tipo eh inteiro e quando eh String
		Iterator<Expr> itExpr = exprList.elements();
		while(itExpr.hasNext()) {
			
			Expr expr = itExpr.next();
			
			// caso seja identificado um tipo inteiro
			if(expr.getType() == Type.intType) {
				pw.printIdent("printf(\"%d \", ");
				expr.genC(pw, false, className);
				pw.println(");");
			}
			
			// caso seja identificado um tipo String
			if(expr.getType() == Type.stringType) {
				pw.printIdent("puts(");
				expr.genC(pw, false, className);
				pw.println(");");
			}
			
		}
	}

	@Override
	public void genKra(PW pw) {
		if(this.line) {
			pw.printIdent("writeln (");
		} else {
			pw.printIdent("write (");
		}
		exprList.genKra(pw, false);
		pw.println(");");
	}

}
