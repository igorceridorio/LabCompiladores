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
    
    public boolean isFinal() {
    	return instanceVariableList.get(0).isFinal();
    }
    
    public boolean isStatic() {
    	return instanceVariableList.get(0).isStatic();
    }
    
    public void genKra(PW pw) {
    	pw.printIdent("");
    	if(isFinal()) {
    		pw.print("final ");
    	}
    	if(isStatic()) {
    		pw.print("static: ");
    	}
    	pw.print("private");
    	for(InstanceVariable v: instanceVariableList) {
    		pw.println(" " + v.getType().getName() + " " + v.getName() + ";");
    	}
    }

}
