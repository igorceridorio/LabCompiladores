package ast;

public class InstanceVariable extends Variable {

	private boolean isFinal;
	private boolean isStatic;
	
    public InstanceVariable( String name, Type type, boolean isFinal, boolean isStatic ) {
        super(name, type);
        this.isFinal = isFinal;
        this.isStatic = isStatic;
    }
    
    public boolean isFinal(){
    	return this.isFinal;
    }
    
    public boolean isStatic() {
    	return this.isStatic;
    }

}