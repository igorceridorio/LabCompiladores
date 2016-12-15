#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

typedef int boolean;
#define true 1
#define false 0

typedef
   void (*Func) ();

typedef
   struct _St_A {
      Func *vt;
   } _class_A;

_class_A *new_A(void);

void _A_m(_class_A *this) {
   printf("%d ", 7);
   if (1 > 0) {
      printf("%d ", 0);
   }
   if (1 >= 0) {
      printf("%d ", 1);
   }
   if (1 != 0) {
      printf("%d ", 2);
   }
   if (0 < 1) {
      printf("%d ", 3);
   }
   if (0 <= 1) {
      printf("%d ", 4);
   }
   if (0 == 0) {
      printf("%d ", 5);
   }
   if (0 >= 0) {
      printf("%d ", 6);
   }
   if (0 <= 0) {
      printf("%d ", 7);
   }
   if (1 == 0) {
      printf("%d ", 18);
   }
   if (0 > 1) {
      printf("%d ", 10);
   }
   if (0 >= 1) {
      printf("%d ", 11);
   }
   if (0 != 0) {
      printf("%d ", 12);
   }
   if (1 < 0) {
      printf("%d ", 13);
   }
   if (1 <= 0) {
      printf("%d ", 14);
   }
}

Func VTclass_A[] = {
   (void(*)()) _A_m
};

_class_A *new_A() {
   _class_A *t;

   if ((t = malloc(sizeof(_class_A))) != NULL)
      t->vt = VTclass_A;
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run(_class_Program *this) {
   _class_A *_a;
   puts("");
   puts("Ok-ger01");
   puts("The output should be :");
   puts("7 0 1 2 3 4 5 6 7");
   _a = new_A();
   ;
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
