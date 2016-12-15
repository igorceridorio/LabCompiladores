// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class KraClass extends Type {
	 
   private KraClass superclass;
   private MemberList memberList;
   private InstanceVariableList instanceVariableList;
   private MethodDecList publicMethodList;
   private MethodDecList privateMethodList;
   private ArrayList<MethodDec> vt;
   
   public KraClass(String name) {
	   super(name);
	   this.superclass = null;
	   this.memberList = null;
	   this.instanceVariableList = new InstanceVariableList();
	   this.publicMethodList = new MethodDecList();
	   this.privateMethodList = new MethodDecList();
	  
   }
   
   public String getCname() {
      return "_class_" + getName();
   }
   
   public String getName() {
	   return super.getName();
   }
   
   public KraClass getSuperclass() {
	   return superclass;
   }
   
   public void setSuperClass(KraClass superclass) {
	   this.superclass = superclass;
   }
   
   public MemberList getMemberList() {
	   return memberList;
   }
   
   public void setMemberList(MemberList memberList) {
	   this.memberList = memberList;
   }
   
   public InstanceVariableList getInstanceVariableList() {
	   return instanceVariableList;
   }
   
   public InstanceVariable getInstanceVariable(String name) {
	   Iterator<InstanceVariable> it = this.instanceVariableList.elements();
		InstanceVariable variable = null;
		
		while (it.hasNext()) {
			variable = it.next();
			if (variable.getName().equals(name)) return variable;
		}
		
		return null;
   }
   
   public void addInstanceVariable(InstanceVariable instanceVariable) {
	   this.instanceVariableList.addElement(instanceVariable);
   }
   
   public InstanceVariable searchInstanceVariable(String variableName) {
	   Iterator<InstanceVariable> it = this.instanceVariableList.elements();
	   InstanceVariable instanceVarAux = null;
	   
	   // nao precisamos buscar na superclasse pois todas as variaveis sao privadas
	   while(it.hasNext()) {
		   instanceVarAux = it.next();
		   if(instanceVarAux.getName().equals(variableName)) {
			   return instanceVarAux;
		   }
	   }
	   
	   //caso nao encontre, nao ha variavel com esse nome disponivel
	   return null;
   }
   
   public MethodDecList getPublicMethodList() {
	   return publicMethodList;
   }
   
   public void addPublicMethod(MethodDec methodDec) {
	   this.publicMethodList.addElement(methodDec);
   }
   
   public MethodDecList getPrivateMethodList() {
	   return privateMethodList;
   }
   
   public void addPrivateMethod(MethodDec methodDec) {
	   this.privateMethodList.addElement(methodDec);
   }
   
   public MethodDec searchMethod(String methodName, boolean isPublic, boolean searchInSuper) {
	   Iterator<MethodDec> it = null;
	   MethodDec methodDecAux = null;

	   // primeiramente define em qual lista realizara a busca
	   if(isPublic) {
		   it = this.publicMethodList.elements();
	   } else {
		   it = this.privateMethodList.elements();
	   }
	   
	   // em seguida realiza a busca na lista desejada
	   while (it.hasNext()) {
		   methodDecAux = it.next();
		   if(methodDecAux.getName().equals(methodName)) {
			   return methodDecAux;
		   }
	   }
	   
	   // caso nao encontre o metodo na classe atual procura em sua superclasse (caso exista)
	   if(this.getSuperclass() != null && searchInSuper) {
		   return this.getSuperclass().searchMethod(methodName, true, searchInSuper);
	   }
	   
	   // caso nao encontre, nao ha metodos com esse nome disponiveis
	   return null;
   }
   
   /* responsavel por retornar o indice de determinado metodo publico de uma classe 
    * informada como parametro 
    */
   
   public int getMethodIndex(String methodName, String className) {
	   
	   if(this.vt != null) {
		   for(int i=0; i < this.vt.size(); i++) {
			   if(this.vt.get(i).getName().equals(methodName)) {
				   return i;
			   }
		   }
	   }
	   
	   return 0;
   }
   

   public boolean extend(String name) {
	   
	   // verifica se a classe atual herda a classe passada como parametro
	   if(this.superclass != null) {
		   if(this.superclass.getName().equals(name)) {
			   return true;
		   } else {
			   // caso haja classe superior buscara na mesma
			   return this.superclass.extend(name);
		   }
	   }
	   
	   return false;
   }
   
   public  ArrayList<MethodDec> getUnRedefinedMethodList(KraClass kClass) {
	   ArrayList<MethodDec> kClassUnRedefinedMethodList = new ArrayList<MethodDec>();
	   
	   if (kClass.getSuperclass() == null) {
		   //se nao herda de ninguem, sao todos os seus metodos publicos
		   for (MethodDec md: kClass.getPublicMethodList().getMethodDecList()) {
			   md.setKraClass(kClass);
			   kClassUnRedefinedMethodList.add(md);
		   }
		   return kClassUnRedefinedMethodList;
	   }
	   //se a classe herda de alguem
	   
	   ArrayList<MethodDec> superUnRedefined = getUnRedefinedMethodList(kClass.getSuperclass());
	   
	   for (MethodDec md: superUnRedefined) {
		   //se o metodo da superClasse nao existir na classe atual quer dizer que a classe nao esta redefinindo
		   if (kClass.searchMethod(md.getName(), true, false) == null) {
			   //logo, adicionar na tabela da classe atual
			   kClassUnRedefinedMethodList.add(md);
		   }
	   }
	   
	   //aqui kClassUnRedefinedMethodList tem os metodos da superclasse que nao foram redefinidos
	   //entao adicionamos os metodos publicos da classe corrente
	   for (MethodDec md: kClass.getPublicMethodList().getMethodDecList()) {
		   md.setKraClass(kClass);
		   kClassUnRedefinedMethodList.add(md);
	   }
	   
	   
	   return kClassUnRedefinedMethodList;
   }
   
   public void genKra(PW pw) {
	   pw.print("class " + getName());
	   if(superclass != null) {
		   pw.print(" extends " + superclass.getName());
	   }
	   pw.println(" {");
	   pw.println("");
	   pw.add();
	   memberList.genKra(pw);
	   pw.println("");
	   pw.sub();
	   pw.println("}");
	   pw.println("");
	   pw.println("");
   }
   
   public void genC(PW pw) {
	   vt = getUnRedefinedMethodList(this);
	   // gera a estrutura com um vetor de metodos da classe e variaveis de instancia
	   
	   pw.println("typedef");
	   pw.add();
	   pw.printlnIdent("struct _St_" + getName() + " {");
	   pw.add();
	   pw.printlnIdent("Func *vt;");
	   
	   // chamada para geracao de codigo das variaveis de instancia
	   
	   // caso haja uma superclasse, as variaveis de instancia serao as mesmas desta superclasse
	   if (superclass != null) {
		   superclass.instanceVariableList.genC(pw, superclass.getName());
	   } 
	   if (instanceVariableList.getSize() != 0) {
		   instanceVariableList.genC(pw, this.getName()); 
	   }
	   
	   pw.sub();
	   pw.printlnIdent("} " + getCname() + ";");
	   pw.sub();
	   
	   // criacao de um objeto desta classe 
	   pw.println("");
	   pw.println(getCname() + " *new_" + getName() + "(void);");
	   pw.println("");
	   
	   // gera codigos para os metodos desta classe
	   memberList.genC(pw, this);
	   
	   // gera o vetor de metodos publicos desta classe
	   pw.println("Func VTclass_" + this.getName() + "[] = {");
	   pw.add();
	   
	   // gera o codigo relativo a todos os metodos publicos declarados para esta classe
	   
	   if (superclass != null) {
		   for (int i=0; i < vt.size(); i++) {
			   pw.printIdent("(void(*)()) _" + vt.get(i).getKraClass().getName() + "_" + vt.get(i).getName());
			   if (i+1 != vt.size()) {
				   pw.println(",");
			   } else {
				   pw.println("");
			   }
		   }
	   }
	   else {
		   for(int i=0; i < this.publicMethodList.getSize(); i++) {
			   MethodDec currentMethod = this.publicMethodList.getElement(i);
			   pw.printIdent("(void(*)()) _" + this.getName() + "_" + currentMethod.getName());
			   if(i+1 != this.publicMethodList.getSize()) {
				   pw.println(",");
			   } else {
				   pw.println("");
			   }
		   }
	   }
	   
	   pw.sub();
	   pw.println("};");
	   pw.println("");
	   
	   // gera o codigo para alocacao de memoria para um "objeto" da classe
	   pw.println(getCname() + " *new_" + getName() + "() {");
	   pw.add();
	   pw.printlnIdent(getCname() + " *t;");
	   pw.println("");
	   pw.printlnIdent("if ((t = malloc(sizeof(" + getCname() + "))) != NULL)");
	   pw.add();
	   pw.printlnIdent("t->vt = VTclass_" + getName() + ";");
	   pw.sub();
	   pw.printlnIdent("return t;");
	   pw.sub();
	   pw.printlnIdent("}");
	   pw.println("");
	   
	   
   }
   
}
