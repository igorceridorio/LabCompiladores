/*==============================================================

UFSCar - Universidade Federal de São Carlos, campus Sorocaba
Laboratório de Compiladores - Trabalho 1

408611 - Igor Felipe Ferreira Ceridório
552380 - Rafael Zanetti

==============================================================*/


package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {
	
	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	private KraClass		currentClass;
	private MethodDec		currentMethod;
	private int				whileCounter = 0;

	// compile must receive an input with an character less than p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		// Program ::= { MOCall } ClassDec { ClassDec }

		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		
		try {
			while ( lexer.token == Symbol.MOCall ) {
				metaobjectCallList.add(metaobjectCall());
			}

			kraClassList.add(classDec());
			
			while (lexer.token != Symbol.EOF) {
				if (lexer.token != Symbol.CLASS) {
					signalError.showError("Expected class declaration");
				}
				
				kraClassList.add(classDec());
				
			}
			
			if ( lexer.token != Symbol.EOF ) {
				signalError.showError("End of file expected");
			}
			
		}
		catch( RuntimeException e) {
			// if there was an exception, there is a compilation signalError
		}
		
		// ANALISE SEMANTICA: todo programa deve conter uma classe 'Program'
		if (symbolTable.getInGlobal("Program") == null) {
			signalError.showError("Source code without a class 'Program'");
		}
		
		
		
		return new Program(kraClassList, metaobjectCallList, compilationErrorList);
	}

	/**  parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
     * <code>
     * @ce(5, "'class' expected") <br>
     * clas Program <br>
     *     public void run() { } <br>
     * end <br>
     * </code>
     * 
	 */
	
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
		// MOCall ::=  “@” Id [ “(” { MOParam } “)” ]
		
		String name = lexer.getMetaobjectName();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		
		lexer.nextToken();
		
		if ( lexer.token == Symbol.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING ||
					lexer.token == Symbol.IDENT ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case IDENT:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Symbol.COMMA ) 
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Symbol.RIGHTPAR ) 
				signalError.showError("')' expected after metaobject call with parameters");
			else
				lexer.nextToken();
		}
		if ( name.equals("nce") ) {
			if ( metaobjectParamList.size() != 0 )
				signalError.showError("Metaobject 'nce' does not take parameters");
		}
		else if ( name.equals("ce") ) {
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				signalError.showError("Metaobject 'ce' take three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  )
				signalError.showError("The first parameter of metaobject 'ce' should be an integer number");
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				signalError.showError("The second and third parameters of metaobject 'ce' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )  
				signalError.showError("The fourth parameter of metaobject 'ce' should be a literal string");
			
		}
			
		return new MetaobjectCall(name, metaobjectParamList);
	}

	private KraClass classDec() {
		/*
		 * ClassDec ::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
		 * MemberList ::= { Qualifier Member } 
		 * Member ::= InstVarDec | MethodDec
		 * Qualifier ::= [ "static" ]  ( "private" | "public" )
		 */
		
		String className = null;
		
		if ( lexer.token != Symbol.CLASS ) 
			signalError.showError("'class' expected");
		
		lexer.nextToken();
		
		if ( lexer.token != Symbol.IDENT )
			signalError.show(ErrorSignaller.ident_expected);
		
		className = lexer.getStringValue();
		KraClass kraClass = new KraClass(className);
		symbolTable.putInGlobal(className, kraClass);
		currentClass = kraClass;
		
		lexer.nextToken();
		
		if ( lexer.token == Symbol.EXTENDS ) {
			lexer.nextToken();
			
			if ( lexer.token != Symbol.IDENT )
				signalError.show(ErrorSignaller.ident_expected);
			
			String superclassName = lexer.getStringValue();
			
			// ANALISE SEMANTICA: condicoes para heranca de classes
			
			// verifica se a classe esta tentando herdar de si mesma
			if(className.equals(superclassName)) {
				signalError.showError("Class " + className + " cannot inherit from itself");
			}
			
			// verifica a existencia da superclasse
			KraClass superClass = symbolTable.getInGlobal(superclassName);
			if(superClass == null) {
				signalError.showError("Class " + superclassName + " has not been declared");
			}
			
			// caso passe pela analise a superclasse eh definida
			kraClass.setSuperClass(superClass);
			currentClass.setSuperClass(superClass);

			lexer.nextToken();
		}
		
		if ( lexer.token != Symbol.LEFTCURBRACKET )
			signalError.showError("'{' expected", true);
		lexer.nextToken();
		
		// MemberList ::= { Qualifier Member } 
		ArrayList<Member> memberArray = null;
		
		while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {

			Symbol qualifier;
			switch (lexer.token) {
			case PRIVATE:
				lexer.nextToken();
				qualifier = Symbol.PRIVATE;
				break;
			case PUBLIC:
				lexer.nextToken();
				qualifier = Symbol.PUBLIC;
				break;
			default:
				signalError.showError("'private' or 'public' expected");
				qualifier = Symbol.PUBLIC;
			}
			
			if(memberArray == null) {
				memberArray = new ArrayList<Member>();
			}
			
			Type t = type();
			
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			
			String name = lexer.getStringValue();
			lexer.nextToken();
			
			// ANALISE SEMANTICA: caso name seja 'run' e pertenca a Program verifica se o tipo de retorno eh void
			if(currentClass.getName().equals("Program") && name.equals("run") && t != Type.voidType) {
				signalError.showError("Method 'run' from class 'Program' must be 'void'");
			}

			
			InstanceVariableList instanceVariableList = null;
			MethodDec methodDec = null;
			
			if ( lexer.token == Symbol.LEFTPAR )
				methodDec = methodDec(t, name, qualifier);
			else if ( qualifier != Symbol.PRIVATE ) {
				// ANALISE SEMANTICA: apenas variaveis de instancia privadas podem ser declaradas em uma classe 
				signalError.showError("Attempt to declare a public instance variable");
			} else {
				instanceVariableList = instVarDec(t, name);
			}
			
			memberArray.add(new Member(instanceVariableList, methodDec));
			
		}
		
		// define o memberList para esta kraClass
		MemberList memberList = new MemberList(memberArray);
		kraClass.setMemberList(memberList);
		currentClass.setMemberList(memberList);
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) {
			signalError.showError("'public', 'private' or '}' expected");
		}

		// ANALISE SEMANTICA: caso a classe seja 'Program', verifica se possui o metodo 'run()'
		if (currentClass.getName().equals("Program")){
			if (currentClass.getPublicMethodList() != null) {
				if (currentClass.searchMethod("run", true, false) == null) {
					signalError.showError("Method 'run' was not found in class 'Program'");
				}
			}
		}
		
		lexer.nextToken();

	return kraClass;
	
	}

	private InstanceVariableList instVarDec(Type type, String name) {
		// InstVarDec ::= "private" Type IdList ";"
		// IdList = Id {"," Id}
		
		InstanceVariableList instanceVariableList = new InstanceVariableList();

		// ANALISE SEMANTICA: verifica se nao ha a redeclaracao da variavel de instancia
		if(currentClass.searchInstanceVariable(name) != null) {
			signalError.showError("Instance variable " + name + " has already been declared");
		}
		
		// primeira variavel eh adicionada a currentClass e a instanceVariableList
		currentClass.addInstanceVariable(new InstanceVariable(name, type));
		instanceVariableList.addElement(new InstanceVariable(name, type));
		
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			
			String variableName = lexer.getStringValue();
			
			// ANALISE SEMANTICA: verifica se nao ha a redeclaracao da variavel de instancia
			if (currentClass.searchInstanceVariable(variableName) != null) {
				signalError.showError("Instance variable " + name + " has already been declared");
			}
			
			//adiciona a variavel corrente a currentClass e a instanceVariableList
			currentClass.addInstanceVariable(new InstanceVariable(variableName, type));
			instanceVariableList.addElement(new InstanceVariable(variableName, type));
			
			lexer.nextToken();
			
		}
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return instanceVariableList;
	}

	private MethodDec methodDec(Type type, String name, Symbol qualifier) {
		// MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{" StatementList "}"

		// ANALISE SEMANTICA: 

		// o metodo 'run' de 'Program' deve ser publico
		if(currentClass.getName().equals("Program") && name.equals("run") && qualifier != Symbol.PUBLIC) {
			signalError.showError("Method 'run' of class 'Program' must be public");
		}
		
		// verifica se o metodo nao possui o mesmo nome de uma variavel previamente declarada
		if(currentClass.searchInstanceVariable(name) != null) {
			signalError.showError("Method '" + name + "' cannot have the same name as an instance variable");
		}
		
		// verifica se o metodo nao esta sendo redeclarado
		if(currentClass.searchMethod(name, false, false) != null || currentClass.searchMethod(name, true, false) != null) {
			signalError.showError("Method '" + name + "' is being redeclared");
		}
		
		MethodDec methodDec = new MethodDec(qualifier, type, name);
		
		// adiciona o metodo na lista de metodos da classe atual
		if (qualifier == Symbol.PRIVATE) {
			currentClass.addPrivateMethod(methodDec);
		} else {
			currentClass.addPublicMethod(methodDec);
		}
		
		
		lexer.nextToken();
		if ( lexer.token != Symbol.RIGHTPAR ) {
			methodDec.setFormalParamDec(formalParamDec());
		}
		
		currentMethod = methodDec;
		
		// ANALISE SEMANTICA
		
		// metodo 'run' 'program' nao deve conter parametros
		if(name.equals("run") && currentClass.getName().equals("Program") && currentMethod.getFormalParamDec() != null) {
			signalError.showError("Method 'run' of class 'Program' cannot take parameters");
		}
		
		// em caso de metodo publico redefinido verifica a validade desta redefinicao
		KraClass superClass = currentClass.getSuperclass();
		
		if (superClass != null) {
			MethodDec inherited = superClass.searchMethod(name, true, true);
			if(inherited != null) {
				
				// ANALISE SEMANTICA: verifica se a assinatura dos metodos eh equivalente
				boolean sameParameters = true;
				
				// verifica se ambas as listas nao sao nulas
				if((currentMethod.getFormalParamDec() != null) && (inherited.getFormalParamDec() != null) && (sameParameters)) {				
					// verifica se as listas possuem o mesmo tamanho
					if(currentMethod.getFormalParamDec().getSize() != inherited.getFormalParamDec().getSize()) {
						sameParameters = false;	
					} else {		
						// verifica se os tipos dos metodos sao os mesmos
						if(currentMethod.getType() != inherited.getType()) {
							sameParameters = false;
						} else {		
							// verifica se o retorno de cada metodo eh equivalente
							Iterator<Variable> itCurrentMethod = currentMethod.getFormalParamDec().elements();
							Iterator<Variable> itInherited = inherited.getFormalParamDec().elements();;		
							if(currentMethod.getFormalParamDec().getSize() == inherited.getFormalParamDec().getSize()) {
								while(itCurrentMethod.hasNext()) {
									if(itCurrentMethod.next().getType() != itInherited.next().getType()) {
										sameParameters = false;
										break;
									}
								}
							} else {
								sameParameters = false;
							}
						}
					}
				} else {
					sameParameters = false;
				}
				if(!sameParameters) {
					signalError.showError("Inherited method '" + name + "' of '" + currentClass.getName() + "' has a different signature from its superclass inherited method");
				}
			}
			
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) { 
			signalError.showError(") expected");
		}

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) {
			signalError.showError("'{' expected");
		}

		lexer.nextToken();
		methodDec.setStatementList(statementList());
		currentMethod.setStatementList(methodDec.getStatementList());
		
		// ANALISE SEMANTICA: verifica se um metodo nao void possui de fato retorno
		if(type != Type.voidType && !methodDec.getHasReturn()) {
			signalError.showError("Missing 'return' in method '" + name + "'");
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) {
			signalError.showError("'}' expected");
		}

		// remove todos os identificadores locais da tabela
		symbolTable.removeLocalIdent();
		lexer.nextToken();
		
		return methodDec;
	}

	private LocalVariableList localDec() {
		// LocalDec ::= Type IdList ";"

		LocalVariableList localVariableList = new LocalVariableList();
		Type t = type();
		
		if ( lexer.token != Symbol.IDENT ) {
			signalError.showError("Identifier expected");
		}
		
		String name = lexer.getStringValue();
		Variable v = new Variable(name, t);
		lexer.nextToken();
		
		// ANALISE SEMANTICA: checa se a variavel local esta sendo redeclarada
		if(symbolTable.getInLocal(name) != null) {
			signalError.showError("Variable '" + name + "' is being redeclared");
		}
		
		// caso nao esteja eh entao adicionada a lista de variaveis locais
		symbolTable.putInLocal(name, v);
		localVariableList.addElement(v);
		
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT ) {
				signalError.showError("Identifier expected");
			}
			
			name = lexer.getStringValue();
			v = new Variable(name, t);
			lexer.nextToken();
			
			// ANALISE SEMANTICA: checa se a variavel local esta sendo redeclarada
			if(symbolTable.getInLocal(name) != null) {
				signalError.showError("Variable '" + name + "' is being redeclared");
			}
			
			// caso nao esteja eh entao adicionada a lista de variaveis locais
			symbolTable.putInLocal(name, v);
			localVariableList.addElement(v);	
		}
		
		if (lexer.token != Symbol.SEMICOLON) {
			signalError.showError("Missing ';'");
		}
		
		lexer.nextToken(); //le o token ";"
		
		return localVariableList;
	}

	private ParamList formalParamDec() {
		// FormalParamDec ::= ParamDec { "," ParamDec }

		ParamList paramList = new ParamList();
		paramList.addElement(paramDec());

		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			paramList.addElement(paramDec());
		}
		
		return paramList;
	}

	private Parameter paramDec() {
		// ParamDec ::= Type Id

		Type t= type();
		
		if ( lexer.token != Symbol.IDENT ) {
			signalError.showError("Identifier expected");
		}
		
		// ANALISE SEMANTICA: verifica se a variavel nao esta sendo redeclarada
		String name = lexer.getStringValue();
		lexer.nextToken();
		
		if (symbolTable.getInLocal(name) != null) {
			signalError.showError("Parameter '" + name + "' has already been declared");
		}
		
		Parameter parameter = new Parameter(name, t);
		symbolTable.putInLocal(name, parameter);
			
		return parameter;
	}

	private Type type() {
		// Type ::= BasicType | Id
		
		Type result;

		switch (lexer.token) {
		case VOID:
			result = Type.voidType;
			break;
		case INT:
			result = Type.intType;
			break;
		case BOOLEAN:
			result = Type.booleanType;
			break;
		case STRING:
			result = Type.stringType;
			break;
		case IDENT:
			String name = lexer.getStringValue();
			if (symbolTable.getInGlobal(name) == null) {
				signalError.showError("Type '" + name + "' has not been defined");
				result = Type.undefinedType;
			}
			result = symbolTable.getInGlobal(name);
			break;
		default:
			signalError.showError("Type expected");
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
	}

	private CompositeStatement compositeStatement() {
		// CompStatement ::= “{” { Statement } “}”
		
		lexer.nextToken();
		CompositeStatement compositeStatement = new CompositeStatement(statementList());
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("} expected");
		else
			lexer.nextToken();
		
		return compositeStatement;
	}

	private StatementList statementList() {
		// StatementList ::= { Statement }
		
		Symbol tk;
		StatementList statementList = new StatementList();
		
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET && tk != Symbol.ELSE) {
			statementList.addElement(statement());
		}
			
		return statementList;
	}

	private Statement statement() {
		/*
		 * Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” 
		 * | ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement DoWhileStat
		 * 
		 */

		switch (lexer.token) {
		case THIS:
		case IDENT:
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:
			return assignExprLocalDec();
		case ASSERT:
			return assertStatement();
		case RETURN:
			return returnStatement();
		case READ:
			return readStatement();
		case WRITE:
			return writeStatement();
		case WRITELN:
			return writelnStatement();
		case IF:
			return ifStatement();
		case BREAK:
			return breakStatement();
		case WHILE:
			return whileStatement();
		case DO:
			return doWhileStatement();
		case SEMICOLON:
			return nullStatement();
		case LEFTCURBRACKET:
			return compositeStatement();
		default:
			signalError.showError("Statement expected");
		}
		
		return null;
	}

	private Statement assertStatement() {
		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		Expr e = expr();
		if ( e.getType() != Type.booleanType )
			signalError.showError("boolean expression expected");
		if ( lexer.token != Symbol.COMMA ) {
			this.signalError.showError("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Symbol.LITERALSTRING ) {
			this.signalError.showError("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		if ( lexer.token == Symbol.SEMICOLON )
			lexer.nextToken();
		
		return new StatementAssert(e, lineNumber, message);
	}

	/*
	 * retorne true se 'name' é uma classe declarada anteriormente. É necessário
	 * fazer uma busca na tabela de símbolos para isto.
	 */
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}

	private Statement assignExprLocalDec() {
		// AssignExprLocalDec ::= Expression [ “=” Expression ] | LocalDec
		
		Expr left = null;
		Expr right = null;
					
		if ( lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN || lexer.token == Symbol.STRING || (lexer.token == Symbol.IDENT && isType(lexer.getStringValue())) ) {
			 // AssignExprLocalDec ::=  LocalDec 
			return localDec();
		}
		else {
			// AssignExprLocalDec ::= Expression [ “=” Expression ]
			left = expr();
			
			if ( lexer.token == Symbol.ASSIGN ) {
				lexer.nextToken();
				
				right = expr();
				
				// ANALISE SEMANTICA:
				
				if (right != null) {					
					// verifica se o tipo de uma expressao pode ser convertido no tipo da outra
					if (!isConvertible(left.getType(), right.getType())) {
						signalError.showError("Cannot assign '" + right.getType().getName() + "' type to '" + left.getType().getName() + "'");
					}
				} else {
					// uma expressao nao pode receber null
					signalError.showError("Expression expected in the right side of assignment");
				}
			
				if ( lexer.token != Symbol.SEMICOLON )
					signalError.showError("';' expected", true);
				else
					lexer.nextToken();
			}
		}
		
		if (right != null) {
			return new AssignmentStatement(left, right);
		}
		
		return new MessageSendStatement(left);
	}

	private ExprList realParameters() {
		ExprList anExprList = null;

		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		if ( startExpr(lexer.token) ) anExprList = exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		return anExprList;
	}

	private WhileStatement whileStatement() {
		// WhileStat ::= “while” “(” Expression “)” Statement
		
		// contem o numero de whiles que estao abertos no momento
		whileCounter++;
		
		Expr expr = null;
		Statement statement = null;
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		expr = expr();
		
		// ANALISE SEMANTICA: while deve analisar somente expressoes booleanas
		if (expr.getType() != Type.booleanType) signalError.showError("boolean expression is expected in a 'while' statement");
		
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		statement = statement();
		
		whileCounter--;
		
		return new WhileStatement(expr, statement);
	}
	
	private DoWhileStatement doWhileStatement() {
		//DoWhileStat ::= “do” CompStatement “while” “(” Expression “)”

		CompositeStatement compositeStatement = null;
		Expr expr = null;
		
		lexer.nextToken(); //le o token "do"
		compositeStatement = compositeStatement();
		
		if ( lexer.token != Symbol.WHILE ) signalError.showError("'while' expected");
		lexer.nextToken(); //le o token "while"
		
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken(); // le o token "("
		
		expr = expr();
		
		// ANALISE SEMANTICA: do while aceita apenas expressoes do tipo booleana 
		if (expr.getType() != Type.booleanType) {
			signalError.showError("boolean expression expected in a do-while statement");
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken(); // le o token ")"
		
		return new DoWhileStatement(compositeStatement, expr);
	}

	private IfStatement ifStatement() {
		// IfStat ::= “if” “(” Expression “)” Statement [ “else” Statement ]
		
		Expr expr = null;
		Statement thenPart = null;
		Statement elsePart = null;
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		expr = expr();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		thenPart = statement();
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			elsePart = statement();
		}
		
		return new IfStatement(expr, thenPart, elsePart);
	}

	private ReturnStatement returnStatement() {
		// ReturnStat ::= “return” Expression

		// ANALISE SEMANTICA: caso o metodo atual seja void nao pode existir um return statement
		if (currentMethod.getType() == Type.voidType) {
			signalError.showError("Method '" + currentMethod.getName() + "' is 'void', hence shouldn't have a return");
		}
		
		lexer.nextToken();
		
		Expr expr = expr();
		
		// ANALISE SEMANTICA: verifica se o tipo da expressao eh equivalente ao tipo do retorno
		if (!isConvertible(expr.getType(), currentMethod.getType())) {
			signalError.showError("Statement type is '" + expr.getType().getName() + "' and return type is '" + currentMethod.getType().getName() + "'");
		}
		
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return new ReturnStatement(expr);
	}

	private ReadStatement readStatement() {
		// ReadStat ::= “read” “(” LeftValue { “,” LeftValue } “)”
		// LeftValue ::= [ (“this” | Id ) “.” ] Id
		
		VariableList readList = new VariableList();
		Variable v = null;
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		
		while (true) {
		
			if (lexer.token == Symbol.THIS) {
				// this.Id
				
				lexer.nextToken();
				if ( lexer.token != Symbol.DOT ) signalError.showError(". expected");
				lexer.nextToken();
				
				if (lexer.token != Symbol.IDENT) signalError.show(ErrorSignaller.ident_expected);
				
				// caso onde temos uma variavel de instancia (acessada por this.Id)
				v = currentClass.getInstanceVariable(lexer.getStringValue());
				if (v == null) {
					signalError.showError("Instance variable '" + lexer.getStringValue() + "' has not been declared");
				}
				
				if ( v.getType() == Type.booleanType ) {
					signalError.showError("command 'read' does not accept 'boolean' variables");
				} else {
					readList.addElement(v);
				}
				lexer.nextToken();
				
			} else if (lexer.token == Symbol.IDENT) {
				
				// Id.Id || Id
				
				String n = lexer.getStringValue();
				lexer.nextToken();
				
				if (lexer.token == Symbol.DOT) {
					
					// caso onde a variavel eh do tipo de uma classe (Id.Id)
					
					// checa se a variavel foi previmente declarada
					v = symbolTable.getInLocal(n);
					if (v == null) {
						signalError.showError("Variable '" + n + "' has not been declared");
					}
					
					lexer.nextToken();
					if (lexer.token != Symbol.IDENT) signalError.show(ErrorSignaller.ident_expected);
					
					// verifica se a variavel n esta presente na classe v em questao
					n = lexer.getStringValue();
					
					// checa se a classe contem uma variavel n
					KraClass readClass = symbolTable.getInGlobal(v.getType().getName());
					if (readClass == null) {
						signalError.showError("Variable '" + n + "' does not have a class associated to it");
					}
					
					v = readClass.getInstanceVariable(n);
					if (v == null) {
						signalError.showError("Instance variable '" + n + "' has not been found in type '" + readClass.getName() + "'");
					}
					
					if ( v.getType() == Type.booleanType ) {
						signalError.showError("command 'read' does not accept 'boolean' variables");
					} else {
						readList.addElement(v);
					}
					lexer.nextToken();
					
				} else {
					
					// caso onde a variavel eh acessada diretamente (Id)
					v = symbolTable.getInLocal(n);
					if (v == null) {
						signalError.showError("Variable '" + n + "' has not been declared");
					}
					
					if ( v.getType() == Type.booleanType ) {
						signalError.showError("command 'read' does not accept 'boolean' variables");
					} else {
						readList.addElement(v);
					}
					lexer.nextToken();
					
				}
				
			} else if (lexer.token == Symbol.COMMA) {
				// le o proximo leftValue
				lexer.nextToken();
			} else {
				signalError.showError("Expression expected");
				break;
			}
			
		}

		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		// o comando read deve conter a entrada de ao menos uma variavel
		if (readList.getSize() == 0) {
			signalError.showError("command 'read' should take at least one argument");
		}

		return new ReadStatement(readList);
	}

	private WriteStatement writeStatement() {
		// WriteStat ::= “write” “(” ExpressionList “)”

		ExprList exprList = new ExprList();
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		
		exprList = exprList();
		
		// ANALISE SEMANTICA: 
		int i = 0;
		while (i < exprList.getSize()) {
			
			// write nao suporta nenhuma expressao booleana
			if(exprList.getElement(i).getType() == Type.booleanType) {
				signalError.showError("command 'write' does not accept 'boolean' expressions");
			}
			
			// write nao suporta objetos
			if(exprList.getElement(i).getType() instanceof KraClass) {
				signalError.showError("Command 'write' does not accept objects");
			}
			
			i++;
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return new WriteStatement(exprList, false);
	}

	private WriteStatement writelnStatement() {
		// WriteStat ::= “write” “(” ExpressionList “)”

		ExprList exprList = new ExprList();
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		
		exprList = exprList();
		
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return new WriteStatement(exprList, true);
	}

	private BreakStatement breakStatement() {
		// “break” “;”
		
		// ANALISE SEMANTICA: o break nao pode estar sendo chamado de fora de um while
		if (whileCounter == 0) {
			signalError.showError("'break' statement must be inside a 'while' statement");
		}
		
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return new BreakStatement();
	}

	private NullStatement nullStatement() {
		// “;”
		
		lexer.nextToken();
		return new NullStatement();
	}

	private ExprList exprList() {
		// ExpressionList ::= Expression { "," Expression }

		ExprList anExprList = new ExprList();
		anExprList.addElement(expr());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		return anExprList;
	}
	
	private Expr expr() {
		// Expression ::= SimpleExpression [ Relation SimpleExpression ]
		// Relation ::= “==” | “<” | “>” | “<=” | “>=” | “!=”
		
		Expr left = simpleExpr();
		Symbol op = lexer.token;
		
		if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE || op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {
			lexer.nextToken();
			Expr right = simpleExpr();
			
			// ANALISE SEMANTICA: testa os tipos das expressoes dado os operadores
			
			// apenas podem ser aplicados a valores inteiros
			if (op == Symbol.LT || op == Symbol.LE || op == Symbol.GT || op == Symbol.GE) {
				if (left.getType() == right.getType() && left.getType() != Type.intType) {
					signalError.showError("'" + op.toString() + "' can only be applied to int values");
				}
			}
			
			// podem ser aplicados a booleanos, inteiros ou classes que herdam uma da outra
			if (op == Symbol.EQ || op == Symbol.NEQ) {
				if (!isConvertible(left.getType(), right.getType())) {
					if (!isConvertible(right.getType(), left.getType())) {
						signalError.showError("incompatible type comparation between'" + left.getType().toString() + "' and '" + right.getType().toString() + "'");
					}
				}
			}	

			left = new CompositeExpr(left, op, right);
		}
		
		return left;
	}

	private Expr simpleExpr() {
		// SimpleExpression ::= Term { LowOperator Term }
		// LowOperator ::= "+" | "-" | "||"
		
		Symbol op;
		Expr left = term();
		
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS || op == Symbol.OR) {
			
			lexer.nextToken();
			Expr right = term();
			
			// ANALISE SEMANTICA: checa o tipo das expressoes de acordo com o operador em questao
			if (op == Symbol.MINUS || op == Symbol.PLUS) {
				if (left.getType() != Type.intType || right.getType() != Type.intType) {
					signalError.showError("'" + op.toString() + "' operator supports only 'int' type" );
				}
			} else if (op == Symbol.OR) {
				if (left.getType() != Type.booleanType || right.getType() != Type.booleanType) {
					signalError.showError("'" + op.toString() + "' operator supports only 'boolean' type" );
				}
			}
			
			left = new CompositeExpr(left, op, right);
		}
		
		return left;
	}

	private Expr term() {
		// Term ::= SignalFactor { HighOperator SignalFactor }
		// HighOperator ::= “*” | “/” | “&&”
		
		Symbol op;
		Expr left = signalFactor();
		
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT || op == Symbol.AND) {
			
			lexer.nextToken();
			Expr right = signalFactor();
			
			// ANALISE SEMANTICA: checa o tipo das expressoes de acordo com o operador em questao
			if (op == Symbol.MULT || op == Symbol.DIV) {
				if (left.getType() != Type.intType || right.getType() != Type.intType) {
					signalError.showError("'" + op.toString() + "' operator supports only 'int' type" );
				}
			} else if (op == Symbol.AND) {
				if (left.getType() != Type.booleanType || right.getType() != Type.booleanType) {
					signalError.showError("'" + op.toString() + "' operator supports only 'boolean' type" );
				}
			}
			
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr signalFactor() {
		// SignalFactor ::= [ Signal ] Factor
		// Signal ::= “+” | “-”
		
		Symbol op;
		if ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
			lexer.nextToken();
			return new SignalExpr(op, factor());
		}
		else
			return factor();
	}

	/*
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 *      ObjectCreation | PrimaryExpr
	 * 
	 * BasicValue ::= IntValue | BooleanValue | StringValue 
	 * BooleanValue ::=  "true" | "false" 
	 * ObjectCreation ::= "new" Id "(" ")" 
	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 Id  |
	 *                 Id "." Id | 
	 *                 Id "." Id "(" [ ExpressionList ] ")" |
	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
	 *                 "this" | 
	 *                 "this" "." Id | 
	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
	 */
	private Expr factor() {

		Expr anExpr;
		ExprList exprList;
		String messageName, id;
		KraClass classObj;
		
		// Factor ::= BasicValue
		// BasicValue ::= IntValue | BooleanValue | StringValue
		// BooleanValue ::=  "true" | "false"

		switch (lexer.token) {
		
		// IntValue
		case LITERALINT:
			return literalInt();
		
		// BooleanValue
		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;
		
		// BooleanValue
		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;
		
		// StringValue
		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);
		
		// Factor ::= "(" Expression ")"
		case LEFTPAR:
			lexer.nextToken();
			anExpr = expr();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(anExpr);

		// Factor ::= "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
		
		// Factor ::= "!" Factor
		case NOT:
			lexer.nextToken();
			anExpr = factor();
		
			// ANALISE SEMANTICA: apenas expressoes do tipo boolean podem ser associadas com o operador not
			if (anExpr.getType() != Type.booleanType) signalError.showError("operator '!' just accepts boolean expressions");
			return new UnaryExpr(anExpr, Symbol.NOT);
			
		// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");

			String className = lexer.getStringValue();
			classObj = symbolTable.getInGlobal(className);
			
			// ANALISE SEMANTICA: verifica se a classe informada existe
			if (classObj == null) {
				signalError.showError("class '" + className + "' does not exist");
			}

			lexer.nextToken();
			if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
			lexer.nextToken();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			
			return new ObjectCreation(classObj);
			
		/*
		 * Factor ::= PrimaryExpr
      	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
      	 *                 Id  |
      	 *                 Id "." Id | 
      	 *                 Id "." Id "(" [ ExpressionList ] ")" |
      	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
      	 *                 "this" | 
      	 *                 "this" "." Id | 
      	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
      	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
		 */
		
		// PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"
		case SUPER:
			
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				signalError.showError("'.' expected");
			}
			else
				lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			messageName = lexer.getStringValue();

			// ANALISE SEMANTICA: realiza a busca por 'messageName' nas superclasses
			MethodDec msg = null;
			if (currentClass.getSuperclass() == null) {
				signalError.showError("class '" + currentClass.getName() + "' does not have a superclass");
			} else {
				msg = currentClass.searchMethod(messageName, true, true);
				if (msg == null) {
					signalError.showError("method '" + messageName + "' was not found in superclass");
				}
			}
			
			lexer.nextToken();
			exprList = realParameters();
			
			return new MessageSendToSuper(msg, exprList);
			
		case IDENT:
			/*
          	 * PrimaryExpr ::=  
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
			 */

			String firstId = lexer.getStringValue();
			lexer.nextToken();
			
			// PrimaryExpr ::= Id - logo deve verificar se o identificador local foi previamente declarado
			if ( lexer.token != Symbol.DOT ) {
				Variable v = symbolTable.getInLocal(firstId);
				
				if (v == null) {
					signalError.showError("variable '" + firstId + "' has not been declared");
				}
				
				return new VariableExpr(v);
			}
			else { 
				
				// Id "."
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.showError("Identifier expected");
				}
				else {
					
					// PrimaryExpr ::= Id "." Id
					lexer.nextToken();
					id = lexer.getStringValue();
					
					if ( lexer.token == Symbol.DOT ) {
						// PrimaryExpr ::= Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
						 * se o compilador permite variáveis estáticas, é possível
						 * ter esta opção, como
						 *     Clock.currentDay.setDay(12);
						 * Contudo, se variáveis estáticas não estiver nas especificações,
						 * sinalize um erro neste ponto.
						 */
						
						signalError.showError("compiler does not support static variables");
						
						lexer.nextToken();
						if ( lexer.token != Symbol.IDENT )
							signalError.showError("Identifier expected");
						
						messageName = lexer.getStringValue();
						lexer.nextToken();
						exprList = this.realParameters();

					} else if ( lexer.token == Symbol.LEFTPAR ) {
						// Id "." Id "(" [ ExpressionList ] ")"
						exprList = this.realParameters();
						
						/*
						 * para fazer as conferências semânticas, procure por
						 * método 'id' na classe de 'firstId'
						 */

						// ANALISE SEMANTICA:
						Variable v = null;
						KraClass c = null;
						
						// caso 'firstId' nao seja uma classe
						if ((c = symbolTable.getInGlobal(firstId)) == null) {
							
							// verifica se 'firstId' eh uma variavel local
							if ((v = symbolTable.getInLocal(firstId)) == null) {
								signalError.showError("'" + firstId + "' is neither a class object nor a variable");
							}
							
							// verifica se 'firstId' eh variavel de instancia de alguma classe
							if (v.getType() instanceof KraClass) {
								c = (KraClass) v.getType();
							} else {
								signalError.showError("variable '" + firstId + "' does not have a class type");
							}
							
							// se 'firstId' for um objeto de class verifica se 'id' foi declarado
							MethodDec aux = null;
							if ((aux = c.searchMethod(id, false, true)) == null) {
								if ((aux = c.searchMethod(id, true, true)) == null) {
									signalError.showError("undefined method '" + id + "' in class '" + c.getName() + "'");
								}
							}
							
							if (aux.getQualifier() == Symbol.PRIVATE) {
								signalError.showError("Method '" + id + "' was not found in the public interface of '" + firstId + "' or its superclasses");
							}
							
							
						} else {
							// se 'firstId' for uma KraClass verifica se 'id' foi declarado
							if (c.searchMethod(id, false, true) == null) {
								if (c.searchMethod(id, true, true) == null) {
									signalError.showError("undefined method '" + id + "' in class '" + c.getName() + "'");
								}
							}

						}
						
					} else {
						
						// retorne o objeto da ASA que representa Id "." Id
						KraClass kVariable = symbolTable.getInGlobal(firstId);
						if (kVariable != null) {
							InstanceVariable instanceVariable = kVariable.getInstanceVariable(id);
							if (instanceVariable != null) return new VariableExpr(instanceVariable);
						}
						
						Variable cVariable = symbolTable.getInLocal(firstId);
						if (cVariable == null) {
							signalError.showError("variable '" + firstId + "' was not declared");
						}
						else {
							KraClass kcVariable = symbolTable.getInGlobal(cVariable.getType().getName());
							Variable ckVariable = kcVariable.getInstanceVariable(id);
							if (ckVariable == null) {
								signalError.showError("variable '" + firstId + "' does not exist");
							} else {
								return new VariableExpr(ckVariable); 
							}
						}
					}
				}
			}
			break;
			
		case THIS:
			/*
			 * Este 'case THIS:' trata os seguintes casos: 
          	 * PrimaryExpr ::= 
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// only 'this'
				// retorne um objeto da ASA que representa 'this'
				// confira se não estamos em um método estático
				return new MessageSendToSelf(null, null, null, currentClass);
			}
			else {
				lexer.nextToken();
				if ( lexer.token != Symbol.IDENT )
					signalError.showError("Identifier expected");
				id = lexer.getStringValue();
				lexer.nextToken();
				// já analisou "this" "." Id
				if ( lexer.token == Symbol.LEFTPAR ) {
					// "this" "." Id "(" [ ExpressionList ] ")"
					/*
					 * Confira se a classe corrente possui um método cujo nome é
					 * 'ident' e que pode tomar os parâmetros de ExpressionList
					 */
					exprList = this.realParameters();
					
					msg = currentClass.searchMethod(id, false, true);
					if (msg != null) {
						return new MessageSendToSelf(null, msg, exprList, msg.getType());
					} else {
						msg = currentClass.searchMethod(id, true, true);
						if (msg != null) {
							return new MessageSendToSelf(null, msg, exprList, msg.getType());
						} else {
							signalError.showError("undefined method '" + id + "' in class '" + currentClass.getName() + "'");
						}
					}
					
				}
				else if ( lexer.token == Symbol.DOT ) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
					
					// o compilador nao suporta vairaveis estaticas
					signalError.showError("compiler does not support static variables");
					
					lexer.nextToken();
					if ( lexer.token != Symbol.IDENT )
						signalError.showError("Identifier expected");
					lexer.nextToken();
					exprList = this.realParameters();
				}
				else {
					// retorne o objeto da ASA que representa "this" "." Id
					/*
					 * confira se a classe corrente realmente possui uma
					 * variável de instância 'ident'
					 */
					
					Variable v = currentClass.getInstanceVariable(id);
					if (v == null) {
						signalError.showError("instance variable '" + id + "' has not been declared");
					} else {
						return new MessageSendToSelf(v, null, null, v.getType());
					}
				}
			}
			break;
		default:
			signalError.showError("Expression expected");
		}
		return null;
	}

	private LiteralInt literalInt() {

		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	private static boolean startExpr(Symbol token) {

		return token == Symbol.FALSE || token == Symbol.TRUE
				|| token == Symbol.NOT || token == Symbol.THIS
				|| token == Symbol.LITERALINT || token == Symbol.SUPER
				|| token == Symbol.LEFTPAR || token == Symbol.NULL
				|| token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}
	
	
	// ==========================================================================================
	// FUNCOES AUXILIARES
	// ==========================================================================================
	
	// verifica se um tipo pode ser convertido para outro
	private boolean isConvertible(Type left, Type right) {
		
		// caso left for int, boolean ou String verifica se right possui o mesmo tipo
		if (left == Type.intType || left == Type.booleanType || left == Type.stringType) {
			if (left == right) return true;
			else return false;
		}
		
		KraClass leftClass = null, rightClass = null;
		
		// caso right seja subclasse de left
		if (left instanceof KraClass) leftClass = (KraClass) left;
		else return false;
		
		if (right instanceof KraClass) rightClass = (KraClass) right;
		else return false;
		
		// uma classe eh considerada subclasse de si mesma
		if (leftClass.getName().equals(rightClass.getName())) return true;
		
		if (rightClass.getSuperclass() == null) {
			return false;
		}
		else {
			while (rightClass.getSuperclass() != null) {
				if (leftClass.getName().equals(rightClass.getSuperclass().getName())) return true;
				rightClass = rightClass.getSuperclass();
			}
		}
		
		// caso left seja uma classe mas right seja null
		if (left instanceof KraClass && right == Type.nullType) return true;
		
		return false;
	}

}
