// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class StatementList {

	private ArrayList<Statement> statementList;
	
	public StatementList() {
		statementList = new ArrayList<Statement>();
	}
	
	public void addElement(Statement s) {
		statementList.add(s);
	}
	
	public Iterator<Statement> elements() {
		return statementList.iterator();
	}
	
	public int getSize() {
		return statementList.size();
	}
	
	public void genKra(PW pw) {
		if(!statementList.isEmpty()) {
			for(Statement s : statementList) {
				if(s != null) {
					s.genKra(pw);
				}
			}
		}
	}
	
	public void genC(PW pw, String className) {
		
		// chama cada statement para a geracao do codigo correspondente
		if(!statementList.isEmpty()) {
			for(Statement s: statementList) {
				if(s != null) {
					s.genC(pw, className);
				}
			}
		}
		
	}
}
