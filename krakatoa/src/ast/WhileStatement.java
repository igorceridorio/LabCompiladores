// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class WhileStatement extends Statement {

	private Expr expr;
	private Statement whilePart;
	
	public WhileStatement(Expr expr, Statement whilePart) {
		this.expr = expr;
		this.whilePart = whilePart;
	}

	@Override
	public void genC(PW pw, String className) {
		pw.printIdent("while (");
		expr.genC(pw, false, className);
		pw.print(")");
		if(whilePart != null) {
			pw.println(" {");
		} else {
			pw.println("");
		}
		pw.add();
		if(whilePart != null) {
			whilePart.genC(pw, className);
		} else {
			pw.printlnIdent(";");
		}
		pw.sub();
		if(whilePart != null) {
			pw.printlnIdent("}");
		} else {
			pw.println("");
		}
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("while (");
		expr.genKra(pw, false);
		pw.print(")");
		if(whilePart != null) {
			pw.println(" {");
		} else {
			pw.println("");
		}
		pw.add();
		if(whilePart != null) {
			whilePart.genKra(pw);
		} else {
			pw.printlnIdent(";");
		}
		pw.sub();
		if(whilePart != null) {
			pw.printlnIdent("}");
		} else {
			pw.println("");
		}
	}	
	
}
