package ast;

public class DoWhileStatement extends Statement {

	private CompositeStatement compositeStatement;
	private Expr expr;
	
	public DoWhileStatement(CompositeStatement compositeStatement, Expr expr) {
		this.compositeStatement = compositeStatement;
		this.expr = expr;
	}
	
	@Override
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("do {");
		pw.add();
		compositeStatement.genKra(pw);
		pw.sub();
		pw.printIdent("while(");
		expr.genKra(pw, false);
		pw.println(");");
	}

}
