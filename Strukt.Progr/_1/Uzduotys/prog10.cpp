//prog10
//10. Duotas masyvas. Nelyginius elementus sumazinti m kartu, o elementus su nelyginiu numeriu padidinti n kartu.
#include <iostream>
#include <cstdlib>

using namespace std;

main(){
    cout<<"10. Duotas masyvas. Nelyginius elementus sumazinti m kartu, o elementus su nelyginiu numeriu padidinti n kartu.\n\n";

    int m, n, length, k=0, x;
    cout<<"m=";cin>>m; cout<<"n=";cin>>n;
    cout<<"Iveskite masyvo ilgi: >";
    cin>>length;

    int arr[length];

    while( k < length){
        cout<<"Iveskite masyvo elementa ("<<k<<"/"<<length<<") >";
        cin>>x;
        (x%2==0?x*=n:x/=m);

        arr[k]=x;
        k++;
    }//whle

for(int i=0; i<length; i++){
    cout<<"arr["<<i<<"]="<<arr[i] << "\n";
}

}
