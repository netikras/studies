#include <iostream.h>
#include <iomanip.h>
#include <math.h>

main() 
{
     double amount, principal=1000.0, rate=.05;
     
     cout<<"Metai"<<setw(21)<<"Indelio suma"<<endl;
     
     for(int year=1; year<=10; year++)
             {
                     amount=principal*pow(1.0+rate, year);
cout<<setw(3)<<year<<setiosflags(ios::fixed | ios::showpoint)<<setw(21)<<setprecision(2)<<amount<<endl;
}     
     system("PAUSE"); 
     return 0;

}
