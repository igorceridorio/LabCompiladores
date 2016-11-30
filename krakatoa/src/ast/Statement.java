// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

abstract public class Statement {
	
	/*Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” |
	ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement
	DoWhileStat*/

	abstract public void genC(PW pw, String className);
	abstract public void genKra(PW pw);
	
}
