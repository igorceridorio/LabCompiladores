package ast;

abstract public class Expr {
    abstract public void genC( PW pw, boolean putParenthesis );
    abstract public Type getType();
    abstract void genKra(PW pw, boolean ident);
}