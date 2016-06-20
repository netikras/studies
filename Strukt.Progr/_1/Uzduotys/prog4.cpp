//prog4
//4.	Duotas naturinis skaicius. Rasti du jo maziausius skaitmenis.

#include <iostream>
#include <cstdlib>

using namespace std;

main(){
	cout<<"4.	Duotas naturinis skaicius. Rasti du jo maziausius skaitmenis.\n\n";
	int x, min1=9, min2=9;

	cout<<"Iveskite naturini skaiciu\n> ";
	cin>>x;
	int number = x;



	int digits = 0;
	while (number != 0) {
		number /= 10;
		digits++;

		};

	//cout<<"skaitmenys: " << digits;

	int skaitmenys[digits];


	for ( int i=digits-1; i>=0; i-- ){

		skaitmenys[i] = x%10;
		x /= 10;
		//cout<<"skaiciai: "<<skaitmenys[i]<<"\n";
	}

	//cout<<"skaitmenys:  " << digits << "antras  " << skaitmenys[1];

	for( int i=0; i<digits; i++ ){
		//cout<<skaitmenys[i]<<"\n";
		if( skaitmenys[i] < min1){
			min1 = skaitmenys[i];
		}

	}
	cout<<endl;
	for( int i=0; i<digits; i++ ){
		//cout<<skaitmenys[i]<<"\n";
		if( skaitmenys[i] < min2 && skaitmenys[i] != min1){
			min2 = skaitmenys[i];
		}

	}


	cout << "min1=" << min1<<"\nmin2="<<min2<<"\n";




	system("PAUSE");
	return 0;

}
