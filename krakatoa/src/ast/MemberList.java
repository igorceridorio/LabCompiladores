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
	
}
