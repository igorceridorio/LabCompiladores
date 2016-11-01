/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

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
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		leftExpr.genKra(pw, true);
		pw.print(" = ");
		rightExpr.genKra(pw, false);
		pw.print(";");
	}
	
}
