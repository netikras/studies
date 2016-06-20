//prog6
//6.	Duoti naturiniai skaiciai m ir n. Apskaiciuoti  reiskini: [1^n + 2^n + ... + m^n]
#include <iostream>
#include <cstdlib>
#include <math.h>


using namespace std;

main(){

	cout<<"6.	Duoti naturiniai skaiciai m ir n. Apskaiciuoti  reiskini:\n [1^n + 2^n + ... + m^n]\n\n";

	cout<<"Iveskite m\n> ";
	int m;
	cin >>m;
	cout<<"Iveskite n\n> ";
	int n;
	cin>>n;

	int rez=0;

	for( int i=1; i<=m; i++ ){

		rez+=pow(i, n);

	}

	cout<<"rezultatas= " << rez << "\n";

	system("PAUSE");
	return 0;

}
