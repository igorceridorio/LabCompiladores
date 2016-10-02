package ast;

public class InstanceVariable extends Variable {

	private boolean isFinal;
	
    public InstanceVariable( String name, Type type, boolean isFinal) {
        super(name, type);
        this.isFinal = isFinal;
    }
    
    public boolean isFinal(){
    	return this.isFinal;
    }
    
    public void genKra(PW pw) {
    	
    }

}