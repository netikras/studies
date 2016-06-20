/*
Parasyti programa, kuri atliks standartini darbo su steko rinkini, veikiancia dinamines atminties paskirstymo pagrindu:
tikrina, ar stekas netuscias
patikrina steko masyvo uzpildyma
is karto keleta elementu papildymas (ivedamas elementu kiekis)
elemento pasalinimas is steko virsunes
steko esamos busenos isvedimas i ekrana
*/

#include <iostream>
#include <cstdlib>
using namespace std;

struct STACK {
int info; 
STACK *next;
};

int Empty(STACK *pstack)
{
if (pstack==NULL) 
return 0; 
else 
return 1;
}

void Add(STACK **pstack)
{
     STACK *tmp = new STACK; 
     tmp->info=rand()%100; 
     tmp->next=*pstack; 
     *pstack=tmp;
}

void Del(STACK **pstack)
{
     STACK *tmp=*pstack; 
     *pstack=(*pstack)->next; 
     delete tmp;
}

void Show(STACK *pstack)
{
     STACK *tmp=pstack; 
     while(tmp!=NULL) 
{ 
     cout << tmp->info << " "; 
     tmp=tmp->next; 
}
}

void ClearAll(STACK **pstack)
{
     STACK *tmp; 
     while(*pstack!=NULL) 
     { 
       tmp=*pstack; 
       *pstack=(*pstack)->next; 
       delete tmp; 
       }
}

int main()
{

STACK *stack=NULL; 
int num; 
char ats; 
do 
{ 
cout << "1. Elementu papildymas i steko virsune" << endl 
<< "2. Elementu pasalinimas is steko virsunes" << endl 
<< "3. Elementu isvedimas" << endl 
<< "0. Baigti" << endl; 
cout << " = "; 
cin >> ats; 
switch(ats) 
{ 
case '1': 
          cout << endl << "Kiek elementu papildyti" << endl; 
          cin >> num; 
          for ( int i=0;i<num;i++) 
          Add(&stack); 
          cout << endl << "Elementai papildyti" << endl; 
break;
case '2':
          if (Empty(stack)==0) 
          cout << endl << "Stekas tuscias" << endl; 
          else 
               { 
               Del(&stack); 
               cout << endl << "Elementas pasalintas is steko virsunes" << endl; 
               } 
break;
case '3':
         if (Empty(stack)==0) 
            cout << endl << "Stekas tuscias" << endl; 
            else 
                 { 
                 cout << endl << "Steko elementai:" << endl; 
                 Show(stack); 
                 cout << endl; 
                 } 
break;
case '0':
         ClearAll(&stack); 
break; 
default: 
         cout << endl << "Klaida" << endl; 
break;
}

}while(ats!='0');
cin.get();

}
