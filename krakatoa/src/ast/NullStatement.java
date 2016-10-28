package ast;

public class NullStatement extends Statement{

	@Override
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent(";");
	}

}
