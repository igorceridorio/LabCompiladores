// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

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
    
    public int getSize() {
    	return exprList.size();
    }
    
    public Expr getElement(int i) {
    	return exprList.get(i);
    }

    public void genC( PW pw, String className ) {

        int size = exprList.size();
        for ( Expr e : exprList ) {
        	e.genC(pw, false, className);
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
