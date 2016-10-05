package ast;

import java.util.ArrayList;

public class ReadStatement extends Statement{

	private ArrayList<String> leftValues;
	
	public ReadStatement(ArrayList<String> leftValues) {
		this.leftValues = leftValues;
	}

	@Override
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("read(");
		int s = leftValues.size();
		for(String v : leftValues) {
			if(v != null) {
				pw.print(v);
			}
			if(s-- > 0) {
				pw.print(", ");
			}
		}
		pw.println(");");
	}
	
}
