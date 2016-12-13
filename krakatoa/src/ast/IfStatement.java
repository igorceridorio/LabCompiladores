// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class IfStatement extends Statement {

	private Expr expr;
	private Statement thenPart;
	private Statement elsePart;
	
	public IfStatement(Expr expr, Statement thenPart, Statement elsePart) {
		this.expr = expr;
		this.thenPart = thenPart;
		this.elsePart = elsePart;
	}
	
	@Override
	public void genC(PW pw, String className) {
		pw.printIdent("if (");
		expr.genC(pw, false, className);
		pw.println(") {");
		pw.add();
		if(thenPart != null) {
			thenPart.genC(pw, className);
		} else {
			pw.println("");
		}
		pw.sub();
		pw.printIdent("}");
		if(elsePart != null) {
			pw.println(" else { ");
			pw.add();
			elsePart.genC(pw, className);
			pw.sub();
			pw.printlnIdent("}");
		} else {
			pw.println("");
		}
	}
	
	@Override
	public void genKra(PW pw) {
		pw.printIdent("if (");
		expr.genKra(pw, false);
		pw.println(") {");
		pw.add();
		if(thenPart != null) {
			thenPart.genKra(pw);
		} else {
			pw.println("");
		}
		pw.sub();
		pw.printIdent("}");
		if(elsePart != null) {
			pw.println(" else { ");
			pw.add();
			elsePart.genKra(pw);
			pw.sub();
			pw.printlnIdent("}");
		} else {
			pw.println("");
		}
	}
	
}
