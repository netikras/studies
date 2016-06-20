#include <iostream>
#include <cstdlib>

using namespace std;

//steko realizavimas
const int max_len = 1000;
enum {EMPTY=-1, FULL=max_len-1};

struct stack {
       char s[max_len];
       int top;
             };
stack s;

void reset(stack* stk);
void push (char c, stack* stk);
char pop (stack* stk);
char top (stack* stk);
bool empty(const stack* stk);
bool full(const stack* stk);

int main()
{
      
      char str[40]={"Siandien grazi diena!"};
      int i=0;
      
      cout << str <<endl; //Eiluciu spausdinimas
      reset(&s);
      while(str[i])       //Ideda i steka
         if (!full(&s))
           push(str[i++], &s);
           while (!empty(&s)) //Atvirkstines eilutes spausdinimas
           cout << pop(&s);
           cout<<endl;
           system("pause");
       return 0;
      }

//Funkcijos
void reset(stack* stk)
{
     stk->top=EMPTY;
     }
     
void push (char c, stack* stk)
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
