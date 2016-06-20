//prog3
//3.	Apskaiciuoti suma reiskinio [1 - (2x)/3 + (3(x^2))/4 - (4(x^3))/5 + ... + (11(x^10))/12 ], kai x=2.
#include <iostream>
#include <cstdlib>
#include <math.h>

using namespace std;

main(){
	cout<<"3.	Apskaiciuoti suma reiskinio [1 - (2x)/3 + (3(x^2))/4 - (4(x^3))/5 + ... + (11(x^10))/12 ], kai x=2.\n\n";
	int a=2, n=1;
	float suma=1, s1;

	for(int i=1; i<=10; i++ ){
        n*=-1;
        s1=suma;
		suma += (i+1)*pow(a, i)*n/(i+2);
        //cout<<"S["<<i<<"]="<<s1<<(n<0?"-":"+")<<"("<<i+1<<"(x^"<<i<<"))/"<<i+2<<"="<<suma<<"\n";
	}
    cout<<"\nSuma: " << suma << "\n\n";

	system("PAUSE");
	return 0;
}

