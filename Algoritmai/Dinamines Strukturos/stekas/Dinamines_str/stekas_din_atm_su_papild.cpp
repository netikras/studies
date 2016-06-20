/*
Parasyti programa, kuri atliks standartini darbo su steko rinkini, veikiancia dinamines atminties paskirstymo pagrindu:
tikrina, ar stekas netuscias
patikrina steko masyvo uzpildyma
is karto keleta elementu papildymas (ivedamas elementu kiekis)
elemento pasalinimas is steko virsunes
steko esamos busenos isvedimas i ekrana

su papildomu steku pasalintiems elementams
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
     STACK *tmp= new STACK; 
     tmp->info=rand()%100; 
     tmp->next=*pstack; 
     *pstack=tmp;
}

void Move(STACK **pdopstack, STACK **pstack)
{
     STACK *tmp=*pdopstack; 
     *pdopstack=(*pdopstack)->next; 
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

    STACK *stack=NULL, *dopstack=NULL; 
    int num; 
    char ats, ats2; 
do 
{ 
cout << "1. Elementu papildymas i steko virsune" << endl 
<< "2. Elemento pasalinimas is steko virsunes" << endl 
<< "3. Pagrindinio steko elementu isvedimas" << endl 
<< "4. Papildomo steko elementu isvedimas" << endl 
<< "0. Baigti" << endl; 
cout << " = "; 
cin >> ats; 
switch(ats) 
{
case '1':
         cout << endl << "1. Prideti nauja elementa" << endl 
         << "2. Prideti is papildomo steko" << endl; 
         cout << " = "; 
         cin >> ats2; 
switch(ats2) 
{ 
case '1': 
          cout << endl << "Kiek elementu reikia papildyti = " << endl; 
          cin >> num; 
          for ( int i=0;i<num;i++) 
          Add(&stack); 
          cout << endl << "Elementai papildyti" << endl; 
break;
case '2':
         if (Empty(dopstack)==0) 
         cout << endl << "Papildomas stekas tuscias" << endl; 
         else 
         { 
         Move(&dopstack,&stack); 
         cout << endl << "Elementas papildytas is papildomo steko i pagrindini" << endl; 
         } 
break;
default:
        cout << endl << "Klaida" << endl; 
break;
}
break;

case '2':
         cout << endl << "1. Pasalinti elementa" << endl 
         << "2. Papildyti i pagalbini steka" << endl << " = "; 
         cin >> ats2; 
switch(ats2) 
{ 
case '1': 
          if (Empty(stack)==0) 
          cout << endl << "Stekas tuscias" << endl; 
          else 
          { 
          Del(&stack); 
          cout << endl << "Elementas pasalintas" << endl; 
          } 
break;
case '2':
         if (Empty(stack)==0) 
         cout << endl << "Stekas tuscias" << endl; 
         else 
         { 
         Move(&stack,&dopstack); 
         cout << endl << "Elementas papildytas i pagalbini steka" << endl; 
         } 
break;
default:
        cout << endl << "Klaida" << endl; 
break;
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
case '4':
         if (Empty(dopstack)==0) 
         cout << endl << "Papildomas stekas tuscias" << endl; 
         else 
         { 
         cout << endl << "Papildomo steko elementai:" << endl; 
         Show(dopstack); 
         cout << endl; 
         } 
break;
case '0':
         ClearAll(&stack); 
         ClearAll(&dopstack); 
break;
default:
         cout << endl << "Klaida" << endl; 
break;
}

}while(ats!='0');
cin.get();

}
