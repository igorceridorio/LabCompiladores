/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

public class TypeNull extends Type{

	public TypeNull() {
		super("null");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCname() {
		return "NULL";
	}

}
