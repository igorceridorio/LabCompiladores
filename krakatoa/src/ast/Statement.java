package ast;

abstract public class Statement {
	
	/*Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” |
	ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement
	DoWhileStat*/

	abstract public void genC(PW pw);
	abstract public void genKra(PW pw);
	
}
