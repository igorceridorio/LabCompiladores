// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

public class ObjectCreation extends Expr{

	KraClass newObject;
	
	public ObjectCreation(KraClass newObject) {
		this.newObject = newObject;
	}
	
	@Override
	public Type getType() {
		return newObject;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis, String className) {
		
	}

	@Override
	void genKra(PW pw, boolean ident) {
		if (ident) pw.printIdent("new " + newObject.getName() + "()");
		else pw.print("new " + newObject.getName() + "()");
	}

}
