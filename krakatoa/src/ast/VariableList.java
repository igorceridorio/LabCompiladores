// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class VariableList extends Statement{

	private ArrayList<Variable> variableList;
	
    public VariableList() {
    	variableList = new ArrayList<Variable>();
    }
    
    public void addElement(Variable v) {
    	variableList.add(v);
    }

    public Iterator<Variable> elements() {
        return variableList.iterator();
    }

    public int getSize() {
        return variableList.size();
    }	
    
    public Variable get( int i ) {
        return variableList.get(i);
    }
	
	@Override
	public void genC(PW pw, String className) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genKra(PW pw) {
		if ( !variableList.isEmpty() ) {
			for ( Variable v : variableList ) {
				if ( v != null ) {
					v.genKra(pw);
				}
			}
		}
		
	}

}
