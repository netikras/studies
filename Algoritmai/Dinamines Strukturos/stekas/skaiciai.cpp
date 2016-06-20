//Duotas intervalas sveiku skaiciu [a;b]. Atspausdinti teigiamus sio intervalo skaicius atbuline tvarka.

#include <iostream.h>

using namespace std;

//steko realizavimas
const int max_len = 1000;
enum {EMPTY=-1, FULL=max_len-1};

struct stack {
       int s[max_len];
       int top;
             };
stack s;

void reset(stack* stk);
void push (int c, stack* stk);
char pop (stack* stk);
char top (stack* stk);
bool empty(const stack* stk);
bool full(const stack* stk);

main()
{
 int a, b, skaiciai[100];
 cout<<"Iveskite intervalo pradzia"<<endl;
 cin>>a;
 cout<<"Iveskite intervalo pabaiga"<<endl;
 cin>>b;
     
    for (int i=1; a<b; i++)
    {
    skaiciai[i]=a;
    a++;
    }
      
     int i=0;
      
      //cout << str <<endl; //Eiluciu spausdinimas
      reset(&s);
      while(skaiciai[i])       //Ideda i steka
         if (!full(&s))
         {
           push(skaiciai[i], &s);
           i++;
           }
           while (!empty(&s)) //Atvirkstines eilutes spausdinimas
           cout << pop(&s);
           cout<<endl;
           system("pause");
      }

//Funkcijos
void reset(stack* stk)
{
     stk->top=EMPTY;
     }
     
void push (int c, stack* stk)
{
   stk ->s[++stk -> top] =c;  
     }
     
char pop (stack* stk)
{
     return (stk -> s[stk ->top--]);
     }
     
char top (stack* stk)
{
     return (stk -> s[stk ->top]);
     }
     
bool empty(const stack* stk)
{
        return (bool)(stk -> top ==EMPTY);
        }
        
bool full(const stack* stk)
{
        return (bool)(stk -> top ==FULL);
        }
