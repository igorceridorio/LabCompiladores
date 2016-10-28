package ast;

public class BreakStatement extends Statement {

	@Override
	public void genC(PW pw) {	
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("break;");
	}

}
