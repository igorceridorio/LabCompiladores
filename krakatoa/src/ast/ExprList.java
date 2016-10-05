package ast;

import java.util.*;

public class ExprList {

    private ArrayList<Expr> exprList;
	
    public ExprList() {
        exprList = new ArrayList<Expr>();
    }

    public void addElement( Expr expr ) {
        exprList.add(expr);
    }

    public void genC( PW pw ) {

        int size = exprList.size();
        for ( Expr e : exprList ) {
        	e.genC(pw, false);
            if ( --size > 0 )
                pw.print(", ");
        }
    }

    public void genKra(PW pw, boolean ident) {
    	int s = exprList.size();
    	for(Expr e : exprList) {
    		if(e != null) {
    			e.genKra(pw, ident);
    		}
    		if(--s > 0) {
    			pw.print(", ");
    		}
    	}
    }

}
