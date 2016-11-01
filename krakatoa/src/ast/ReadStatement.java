/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

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
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("read( ");

		int size = leftValues.getSize();
		
		for (int i = 0; i < size; i++) {
			pw.print(leftValues.get(i).getName());
			if (i < (size - 1)) pw.print(", ");
		}
		
		pw.println(");");
	}
	
}
