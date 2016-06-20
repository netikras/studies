/*
Parasyti programa, kuri atliks standartini darbo su steko rinkini, veikiancia masyvo pagrindu:
tikrina, ar stekas netuscias
patikrina steko masyvo uzpildyma
elemento papildymas i steko virsune
elemento pasalinimas is steko virsunes
steko esamos busenos isvedimas i ekrana
*/

#include <iostream.h>
#include <cstdlib>
using namespace std;

const int N=4;

struct STACK {

int arr[N];
int count;

};

void Initial(STACK *ps)
{

     ps->count=0;

}

int Empty(STACK *ps)
{

    if (ps->count==0)
       return 1;
       else
       return 0;

}

int Full(STACK *ps)
{

    if (ps->count==N)
    return 1;
    else
    return 0;

}

void Add(STACK *ps)
{
     ps->arr[ps->count]=rand()%100; 
     ps->count++;
}

void Del(STACK *ps)
{
     ps->count--;
     
}

void Show(STACK *ps)
{
     for ( int i=0;i<ps->count;i++) 
     cout << ps->arr[i] << " ";
}

int main()
{
STACK s; 
Initial(&s); 
char ats; 
do 
{
cout << "1. Elemento papildymas i steko virsune" << endl 
<< "2. Elemento pasalinimas is steko virsunes" << endl
<< "3. Isvesti steko elementus" << endl
<< "4. Patikrinimas, ar stekas tuscias" << endl
<< "5. Patikrinimas, ar stekas pilnas" << endl
<< "0. Baigti" << endl
<< " = ";
cin >> ats;
switch(ats)
{
case '1':

         if (Full(&s)==1)
         cout << endl << "Stekas pilnas" << endl;
         else
         {
          Add(&s);
          cout << endl << "Elementas papildytas i steka" << endl;
          }
break;

case '2':

         if (Empty(&s)==1)
         cout << endl << "Stekas tuscias" << endl;
         else 
         {
         Del(&s);
         cout << endl << "Elementas pasalintas is steko" << endl;
         }
break;

case '3':

         if (Empty(&s)==1)
         cout << endl << "Stekas tuscias" << endl;
         else
         {
         cout << endl << "Steko elementai" << endl;
         Show(&s);
         cout << endl;
         }
break;

case '4':

         if (Empty(&s)==1)
         cout << endl << "Stekas tuscias" << endl;
         else 
         cout << endl << "Steke " << s.count << " elementas tai tu" << endl;
break;

case '5':

         if (Full(&s)==1)
         cout << endl << "Stekas pilnas" << endl;
         else 
         cout << endl << "Imanoma papildyti " << N-s.count << " elementa tus tu" << endl;
break;

case '0':

break;

default:

        cout << endl << "Klaida" << endl;
break;

}

}while(ats!='0');
cin.get();

}
