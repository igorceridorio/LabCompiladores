/*==============================================================

UFSCar - Universidade Federal de S�o Carlos, campus Sorocaba
Laborat�rio de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Cerid�rio
552380 - Rafael Zanetti

==============================================================*/

package ast;

abstract public class Statement {
	
	/*Statement ::= AssignExprLocalDec �;� | IfStat | WhileStat | ReturnStat �;� |
	ReadStat �;� | WriteStat �;� | �break� �;� | �;� | CompStatement
	DoWhileStat*/

	abstract public void genC(PW pw);
	abstract public void genKra(PW pw);
	
}
