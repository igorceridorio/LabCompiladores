// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	private ArrayList<KraClass> classList;
	private ArrayList<MetaobjectCall> metaobjectCallList;
	ArrayList<CompilationError> compilationErrorList;
	
	public Program(ArrayList<KraClass> classList, ArrayList<MetaobjectCall> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}

	public void genKra(PW pw) {
		for(KraClass k : classList) {
			k.genKra(pw);
		}
	}

	public void genC(PW pw) {
		
		// gera a base padrao para todos os programas
		
		// headers 
		pw.println("#include <malloc.h>");
		pw.println("#include <stdlib.h>");
		pw.println("#include <stdio.h>");
		pw.println("");
		
		// define o tipo boolean
		pw.println("typedef int boolean;");
		pw.println("#define true 1");
		pw.println("#define false 0");
		pw.println("");
		
		// define um tipo Func que eh um ponteiro para funcao
		pw.println("typedef");
		pw.add();
		pw.println("void (*Func) ();");
		pw.sub();
		pw.println("");
		
		for(KraClass k : classList) {
			k.genKra(pw);
		}
		
	}
	
	public ArrayList<KraClass> getClassList() {
		return classList;
	}

	public ArrayList<MetaobjectCall> getMetaobjectCallList() {
		return metaobjectCallList;
	}
	
	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}
	
}