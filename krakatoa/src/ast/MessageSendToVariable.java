/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;


public class MessageSendToVariable extends MessageSend { 

    public Type getType() { 
        return null;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    
    public void genKra(PW pw, boolean ident) {	
    }

    
}    