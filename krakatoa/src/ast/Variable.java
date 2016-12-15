// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class Variable {

    private String name;
    private Type type;
	
    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { 
    	return name; 
    }
    
    public String getCname() {
    	return "_" + name;
    }

    public Type getType() {
        return type;
    }
    
    public void genC(PW pw) {
    	String varType = this.type.getCname();
    	pw.printlnIdent(varType + " " + getCname() + ";");
    }
    
	public void genKra(PW pw) {
		pw.printlnIdent(this.type.getName() + " " + this.name + ";");
	}
}