# LabCompiladoresFase1
Coding Project - First Phase. Compilers Lab course - 2016. Federal University of São Carlos - UFSCar Sorocaba.

### Developers: 

- Igor Felipe Ferreira Ceridório
- Rafael Zanetti

### Grammar rules:

- [x] ```AssignExprLocalDec ::= Expression [ “=” Expression ] | LocalDec```
- [x] ```BasicType ::= “void” | “int” | “boolean” | “String”```
- [ ] ```BasicValue ::= IntValue | BooleanValue | StringValue```
- [ ] ```BooleanValue ::= “true” | “false”```
- [x] ```ClassDec ::= “class” Id [ “extends” Id ] “{” MemberList “}”```
- [x] ```CompStatement ::= “{” { Statement } “}”```
- [ ] ```Digit ::= “0” | ... | “9”```
- [x] ```DoWhileStat ::= “do” CompStatement “while” “(” Expression “)”```
- [x] ```Expression ::= SimpleExpression [ Relation SimpleExpression ]```
- [x] ```ExpressionList ::= Expression { “,” Expression }```
- [ ] ```Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr```
- [x] ```FormalParamDec ::= ParamDec { “,” ParamDec }```
- [x] ```HighOperator ::= “∗” | “/” | “&&”```
- [x] ```Id ::= Letter { Letter | Digit | “ ” }```
- [x] ```IdList ::= Id { “,” Id }```
- [x] ```IfStat ::= “if” “(” Expression “)” Statement [ “else” Statement ]```
- [x] ```InstVarDec ::= Type IdList “;”```
- [ ] ```IntValue ::= Digit { Digit }```
- [x] ```LeftValue ::= [ (“this” | Id ) “.” ] Id```
- [ ] ```Letter ::= “A” | ... | “Z” | “a” | ... | “z”```
- [x] ```LocalDec ::= Type IdList “;”```
- [x] ```LowOperator ::= “+” | “−” | “||”```
- [x] ```MemberList ::= { Qualifier Member }```
- [x] ```Member ::= InstVarDec | MethodDec```
- [x] ```MethodDec ::= Type Id “(” [ FormalParamDec ] “)” “{” StatementList “}”```
- [x] ```MOCall ::= “@” Id [ “(” { MOParam } “)” ]```
- [x] ```MOParam ::= IntValue | StringValue | Id```
- [ ] ```ObjectCreation ::= “new” Id “(” “)”```
- [x] ```ParamDec ::= Type Id```
- [x] ```Program ::= { MOCall } ClassDec { ClassDec }```
- [x] ```Qualifier ::= [ "final" ] [ "static" ] ( “private” | “public”)```
- [x] ```ReadStat ::= “read” “(” LeftValue { “,” LeftValue } “)”```
- [ ] ```PrimaryExpr ::= “super” “.” Id “(” [ ExpressionList ] “)” | Id | Id “.” Id | Id “.” Id “(” [ ExpressionList ] ”)” | Id “.” Id “.” Id “(” [ ExpressionList ] “)” | “this” | “this” “.” Id | “this” ”.” Id “(” [ ExpressionList ] “)” | ```“this” ”.” I ``` “.” Id “(” [ ExpressionList ] “)”```
- [x] ```Relation ::= “==” | “<” | “>” | “<=” | “>=” | “! =”```
- [x] ```ReturnStat ::= “return” Expression```
- [ ] ```RightValue ::= “this” [ “.” Id ] | Id [ “.” Id ]```
- [x] ```Signal ::= “+” | “−”```
- [x] ```SignalFactor ::= [ Signal ] Factor```
- [x] ```SimpleExpression ::= Term { LowOperator Term }```
- [x] ```Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” | ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement DoWhileStat```
- [x] ```StatementList ::= { Statement }```
- [x] ```Term ::= SignalFactor { HighOperator SignalFactor }```
- [x] ```Type ::= BasicType | Id```
- [x] ```WriteStat ::= “write” “(” ExpressionList “)”```
- [x] ```WhileStat ::= “while” “(” Expression “)” Statement```
