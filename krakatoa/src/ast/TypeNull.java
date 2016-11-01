// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

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
