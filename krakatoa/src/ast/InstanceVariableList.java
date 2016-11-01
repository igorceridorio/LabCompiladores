// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.*;

public class InstanceVariableList {
	
	private ArrayList<InstanceVariable> instanceVariableList;

    public InstanceVariableList() {
       instanceVariableList = new ArrayList<InstanceVariable>();
    }

    public void addElement(InstanceVariable instanceVariable) {
       instanceVariableList.add( instanceVariable );
    }

    public Iterator<InstanceVariable> elements() {
    	return this.instanceVariableList.iterator();
    }

    public int getSize() {
        return instanceVariableList.size();
    }
    
    public void genKra(PW pw) {
    	pw.printIdent("private");
    	for(InstanceVariable v: instanceVariableList) {
    		pw.println(" " + v.getType().getName() + " " + v.getName() + ";");
    	}
    }

}
