/* deve-se incluir alguns headers porque algumas fun��es da biblioteca padr�o de C s�o utilizadas na tradu��o. */

#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

/* define o tipo boolean */
typedef int boolean;
#define true  1
#define false 0

/* define um tipo Func que � um ponteiro para fun��o */
typedef
  void (*Func)();

/* Para cada classe, deve ser gerada uma estrutura como a abaixo. Se Program tivesse vari�veis de inst�ncia, estas seriam declaradas nesta estrutura. _class_Program representa em C uma classe em Krakatoa. */

typedef
  struct _St_Program {
       /* ponteiro para um vetor de m�todos da classe */
    Func *vt;
    } _class_Program;

   /* Este � um prot�tipo de m�todo que cria um objeto da classe Program. Toda classe 
      A n�o abstrata possui um m�todo new_A que cria e retorna um objeto da classe A.
      O m�todo new_Program � declarado antes do m�todo main, abaixo.
   */
_class_Program *new_Program(void);


   /* 
      Este � o m�todo run da classe Program. Note que o m�todo � traduzido para 
      uma fun��o de C cujo nome � uma concatena��o do nome da classe com o nome
      do m�todo. Sempre h� um primeiro par�metro chamado this cujo tipo � a 
      estrutura que representa a classe, neste caso, _class_Program.
   */
void _Program_run( _class_Program *this )
{
      /* os nomes de vari�veis locais s�o precedidos por _ */
   int _i;
   int _b;
   boolean _primo;
      /* Strings s�o mapeadas para char * em C */
   char *_msg;

      /* write com Strings s�o mapeados para puts em C */
   puts( "Ola, este e o meu primeiro programa" );
   puts( "Digite um numero: ");
      /* read(b), com b inteiro � mapeado para o c�digo entre { e } abaixo */
   { 
     char __s[512];
     gets(__s);
     sscanf(__s, "%d", &_b);
   }
     /* o restante do c�digo � praticamente igual em Krakatoa e C, a menos
        de nomes de identificadores */
   _primo = true;
   _i = 2;

   while ( _i < _b )
      if ( _b%_i == 0 ) {
         _primo = false;
         break;
      }
      else
         _i++;
   if ( _primo != false )
      _msg = "Este numero e primo";
   else
      _msg = "Este numero nao e primo";
   puts(_msg);

}

   /*
      Para toda classe deve ser declarado um vetor de Func (vetor de ponteiro para
      fun��es). O nome deve ser VTclass_NomeDaClasse, como VTclass_Program. Este
      vetor � inicializado (iniciado) com as fun��es em C, como _Program_run, que
      representam os m�todos **p�blicos** da classe. Note que o tipo de _Program_run
      � void (*)(_class_program *) e portanto � necess�rio um cast para convert�-lo
      para o tipo de Func, void (*)()
   */
Func VTclass_Program[] = {
  ( void (*)() ) _Program_run
  };

   /*
      Para toda classe n�o abstrata se declara uma fun��o new_NomeDaClasse que aloca
      mem�ria para um objeto da classe, que � um "objeto" da estrutura
       _class_NomeDaClasse. Note que este m�todo � igual para todas as classes, a 
       menos do nome da classe. 
   */

_class_Program *new_Program()
{
  _class_Program *t;

  if ( (t = malloc(sizeof(_class_Program))) != NULL )
       /* o texto explica porque vt � inicializado */ 
    t->vt = VTclass_Program;
  return t;
}

   /* genC de Program da ASA deve gerar a fun��o main exatamente como abaixo. */
int main() {
  _class_Program *program;

    /* crie objeto da classe Program e envie a mensagem run para ele */
  program = new_Program();
  ( ( void (*)(_class_Program *) ) program->vt[0] )(program);
  return 0;
}


