/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

import java.util.*;

public class ParamList {
	
	private ArrayList<Variable> paramList;

    public ParamList() {
       paramList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       paramList.add(v);
    }

    public Iterator<Variable> elements() {
        return paramList.iterator();
    }

    public int getSize() {
        return paramList.size();
    }

    public void genKra(PW pw) {
    	for(Variable v: paramList) {
    		pw.print(v.getType().getName() + " " + v.getName());
			if ( v != paramList.get(getSize() - 1) ) {
				pw.print(", ");
			}
    	}
    }

}
