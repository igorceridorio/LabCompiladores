package ast;

public class WriteStatement extends Statement {

	private ExprList exprList;
	private boolean line;
	
	public WriteStatement(ExprList exprList) {
		this.exprList = exprList;
		this.line = false;
	}
	
	public boolean isLine() {
		return this.line;
	}
	
	public void setLine(boolean line) {
		this.line = line;
	}
	
	@Override
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		if(this.line) {
			pw.printIdent("writeln(");
		} else {
			pw.printIdent("write(");
		}
		exprList.genKra(pw, false);
		pw.println(");");
	}

}
