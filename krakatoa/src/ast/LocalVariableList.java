// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.*;

public class LocalVariableList extends Statement {

    private ArrayList<Variable> localList;
	
    public LocalVariableList() {
       localList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       localList.add(v);
    }

    public Iterator<Variable> elements() {
        return localList.iterator();
    }

    public int getSize() {
        return localList.size();
    }

    @Override
    public void genKra(PW pw) {
    	for (Variable var : localList) {
    		if (var != null) {
    			pw.printIdent(var.getType().getName() + " " + var.getName() + ";");
    		}
    	}
    }

	@Override
	public void genC(PW pw) {
	}
    
}