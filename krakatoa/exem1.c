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
   int _primo;
   char *_msg;
   puts(Ol�, este � o meu primeiro programa);
   puts(Digite um n�mero: );
   puts(msg);
}