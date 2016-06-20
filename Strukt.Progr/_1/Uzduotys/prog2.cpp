//prog2
//2.	Parasyti programa, kuri priklausomai nuo menesio eiles numerio isveda i ekrana jo pavadinima.

#include <iostream>
#include <cstdlib>
#include <string>

using namespace std;

main(){
	cout<<"2.	Parasyti programa, kuri priklausomai nuo menesio eiles numerio isveda i ekrana jo pavadinima.\n\n";
	int menuo;

	string eile[12] = {"","Sausis", "Vasaris", "Kovas", "Balandis", "Geguze", "Birzelis", "Liepa", "Rugpjutis", "Rugsejis" "Spalis", "Lapkritis", "Gruodis"};

	cout<<"Iveskite menesio numeri (1-12):\n>";
	cin>>menuo;

	if ( menuo < 1 || menuo > 12 ){
		cout << "        Tokio menesio nera. Pasirinkite skaiciu nuo 1 iki 12!";
		system("PAUSE");

		return 1;
	}

	cout << eile[menuo] << "\n";

	system("PAUSE");
	return 0;
}
