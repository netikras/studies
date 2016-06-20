/*Ði programa rodo, kaip sukuriamas dvejetainis medis C programa. 
Ji naudoja dinamini atminties paskirstyma, rodyklës ir rekursija.
Dvejetainis medis yra labai naudinga duomenø struktûrà, nes ji 
leidþia efektyvias áterpimo, paieðkos ir iðbraukimo operacijas surûðiuotame sàraðe. 
Tokios duomenø struktûros apdorojamos rekursijos pagalba.*/

#include<stdlib.h>
#include<stdio.h>

struct tree_el {
   int val;
   struct tree_el * right, * left;
};

typedef struct tree_el node;

void insert(node ** tree, node * item) {
   if(!(*tree)) {
      *tree = item;
      return;
   }
   if(item->val<(*tree)->val)
      insert(&(*tree)->left, item);
   else if(item->val>(*tree)->val)
      insert(&(*tree)->right, item);
}

void printout(node * tree) {
   if(tree->left) printout(tree->left);
   printf("%d\n",tree->val);
   if(tree->right) printout(tree->right);
}

int main() {
   node * curr, * root;
   int i;

   root = NULL;

   for(i=1;i<=10;i++) {
      curr = (node *)malloc(sizeof(node));
      curr->left = curr->right = NULL;
      curr->val = rand();
      insert(&root, curr);
   }

   printout(root);
   system("pause");
}
