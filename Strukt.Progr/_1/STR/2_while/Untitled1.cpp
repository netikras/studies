#include <iostream.h>

main() 
{
     int counter, grade, total, average;
     
     total=0;
     counter=1;
     
     while (counter <= 10) {
           cout << "Iveskite pazymi"<<endl;
           cin >> grade; 
           total = total + grade; 
           counter = counter + 1;
     }
     
     average = total / 10;     //
     cout<<"Vidurkis: "<<average<<endl;
     
     system("PAUSE"); 
     return 0;

}
