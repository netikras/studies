//prog7
//7.	Duoti 6 skirtingi skaiciai. Rasti didziausia is ju. (Aprasyti funkcija, surandancia didziausia reiksme is dveju skaiciu.)
#include <iostream>
#include <cstdlib>

using namespace std;

int palyg(int x, int y);

main(){

	cout<<"7.	Duoti 6 skirtingi skaiciai. Rasti didziausia is ju. (Aprasyti funkcija, surandancia didziausia reiksme is dveju skaiciu.)\n\n";

	int arr[6];

	int x, y, max=0, k;

	for(int i=0; i<6; i++){
		cout<<"Iveskite skaiciu ("<< i+1 <<"/6):   >";
		cin>>arr[i];
	}

	//cout<<"suvesta";

	for( int i=0; i<6; i++ ){

		k=i;
		//cout<<i<<" ";
		if( palyg(arr[i], arr[i++]) > max ) max=palyg(arr[k], arr[k++]);
		//max=palyg(arr[i], arr[i++]);



	}


	cout<<"Didziausias yra: " <<max <<"\n";

	system("PAUSE");
	return 0;

}


int palyg(int x, int y){
	//cout<<"\nfunk";
	if( x>y ) return x; else return y;

}
