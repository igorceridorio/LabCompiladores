// 408611 - Igor Felipe Ferreira Ceridorio
// 552380 - Rafael Zanetti

package ast;

import java.util.Iterator;

public class KraClass extends Type {
	 
   private KraClass superclass;
   private MemberList memberList;
   private InstanceVariableList instanceVariableList;
   private MethodDecList publicMethodList;
   private MethodDecList privateMethodList;
   
   public KraClass(String name) {
	   super(name);
	   this.superclass = null;
	   this.memberList = null;
	   this.instanceVariableList = new InstanceVariableList();
	   this.publicMethodList = new MethodDecList();
	   this.privateMethodList = new MethodDecList();
   }
   
   public String getCname() {
      return getName();
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
		   return this.getSuperclass().searchMethod(methodName, false, searchInSuper);
	   }
	   
	   // caso nao encontre, nao ha metodos com esse nome disponiveis
	   return null;
   }
   

   public boolean extend(String name) {
	   
	   // verifica se a classe passada como parametro herda a classe atual
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
}
