/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

public class Member {
	
	/*Member ::= InstVarDec | MethodDec*/

	private InstanceVariableList instanceVariableList;
	private MethodDec methodDec;
	
	public Member(InstanceVariableList instanceVariableList, MethodDec methodDec) {
		this.instanceVariableList = instanceVariableList;
		this.methodDec = methodDec;
	}
	
	public void genKra(PW pw) {
		if(instanceVariableList != null) {
			instanceVariableList.genKra(pw);
		} else {
			methodDec.genKra(pw);
		}
	}
	
}
