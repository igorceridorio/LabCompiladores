package ast;

import lexer.Symbol;

public class MethodDec {

	/*MethodDec ::= Qualifier Type Id "(" [ FormalParamDec ] ")" "{" StatementList "}"*/
	
	private Symbol qualifier;
	private Type returnType;
	private String methodName;
	private ParamList formalParamDec;
	private StatementList statementList;
	
	public MethodDec(Symbol qualifier, Type returnType, String methodName, ParamList formalParamDec, StatementList statementList) {
		this.qualifier = qualifier;
		this.returnType = returnType;
		this.formalParamDec = null;
		this.statementList = null;
	}
	
	public Symbol getQualifier() {
		return this.qualifier;
	}
	
	public Type getType() {
		return this.returnType;
	}
	
	public String getName() {
		return this.methodName;
	}
	
	public void setFormalParamDec(ParamList formalParamDec) {
		this.formalParamDec = formalParamDec;
	}
	
	public ParamList getFormalParamDec() {
		return this.formalParamDec;
	}
	
	public void setStatementList(StatementList statementList) {
		this.statementList = statementList;
	}
	
	public StatementList getStatementList() {
		return this.statementList;
	}
	
	public void genKra(PW pw) {
		pw.printIdent("");
		pw.print(qualifier.toString() + " " + returnType.getName() + " " + methodName + "(");
		if(formalParamDec != null) {
			formalParamDec.genKra(pw);
		}
		pw.println(") {");
		pw.add();
		statementList.genKra(pw);
		pw.sub();
		pw.printlnIdent("}");
	} 
	
}
