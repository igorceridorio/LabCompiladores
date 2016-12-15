# LabCompiladores
Coding Project. Compilers Lab course - 2016. Federal University of São Carlos - UFSCar Sorocaba.

### Developers: 

- Igor Felipe Ferreira Ceridório
- Rafael Zanetti

### Grammar rules:

- [x] ```AssignExprLocalDec ::= Expression [ “=” Expression ] | LocalDec```
- [x] ```BasicType ::= “void” | “int” | “boolean” | “String”```
- [x] ```BasicValue ::= IntValue | BooleanValue | StringValue```
- [x] ```BooleanValue ::= “true” | “false”```
- [x] ```ClassDec ::= “class” Id [ “extends” Id ] “{” MemberList “}”```
- [x] ```CompStatement ::= “{” { Statement } “}”```
- [x] ```Digit ::= “0” | ... | “9”```
- [x] ```DoWhileStat ::= “do” CompStatement “while” “(” Expression “)”```
- [x] ```Expression ::= SimpleExpression [ Relation SimpleExpression ]```
- [x] ```ExpressionList ::= Expression { “,” Expression }```
- [x] ```Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr```
- [x] ```FormalParamDec ::= ParamDec { “,” ParamDec }```
- [x] ```HighOperator ::= “∗” | “/” | “&&”```
- [x] ```Id ::= Letter { Letter | Digit | “ ” }```
- [x] ```IdList ::= Id { “,” Id }```
- [x] ```IfStat ::= “if” “(” Expression “)” Statement [ “else” Statement ]```
- [x] ```InstVarDec ::= Type IdList “;”```
- [x] ```IntValue ::= Digit { Digit }```
- [x] ```LeftValue ::= [ (“this” | Id ) “.” ] Id```
- [x] ```Letter ::= “A” | ... | “Z” | “a” | ... | “z”```
- [x] ```LocalDec ::= Type IdList “;”```
- [x] ```LowOperator ::= “+” | “−” | “||”```
- [x] ```MemberList ::= { Qualifier Member }```
- [x] ```Member ::= InstVarDec | MethodDec```
- [x] ```MethodDec ::= Type Id “(” [ FormalParamDec ] “)” “{” StatementList “}”```
- [x] ```MOCall ::= “@” Id [ “(” { MOParam } “)” ]```
- [x] ```MOParam ::= IntValue | StringValue | Id```
- [x] ```ObjectCreation ::= “new” Id “(” “)”```
- [x] ```ParamDec ::= Type Id```
- [x] ```Program ::= { MOCall } ClassDec { ClassDec }```
- [x] ```Qualifier ::= [ "final" ] [ "static" ] ( “private” | “public”)```
- [x] ```ReadStat ::= “read” “(” LeftValue { “,” LeftValue } “)”```
- [x] ```PrimaryExpr ::= “super” “.” Id “(” [ ExpressionList ] “)” | Id | Id “.” Id | Id “.” Id “(” [ ExpressionList ] ”)” | Id “.” Id “.” Id “(” [ ExpressionList ] “)” | “this” | “this” “.” Id | “this” ”.” Id “(” [ ExpressionList ] “)” | “this” ”.” I ``` “.” Id “(” [ ExpressionList ] “)”```
- [x] ```Relation ::= “==” | “<” | “>” | “<=” | “>=” | “! =”```
- [x] ```ReturnStat ::= “return” Expression```
- [x] ```RightValue ::= “this” [ “.” Id ] | Id [ “.” Id ]```
- [x] ```Signal ::= “+” | “−”```
- [x] ```SignalFactor ::= [ Signal ] Factor```
- [x] ```SimpleExpression ::= Term { LowOperator Term }```
- [x] ```Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” | ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement DoWhileStat```
- [x] ```StatementList ::= { Statement }```
- [x] ```Term ::= SignalFactor { HighOperator SignalFactor }```
- [x] ```Type ::= BasicType | Id```
- [x] ```WriteStat ::= “write” “(” ExpressionList “)”```
- [x] ```WhileStat ::= “while” “(” Expression “)” Statement```

### genC files:

- [x] ```AsssignmentStatement```
- [x] ```BreakStatement```
- [x] ```CompositeExpr```
- [x] ```CompositeStatement```
- [x] ```DoWhileStatement```
- [ ] ```ExprList```
- [x] ```IfStatement```
- [ ] ```InstanceVariableList```
- [ ] ```KraClass```
- [x] ```LiteralBoolean```
- [x] ```LiteralInt```
- [x] ```LiteralString```
- [ ] ```LocalVariableList```
- [x] ```Member```
- [x] ```MemberList```
- [x] ```MessageSendStatement```
- [x] ```MessageSendToSelf```
- [x] ```MessageSendToSuper```
- [x] ```MessageSendToVariable```
- [ ] ```MethodDec```
- [ ] ```NullExpr```
- [ ] ```NullStatement```
- [x] ```ObjectCreation```
- [ ] ```ParamList```
- [x] ```ParenthesisExpr```
- [ ] ```Program```
- [x] ```ReadStatement```
- [x] ```ReturnStatement```
- [x] ```SignalExpr```
- [ ] ```StatementAssert```
- [x] ```StatementList```
- [x] ```UnaryExpr```
- [x] ```Variable```
- [x] ```VariableExpr```
- [ ] ```VariableList```
- [x] ```WhileStatement```
- [x] ```WriteStatement```

### genC tests:

- [x] ```OK-GER01```
- [x] ```OK-GER02```
- [x] ```OK-GER03```
- [x] ```OK-GER04```
- [ ] ```OK-GER05 - problemas na fase 1```
- [x] ```OK-GER06```
- [x] ```OK-GER07```
- [x] ```OK-GER08```
- [x] ```OK-GER09```
- [x] ```OK-GER10```
- [ ] ```OK-GER11 - problemas na fase 1```
- [x] ```OK-GER12```
- [ ] ```OK-GER13```
- [ ] ```OK-GER14```
- [x] ```OK-GER15```
- [ ] ```OK-GER16```
- [ ] ```OK-GER17```
- [ ] ```OK-GER18```
- [ ] ```OK-GER19```
- [ ] ```OK-GER20```
- [ ] ```OK-GER21```
