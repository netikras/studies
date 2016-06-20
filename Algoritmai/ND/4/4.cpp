#include <iostream>
/*
    4.	Panaudojant funkcijas parašyti programą,
    kuri grąžina nuorodą į vienmačio masyvo max elementą,
    kuriame yra vien tik neigiami skaičiai.
    // Suprantu, kad MASYVE yra tik neigiami skaičiai, o ne ELEMENTE
*/
using namespace std;

int arr[]={
 -6,  -4, -96,
-85, -13,  -5
};


void* getMaxAddr(int myArr[], int len){
    int MX=myArr[0];
    void* addr;
    for(int i=0; i<len; i++){
        cout << &myArr[i] << " :: " << myArr[i]<<endl;
        if(MX < myArr[i]){
            MX=myArr[i];
            addr=&myArr[i];
        }
    }
    return addr;
}

int main(){
    cout << "Didžiausia reikšmė yra adrese: " << getMaxAddr(arr, sizeof(arr)/sizeof(arr[0]))<<endl;


	return 0;
}
