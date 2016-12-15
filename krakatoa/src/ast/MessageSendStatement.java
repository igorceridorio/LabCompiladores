// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class MessageSendStatement extends Statement { 

	private Expr  messageSend;
	
	public MessageSendStatement(Expr messageSend) {
		this.messageSend = messageSend;
	}

	@Override
	public void genC( PW pw, String className ) {
		pw.printIdent("");
		messageSend.genC(pw, false, className);
		pw.println(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("");
		messageSend.genKra(pw, false);
		pw.println(";");
	}

}


