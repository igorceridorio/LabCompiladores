// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti


package ast;

import lexer.*;

public class UnaryExpr extends Expr {

	public UnaryExpr(Expr expr, Symbol op) {
		this.expr = expr;
		this.op = op;
	}

	@Override
	public void genC(PW pw, boolean putParenthesis, String className) {
		switch (op) {
		case PLUS:
			pw.print("+");
			break;
		case MINUS:
			pw.print("-");
			break;
		case NOT:
			pw.print("!");
			break;
		default:
			pw.print(" internal error at UnaryExpr::genC");

		}
		expr.genC(pw, false, className);
	}
	
    public void genKra(PW pw, boolean ident) {
    	if (ident) {
    		pw.printIdent(op.toString());
    	} else {
    		pw.print(op.toString());
    	}
		if ( expr != null ) {
			expr.genKra(pw, ident);
		}
		
    }

	@Override
	public Type getType() {
		return expr.getType();
	}

	private Expr	expr;
	private Symbol	op;
}
