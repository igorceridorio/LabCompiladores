/* deve-se incluir alguns headers porque algumas funções da biblioteca padrão de C são utilizadas na tradução. */

#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

/* define o tipo boolean */
typedef int boolean;
#define true  1
#define false 0

/* define um tipo Func que é um ponteiro para função */
typedef
  void (*Func)();

/* Para cada classe, deve ser gerada uma estrutura como a abaixo. Se Program tivesse variáveis de instância, estas seriam declaradas nesta estrutura. _class_Program representa em C uma classe em Krakatoa. */

typedef
  struct _St_Program {
       /* ponteiro para um vetor de métodos da classe */
    Func *vt;
    } _class_Program;

   /* Este é um protótipo de método que cria um objeto da classe Program. Toda classe 
      A não abstrata possui um método new_A que cria e retorna um objeto da classe A.
      O método new_Program é declarado antes do método main, abaixo.
   */
_class_Program *new_Program(void);


   /* 
      Este é o método run da classe Program. Note que o método é traduzido para 
      uma função de C cujo nome é uma concatenação do nome da classe com o nome
      do método. Sempre há um primeiro parâmetro chamado this cujo tipo é a 
      estrutura que representa a classe, neste caso, _class_Program.
   */
void _Program_run( _class_Program *this )
{
      /* os nomes de variáveis locais são precedidos por _ */
   int _i;
   int _b;
   boolean _primo;
      /* Strings são mapeadas para char * em C */
   char *_msg;

      /* write com Strings são mapeados para puts em C */
   puts( "Ola, este e o meu primeiro programa" );
   puts( "Digite um numero: ");
      /* read(b), com b inteiro é mapeado para o código entre { e } abaixo */
   { 
     char __s[512];
     gets(__s);
     sscanf(__s, "%d", &_b);
   }
     /* o restante do código é praticamente igual em Krakatoa e C, a menos
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
      funções). O nome deve ser VTclass_NomeDaClasse, como VTclass_Program. Este
      vetor é inicializado (iniciado) com as funções em C, como _Program_run, que
      representam os métodos **públicos** da classe. Note que o tipo de _Program_run
      é void (*)(_class_program *) e portanto é necessário um cast para convertê-lo
      para o tipo de Func, void (*)()
   */
Func VTclass_Program[] = {
  ( void (*)() ) _Program_run
  };

   /*
      Para toda classe não abstrata se declara uma função new_NomeDaClasse que aloca
      memória para um objeto da classe, que é um "objeto" da estrutura
       _class_NomeDaClasse. Note que este método é igual para todas as classes, a 
       menos do nome da classe. 
   */

_class_Program *new_Program()
{
  _class_Program *t;

  if ( (t = malloc(sizeof(_class_Program))) != NULL )
       /* o texto explica porque vt é inicializado */ 
    t->vt = VTclass_Program;
  return t;
}

   /* genC de Program da ASA deve gerar a função main exatamente como abaixo. */
int main() {
  _class_Program *program;

    /* crie objeto da classe Program e envie a mensagem run para ele */
  program = new_Program();
  ( ( void (*)(_class_Program *) ) program->vt[0] )(program);
  return 0;
}


