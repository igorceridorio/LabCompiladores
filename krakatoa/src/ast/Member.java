// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class Member {
	
	/*Member ::= InstVarDec | MethodDec*/

	private InstanceVariableList instanceVariableList;
	private MethodDec methodDec;
	
	public Member(InstanceVariableList instanceVariableList, MethodDec methodDec) {
		this.instanceVariableList = instanceVariableList;
		this.methodDec = methodDec;
	}
	
	public InstanceVariableList getInstanceVariableList() {
		return this.instanceVariableList;
	}
	
	public void genKra(PW pw) {
		if(instanceVariableList != null) {
			instanceVariableList.genKra(pw);
		} else {
			methodDec.genKra(pw);
		}
	}
	
	public void genC(PW pw, KraClass kraClass) {
		
		// member pode ser ou uma lista de variaveis de instancia ou uma lista de metodos
		
		// caso for uma lista de variaveis de instancia
		if(instanceVariableList != null) {
			instanceVariableList.genC(pw, kraClass.getName());
		} else {
		// caso contrario trata-se de uma lista de metodos
			methodDec.genC(pw, kraClass);
		}
		
	}
	
}
