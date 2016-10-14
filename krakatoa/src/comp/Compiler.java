
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

	// Program ::= { MOCall } ClassDec { ClassDec }
	private Program program(ArrayList<CompilationError> compilationErrorList) {

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
	
	// MOCall ::=  “@” Id [ “(” { MOParam } “)” ]
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {	
		
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

	/*
	 * ClassDec ::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
	 * MemberList ::= { Qualifier Member } 
	 * Member ::= InstVarDec | MethodDec
	 * Qualifier ::= [ "static" ]  ( "private" | "public" )
	 */
	private KraClass classDec() {
		
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
		
		lexer.nextToken();
		
		// ANALISE SEMANTICA: caso a classe seja 'Program', verifica se possui o metodo 'run()'
		if ((currentClass.getName().equals("Program")) && (currentClass.searchMethod("run", true, false) == null)) {
			signalError.showError("Class 'Program' must have a 'run'");
		}

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
		currentMethod = methodDec;
		
		lexer.nextToken();
		if ( lexer.token != Symbol.RIGHTPAR ) {
			methodDec.setFormalParamDec(formalParamDec());
		}
		
		// ANALISE SEMANTICA
		
		// metodo 'run' 'program' nao deve conter parametros
		if(name.equals("run") && currentClass.getName().equals("Program") && methodDec.getFormalParamDec().getSize() > 0) {
			signalError.showError("Method 'run' of class 'Program' must be parameterless");
		}
		
		// em caso de metodo publico redefinido verifica a validade desta redefinicao
		MethodDec inherited = currentClass.searchMethod(name, true, true);
		if(inherited != null) {
			// verifica se a assinatura dos metodos eh equivalente
			boolean sameParameters = true;
			
			if(currentMethod.getFormalParamDec().getSize() != inherited.getFormalParamDec().getSize()) {
				sameParameters = false;
			} else if(currentMethod.getType() != inherited.getType()) {
				sameParameters = false;
			} else {
				Iterator<Variable> itCurrentMethod = currentMethod.getFormalParamDec().elements();
				Iterator<Variable> itInherited = inherited.getFormalParamDec().elements();
				
				while(itCurrentMethod.hasNext()) {
					if(itCurrentMethod.next().getType() != itInherited.next().getType()) {
						sameParameters = false;
						break;
					}
				}
			}
			
			if(!sameParameters) {
				signalError.showError("Inherited method '" + name + "' of '" + currentClass.getName() + "' has a different signature from its superclass inherited method");
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
			signalError.showError(("Missing ';'"));
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

	private void compositeStatement() {

		lexer.nextToken();
		statementList();
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("} expected");
		else
			lexer.nextToken();
	}

	private StatementList statementList() {
		// CompStatement ::= "{" { Statement } "}"
		
		Symbol tk;
		StatementList statementList = new StatementList();
		
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET && tk != Symbol.ELSE) {
			statementList.addElement(statement());
		}
			
		return statementList;
	}

	private Statement statement() {
		/*
		 * Statement ::= Assignment ``;'' | IfStat |WhileStat | MessageSend
		 *                ``;'' | ReturnStat ``;'' | ReadStat ``;'' | WriteStat ``;'' |
		 *               ``break'' ``;'' | ``;'' | CompStatement | LocalDec
		 */

		switch (lexer.token) {
		case THIS:
		case IDENT:
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:
			assignExprLocalDec();
			break;
		case ASSERT:
			assertStatement();
			break;
		case RETURN:
			returnStatement();
			break;
		case READ:
			readStatement();
			break;
		case WRITE:
			writeStatement();
			break;
		case WRITELN:
			writelnStatement();
			break;
		case IF:
			ifStatement();
			break;
		case BREAK:
			breakStatement();
			break;
		case WHILE:
			whileStatement();
			break;
		case DO:
			doWhileStatement();
			break;
		case SEMICOLON:
			nullStatement();
			break;
		case LEFTCURBRACKET:
			compositeStatement();
			break;
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

	/*
	 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec
	 */
	private Expr assignExprLocalDec() {

		if ( lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
				|| lexer.token == Symbol.STRING ||
				// token é uma classe declarada textualmente antes desta
				// instrução
				(lexer.token == Symbol.IDENT && isType(lexer.getStringValue())) ) {
			/*
			 * uma declaração de variável. 'lexer.token' é o tipo da variável
			 * 
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec 
			 * LocalDec ::= Type IdList ``;''
			 */
			localDec();
		}
		else {
			/*
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ]
			 */
			expr();
			if ( lexer.token == Symbol.ASSIGN ) {
				lexer.nextToken();
				expr();
				if ( lexer.token != Symbol.SEMICOLON )
					signalError.showError("';' expected", true);
				else
					lexer.nextToken();
			}
		}
		return null;
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

	private void whileStatement() {
		//WhileStat ::= “while” “(” Expression “)” Statement
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		expr();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		statement();
	}
	
	private void doWhileStatement() {
		//DoWhileStat ::= “do” CompStatement “while” “(” Expression “)”

		lexer.nextToken(); //le o token "do"
		compositeStatement();
		if ( lexer.token != Symbol.WHILE ) signalError.showError("'while' expected");
		lexer.nextToken(); //le o token "while"
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken(); // le o token "("
		expr();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken(); // le o token ")"
	}

	private void ifStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		expr();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		statement();
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			statement();
		}
	}

	private void returnStatement() {

		lexer.nextToken();
		expr();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
	}

	private void readStatement() {
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		while (true) {
			if ( lexer.token == Symbol.THIS ) {
				lexer.nextToken();
				if ( lexer.token != Symbol.DOT ) signalError.showError(". expected");
				lexer.nextToken();
			}
			if ( lexer.token != Symbol.IDENT )
				signalError.show(ErrorSignaller.ident_expected);

			String name = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token == Symbol.COMMA )
				lexer.nextToken();
			else
				break;
		}

		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
	}

	private void writeStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
	}

	private void writelnStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
	}

	private void breakStatement() {
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
	}

	private void nullStatement() {
		lexer.nextToken();
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

		Expr left = simpleExpr();
		Symbol op = lexer.token;
		if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {
			lexer.nextToken();
			Expr right = simpleExpr();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr simpleExpr() {
		Symbol op;

		Expr left = term();
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
				|| op == Symbol.OR) {
			lexer.nextToken();
			Expr right = term();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr term() {
		Symbol op;

		Expr left = signalFactor();
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
				|| op == Symbol.AND) {
			lexer.nextToken();
			Expr right = signalFactor();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr signalFactor() {
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
			// "(" Expression ")" |
		case LEFTPAR:
			lexer.nextToken();
			anExpr = expr();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(anExpr);

			// "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
			// "!" Factor
		case NOT:
			lexer.nextToken();
			anExpr = expr();
			return new UnaryExpr(anExpr, Symbol.NOT);
			// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");

			String className = lexer.getStringValue();
			/*
			 * // encontre a classe className in symbol table KraClass 
			 *      aClass = symbolTable.getInGlobal(className); 
			 *      if ( aClass == null ) ...
			 */

			lexer.nextToken();
			if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
			lexer.nextToken();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			/*
			 * return an object representing the creation of an object
			 */
			return null;
			/*
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
		case SUPER:
			// "super" "." Id "(" [ ExpressionList ] ")"
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				signalError.showError("'.' expected");
			}
			else
				lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			messageName = lexer.getStringValue();
			/*
			 * para fazer as conferências semânticas, procure por 'messageName'
			 * na superclasse/superclasse da superclasse etc
			 */
			lexer.nextToken();
			exprList = realParameters();
			break;
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
			if ( lexer.token != Symbol.DOT ) {
				// Id
				// retorne um objeto da ASA que representa um identificador
				return null;
			}
			else { // Id "."
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.showError("Identifier expected");
				}
				else {
					// Id "." Id
					lexer.nextToken();
					id = lexer.getStringValue();
					if ( lexer.token == Symbol.DOT ) {
						// Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
						 * se o compilador permite variáveis estáticas, é possível
						 * ter esta opção, como
						 *     Clock.currentDay.setDay(12);
						 * Contudo, se variáveis estáticas não estiver nas especificações,
						 * sinalize um erro neste ponto.
						 */
						lexer.nextToken();
						if ( lexer.token != Symbol.IDENT )
							signalError.showError("Identifier expected");
						messageName = lexer.getStringValue();
						lexer.nextToken();
						exprList = this.realParameters();

					}
					else if ( lexer.token == Symbol.LEFTPAR ) {
						// Id "." Id "(" [ ExpressionList ] ")"
						exprList = this.realParameters();
						/*
						 * para fazer as conferências semânticas, procure por
						 * método 'ident' na classe de 'firstId'
						 */
					}
					else {
						// retorne o objeto da ASA que representa Id "." Id
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
				return null;
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
				}
				else if ( lexer.token == Symbol.DOT ) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
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
					return null;
				}
			}
			break;
		default:
			signalError.showError("Expression expected");
		}
		return null;
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
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

}
