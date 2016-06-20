//Rekursine faktorialo funkcija
#include<iostream.h>
#include<iomanip.h>
unsigned long factorial(unsigned long);
main()
{
	for (int i=0; i<=10; i++)
	cout<<setw(2)<<i<<"! = "<<factorial(i)<<endl;
	
	system("PAUSE");
    return 0;
	
	
}
unsigned long factorial (unsigned long number)	//Rekursinis faktorialo funkcijos aprašymas
{
	if (number<=1)
	return 1;
	else
		return number * factorial(number-1);
}
