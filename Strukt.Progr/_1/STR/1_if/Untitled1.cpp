#include <iostream.h>

main() 
{
     float x, y;
     
     cout<<"Iveskite skaiciu x"<< endl;
     cin>>x;
     
     if (x > 1) 
           y=x*x*x+x*x;
     else if (x >=0 && x <= 1 )
             y=x*x+2*x;
             else
                 y=2*x*x*x;
          
     cout<<"Y lygus: "<<y<<endl;
     
     system("PAUSE"); 
     return 0;

}
