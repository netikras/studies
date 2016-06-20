//prog1
//1.	Duotas keturzenklis skaicius. Nustatyti, ar skaiciaus skaitmenu sandauga dalinasi is skaiciaus a?

#include <iostream>
#include <cstdlib>

using namespace std;

main(){
    cout<<"1.	Duotas keturzenklis skaicius. Nustatyti, ar skaiciaus skaitmenu sandauga dalinasi is skaiciaus a\n\n";
	int ket_sk, pora1, pora2, temp1, temp2, a;
	cout<<"Iveskite keturzenkli skaiciu\n> ";
	cin>>ket_sk;
	cout<<"Iveskite skaiciu a\n> ";
	cin>>a;
	temp1=ket_sk / 1000;
	temp2=ket_sk / 100 - temp1 * 10;
	pora1= temp1 * temp2;
        //zumažiname kintamąjį jo tūkstančiais ir šimtais
	ket_sk = ket_sk - temp1 * 1000 - temp2 * 100;

	//////////////////////////
	temp1=ket_sk / 10;
	temp2=ket_sk - temp1 * 10;
	pora2=temp1 * temp2;
        //kintamasis nebereikalingas, atmintis jam jau rezervuota, tipas toks pat -- galime jam priskirt kita reiksme
	ket_sk = pora1 * pora2;




	if( ket_sk % a ) {
	    //cout << ket_sk;
		cout << "Nesidalina\n";
	} else {
		 cout <<"Dalinasi\n";
	}

	system("PAUSE");
    //string PAUSE;
    //cout<<"Press enter to exit";
    //getline(cin, PAUSE);

	return 0;


}
