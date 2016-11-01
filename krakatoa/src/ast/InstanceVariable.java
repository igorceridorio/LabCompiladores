/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

public class InstanceVariable extends Variable {

//	private boolean isFinal;
	
    public InstanceVariable( String name, Type type) {
        super(name, type);
//        this.isFinal = isFinal;
    }
    
//    public boolean isFinal(){
//    	return this.isFinal;
//    }
    
    public void genKra(PW pw) {
    	
    }

}