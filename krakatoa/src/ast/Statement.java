/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/

package ast;

abstract public class Statement {
	
	/*Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” |
	ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement
	DoWhileStat*/

	abstract public void genC(PW pw);
	abstract public void genKra(PW pw);
	
}
