// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class ReturnStatement extends Statement {

	private Expr expr;
	
	public ReturnStatement(Expr expr) {
		this.expr = expr;
	}
	
	@Override
	public void genC(PW pw, String className) {
		pw.printIdent("return ");
		expr.genC(pw, false, className);
		pw.println(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("return ");
		expr.genKra(pw, false);
		pw.println(";");
	}

}
