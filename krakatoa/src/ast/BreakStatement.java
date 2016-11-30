// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class BreakStatement extends Statement {

	@Override
	public void genC(PW pw, String className) {
		pw.printlnIdent("break;");
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("break;");
	}

}
