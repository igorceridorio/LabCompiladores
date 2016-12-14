// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.Iterator;

public class ReadStatement extends Statement{

	private VariableList leftValues;
	
	public ReadStatement(VariableList leftValues) {
		this.leftValues = leftValues;
	}
	
	public VariableList getVariableList() {
		return this.leftValues;
	}

	public void setVariableList(VariableList leftValues) {
		this.leftValues = leftValues;
	}

	@Override
	public void genC(PW pw, String className) {
		
		//gera codigo diferente quando o tipo eh inteiro, variavel de instancia ou string
		Iterator<Variable> itLeftValue = leftValues.elements();
		while(itLeftValue.hasNext()) {
			
			Variable var = itLeftValue.next();
			
			// caso seja uma variavel do tipo int ou variavel de instancia
			if(var.getType() == Type.intType || var instanceof InstanceVariable) {
			
				pw.printlnIdent("{");
				pw.add();
				pw.printlnIdent("char __s[512];");
				pw.printlnIdent("gets(__s);");
				pw.printIdent("sscanf(__s, \"%d\", ");
				
				// caso int
				if(var.getType() == Type.intType) {
					pw.println("&_" + var.getName() + ");");
				} else {
					// caso contrario trata-se de uma variavel de instancia
					pw.print("&this->" + var.getType().getCname() + "_" + var.getName() + ");");
				}

			// caso seja uma variavel do tipo string
			} else if(var.getType() == Type.stringType) {
				
				pw.printlnIdent("{");
				pw.add();
				pw.printlnIdent("char __s[512];");
				pw.printlnIdent("gets(__s);");
				
				// caso string
				if(var.getType() == Type.stringType) {
					pw.printlnIdent("_" + var.getName() + " = malloc(strlen(__s) + 1);");
					pw.printlnIdent("strcpy(_" + var.getName() + ", __s);");
				} else {
					// caso contrario trata-se de uma variavel de instancia
					pw.printlnIdent("&this->" + var.getType().getCname() + "_" + var.getName() + 
							" = malloc(strlen(__s) + 1);");
					pw.printlnIdent("strcpy(&this->" + var.getType().getCname() + "_" + 
							var.getName() + ", __s);");
				}
				
			}
						
		}
		
		pw.sub();
		pw.printlnIdent("}");
		
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("read (");

		int size = leftValues.getSize();

		for (int i = 0; i < size; i++) {
			pw.print(leftValues.get(i).getName());
			if (i < (size - 1)) pw.print(", ");
		}

		pw.println(");");
	}
	
}
