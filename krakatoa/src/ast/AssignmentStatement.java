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
	
	public boolean validateAssignment() {
	
		// TODO: ha mais duas condicoes nas instrucoes no pdf que talvez precisem ser feitas
		
		//  LeftType is a basic type (int, boolean, or String) and LeftType is RightType
		String leftType = this.leftExpr.getType().getName();
		if((leftType.equals("int") || leftType.equals("boolean") || leftType.equals("String")) && (leftType.equals(this.rightExpr.getType().getName()))) {
			return true;
		}
		
		return false;
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
