// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class CompositeStatement extends Statement {

	private StatementList statementList;
	
	public CompositeStatement(StatementList statementList) {
		this.statementList = statementList;
	}
	
	@Override
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		statementList.genKra(pw);
	}

}
