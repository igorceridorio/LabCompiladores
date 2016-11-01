/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

abstract public class Expr {
    abstract public void genC( PW pw, boolean putParenthesis );
    abstract public Type getType();
    abstract void genKra(PW pw, boolean ident);
}