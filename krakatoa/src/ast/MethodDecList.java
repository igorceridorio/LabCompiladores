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
