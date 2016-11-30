// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

abstract public class Expr {
    abstract public void genC( PW pw, boolean putParenthesis, String className );
    abstract public Type getType();
    abstract void genKra(PW pw, boolean ident);
}