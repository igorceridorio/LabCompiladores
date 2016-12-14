#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

typedef int boolean;
#define true 1
#define false 0

typedef
   void (*Func) ();

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run(_class_Program *this) {
   int _i;
   int _b;
   boolean _primo;
   char *_msg;
   puts("Olá, este é o meu primeiro programa");
   puts("Digite um número: ");
   {
      char __s[512];
      gets(__s);
      sscanf(__s, "%d", &_b);
   }
   _primo = true;
   _i = 2;
   while (_i < _b) {
      if ((_b - (_i * (_b / _i))) == 0) {
         _primo = false;
         break;
      } else { 
         _i = _i + 1;
      }
   }
   if (_primo) {
      _msg = "Este numero e primo";
   } else { 
      _msg = "Este numero nao e primo";
   }
   puts(_msg);
}

Func VTclass_Program[] = {
   (void(*)()) _Program_run
};

_class_Program *new_Program() {
   _class_Program *t;

   if ((t = malloc(sizeof(_class_Program))) != NULL)
      t->vt = VTclass_Program;
   return t;
}

int main() {
   _class_Program *program;
   program = new_Program();
   ((void (*)(_class_Program *) ) program->vt[0])(program);
   return 0;
}
