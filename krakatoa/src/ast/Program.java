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
		pw.printlnIdent("void (*Func) ();");
		pw.sub();
		pw.println("");
		
		/* cria um objeto kraclass para armazenar a classe 'Program', isso eh necessario 
		 dado que precisamos depois buscar a posicao correta do metodo 'run' no vetor de 
		 metodos desta classe */
		KraClass classProgram = null;
		
		// realizada a chamada para a geracao das classes
		for(KraClass k : classList) {
			  k.genC(pw);
			  
			  // ao encontrar a classe 'program' armazenamos a mesma
			  if(k.getName().equals("Program")) {
				  classProgram = k;
			  }
		}
		
		// gera o codigo do metodo main do programa
		pw.println("int main() {");
		pw.add();
		pw.printlnIdent("_class_Program *program;");
		pw.printlnIdent("program = new_Program();");
		
		/* recebe o valor relativo ao indice de localizacao do metodo run no vetor 
		 de metoodos da classe 'Program' */
		
		int runIndex = classProgram.getMethodIndex("run", "Program");
		pw.printlnIdent("((void (*)(_class_Program *) ) program->vt[" + runIndex + "])(program);");
		
		pw.printlnIdent("return 0;");
		pw.sub();
		pw.println("}");
		
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