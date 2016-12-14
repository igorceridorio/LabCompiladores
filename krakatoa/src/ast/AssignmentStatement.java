// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class AssignmentStatement extends Statement{

	private Expr leftExpr;
	private Expr rightExpr;
	
	public AssignmentStatement(Expr leftExpr, Expr rightExpr) {
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
	}
	
	public Expr getLeftExpr() {
		return leftExpr;
	}
	
	public void setLeftExpr(Expr expr) {
		this.leftExpr = expr;
	}
	
	public Expr getRightExpr() {
		return rightExpr;
	}
	
	public void setRightExpr(Expr expr) {
		this.rightExpr = expr;
	}

	@Override
	public void genC(PW pw, String className) {
		
		//geracao de codigo para atribuicoes
		pw.printIdent("");
		leftExpr.genC(pw, true, className);
		pw.print(" = ");
		rightExpr.genC(pw, false, className);
		pw.println(";");
		
	}

	@Override
	public void genKra(PW pw) {
		leftExpr.genKra(pw, true);
		pw.print(" = ");
		rightExpr.genKra(pw, false);
		pw.println(";");
	}
	
}
