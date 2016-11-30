// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.ArrayList;

public class MemberList {
	
	/*MemberList ::= { Qualifier Member }*/
	
	private ArrayList<Member> memberList;
	
	public MemberList(ArrayList<Member> memberList) {
		this.memberList = memberList;
	}
	
	public void genKra(PW pw) {
		if(memberList != null){
			for(Member m: memberList) {
				m.genKra(pw);
			}
		}
	}
	
	public void genC(PW pw, KraClass kraClass) {
		
		// gera os codigos para os metodos da classe passada como parametro
		if(memberList != null) {
			for(Member m: memberList) {
				if(m.getInstanceVariableList() == null) {
					// se as variaveis de instancia sao nulas entao se trata de metodos
					m.genC(pw, kraClass);
				}
			}
		}
	}
	
}
