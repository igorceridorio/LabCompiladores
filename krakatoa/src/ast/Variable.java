/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

public class Variable {

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }

    public Type getType() {
        return type;
    }

    private String name;
    private Type type;
    
	public void genKra(PW pw) {
		pw.printlnIdent(this.type.getName() + " " + this.name + ";");
	}
}