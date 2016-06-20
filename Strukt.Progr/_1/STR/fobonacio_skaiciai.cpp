// 0, 1, 1, 2, 3, 5, 8, 13, 21, ...

//Rekursine Fibonacio skaiciu apskaiciavimo funkcija. Apskaiciuoja n-taji+1 
//Fibonacio skaiciu
#include<iostream.h>
unsigned long fibonacci(unsigned long);
main()
{
 unsigned long result, number;
 cout<<"Iveskite sveikaji skaiciu:";
 cin>>number;
 result=fibonacci(number);
 cout<<"Fibonacio skaicius ("<< number << ") = "<<result<< endl;
 
 system("PAUSE"); 
 return 0;
 
}
unsigned long fibonacci(unsigned long n)  //Rekursinis Fibonacio funkcijos aprasymas
{
	if (n==0||n==1)
	    return n;
	else
	    return fibonacci(n-1)+fibonacci(n-2);
}
