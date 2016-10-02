package ast;

public class Member {
	
	/*Member ::= InstVarDec | MethodDec*/

	private InstanceVariableList instanceVariableList;
	private MethodDec methodDec;
	
	public Member() {
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
