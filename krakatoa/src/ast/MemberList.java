/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

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
	
}
