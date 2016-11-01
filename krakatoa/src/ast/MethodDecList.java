/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class MethodDecList {

	private ArrayList<MethodDec> methodDecList;
	
	public MethodDecList() {
		methodDecList = new ArrayList<MethodDec>();
	}
	
	public int getSize() {
		return methodDecList.size();
	}
	
	public void addElement(MethodDec methodDec) {
		methodDecList.add(methodDec);
	}
	
	public Iterator<MethodDec> elements() {
		return this.methodDecList.iterator();
	}
	
}
