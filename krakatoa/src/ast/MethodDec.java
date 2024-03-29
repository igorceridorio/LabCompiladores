// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import lexer.Symbol;

public class MethodDec {

	/*MethodDec ::= Qualifier Type Id "(" [ FormalParamDec ] ")" "{" StatementList "}"*/
	
	private Symbol qualifier;
	private Type returnType;
	private String methodName;
	private ParamList formalParamDec;
	private StatementList statementList;
	private boolean hasReturn;
	private KraClass kraClass;
	
	public MethodDec(Symbol qualifier, Type returnType, String methodName) {
		this.qualifier = qualifier;
		this.returnType = returnType;
		this.methodName = methodName;
		this.formalParamDec = null;
		this.statementList = null;
		this.hasReturn = false;
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
	
	public void setKraClass(KraClass k) {
		this.kraClass = k;
	}
	
	public KraClass getKraClass() {
		return this.kraClass;
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
	
	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}
	
	public boolean getHasReturn() {
		return this.hasReturn;
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
	
	public void genC(PW pw, KraClass kraClass) {
		
		// gera o codigo para o nome e parametros do metodo
		pw.print(returnType.getCname() + " _" + kraClass.getName() + "_" + methodName + "(" + kraClass.getCname() + " *this");
		if(formalParamDec != null) {
			if(formalParamDec.getSize() > 0) {
				pw.print(", ");
				formalParamDec.genC(pw);
			}
		}
		pw.println(") {");
		
		// gera o codigo dos statments do metodo
		pw.add();
		statementList.genC(pw, kraClass.getName());
		pw.sub();
		pw.println("}");
		pw.println("");
		
	}
	
}
