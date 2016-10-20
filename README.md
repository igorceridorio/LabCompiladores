# LabCompiladoresFase1
Coding Project - First Phase. Compilers Lab course - 2016. Federal University of São Carlos - UFSCar Sorocaba.

### Developers: 

- Igor Felipe Ferreira Ceridório
- Rafael Zanetti

### Grammar rules:

```
- [ ] AssignExprLocalDec ::= Expression [ “=” Expression ] | LocalDec
- [x] BasicType ::= “void” | “int” | “boolean” | “String”
- [ ] BasicValue ::= IntValue | BooleanValue | StringValue
- [ ] BooleanValue ::= “true” | “false”
- [x] ClassDec ::= “class” Id [ “extends” Id ] “{” MemberList “}”
- [ ] CompStatement ::= “{” { Statement } “}”
- [ ] Digit ::= “0” | ... | “9”
- [ ] DoWhileStat ::= “do” CompStatement “while” “(” Expression “)”
- [ ] Expression ::= SimpleExpression [ Relation SimpleExpression ]
- [ ] ExpressionList ::= Expression { “,” Expression }
- [ ] Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr
- [x] FormalParamDec ::= ParamDec { “,” ParamDec }
- [ ] HighOperator ::= “∗” | “/” | “&&”
- [x] Id ::= Letter { Letter | Digit | “ ” }
- [x] IdList ::= Id { “,” Id }
- [ ] IfStat ::= “if” “(” Expression “)” Statement [ “else” Statement ]
- [x] InstVarDec ::= Type IdList “;”
- [ ] IntValue ::= Digit { Digit }
- [ ] LeftValue ::= [ (“this” | Id ) “.” ] Id
- [ ] Letter ::= “A” | ... | “Z” | “a” | ... | “z”
- [x] LocalDec ::= Type IdList “;”
- [ ] LowOperator ::= “+” | “−” | “||”
- [x] MemberList ::= { Qualifier Member }
- [x] Member ::= InstVarDec | MethodDec
- [x] MethodDec ::= Type Id “(” [ FormalParamDec ] “)” “{” StatementList “}”
- [x] MOCall ::= “@” Id [ “(” { MOParam } “)” ]
- [x] MOParam ::= IntValue | StringValue | Id
- [ ] ObjectCreation ::= “new” Id “(” “)”
- [x] ParamDec ::= Type Id
- [x] Program ::= { MOCall } ClassDec { ClassDec }
- [x] Qualifier ::= [ "final" ] [ "static" ] ( “private” | “public”)
- [ ] ReadStat ::= “read” “(” LeftValue { “,” LeftValue } “)”
- [ ] PrimaryExpr ::= “super” “.” Id “(” [ ExpressionList ] “)” | Id | Id “.” Id | Id “.” Id “(” [ ExpressionList ] ”)” | Id “.” Id “.” Id “(” [ ExpressionList ] “)” | “this” | “this” “.” Id | “this” ”.” Id “(” [ ExpressionList ] “)” | “this” ”.” Id “.” Id “(” [ ExpressionList ] “)”
- [ ] Relation ::= “==” | “<” | “>” | “<=” | “>=” | “! =”
- [ ] ReturnStat ::= “return” Expression
- [ ] RightValue ::= “this” [ “.” Id ] | Id [ “.” Id ]
- [ ] Signal ::= “+” | “−”
- [ ] SignalFactor ::= [ Signal ] Factor
- [ ] SimpleExpression ::= Term { LowOperator Term }
- [ ] Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” | ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement DoWhileStat
- [x] StatementList ::= { Statement }
- [ ] Term ::= SignalFactor { HighOperator SignalFactor }
- [x] Type ::= BasicType | Id
- [ ] WriteStat ::= “write” “(” ExpressionList “)”
- [ ] WhileStat ::= “while” “(” Expression “)” Statement
```
