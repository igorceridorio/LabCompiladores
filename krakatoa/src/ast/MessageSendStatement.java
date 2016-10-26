package ast;

public class MessageSendStatement extends Statement { 

	private Expr  messageSend;
	
	public MessageSendStatement(Expr messageSend) {
		this.messageSend = messageSend;
	}

	@Override
	public void genC( PW pw ) {
		pw.printIdent("");
		// messageSend.genC(pw);
		pw.println(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("");
		messageSend.genKra(pw, false);
		pw.println(";");
	}

}


