//prog9
//9. Duotas masyvas. Nustatykite, ar masyvo elementu kvadratu suma yra penkiazenklis skaicius?
#include <iostream>
#include <cstdlib>
#include <math.h>

using namespace std;


main(){
    cout<<"9. Duotas masyvas. Nustatykite, ar masyvo elementu kvadratu suma yra penkiazenklis skaicius?\n\n";

    int sum=0, length, k=0;

    cout<<"Iveskite masyvo ilgi: >";
    cin>>length;

    int arr[length];

    while( k < length){
        cout<<"Iveskite masyvo elementa ("<<k<<"/"<<length<<") >";
        cin>>arr[k];
        k++;
    }//whle



    for(int i=0; i<length; i++){
        sum+=pow(arr[i], 2);
    }

    cout<<"Masyvo elementų kvadratų suma yra: "<<sum<<"\n";

    return 0;

}

