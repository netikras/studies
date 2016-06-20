//prog5
//5.	Naudojant ciklo operatoriu atspausdinti tokia lentele
#include <iostream>
#include <cstdlib>

using namespace std;


main(){
	cout<<"5.	Naudojant ciklo operatoriu atspausdinti skaiciu lentele\n\n";

	int a=24, b=5, c=5;
	int arr[5];

	for( int i=1; i<6; i++ ){
		b = 6-i;


		while( b>0 ){
			arr[b-1] = a;
		//	cout<<arr[b-1]<<" ";
			a --;

			b--;
		}




		a=24-2*i;

		for(int j=0; j<c; j++){

			cout << arr[j] << " ";
		}

		c--;

		cout<<"\n";


	}



	system("PAUSE");
	return 0;

}
