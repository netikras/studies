#include <iostream>
#include <stdio.h>
/*
5. Parašykite programą, kuri patikrins, ar skaičius N yra dviejų pirminių skaičių suma.
*/

using namespace std;

void pirminiaiSk();

int *arrayBig;
bool tinka=false;
int N=0,arrLen=0;
int sum=0;
bool _not=true;
int main(){
    cout << "Įveskite tikrinamą skaičių:\n>";
    cin>>N;
    arrayBig = new int[N];

    pirminiaiSk();

    for(int i=0; i<arrLen; i++){
        for(int j=i; j<arrLen; j++){
            sum=arrayBig[i]+arrayBig[j];
            if(sum == N){
                if(_not) cout <<"TAIP, skaičius "<<N<<" yra dviejų pirminių skaičių suma!\n";
                cout <<"\t"<<arrayBig[i]<<" + "<<arrayBig[j]<<" = "<<sum<<"\n";
                _not=false;
            }
        }
    }
    if(_not) printf("Ne, skaičius %d nėra dviejų pirminių skaičių suma\n", N);

    delete [] arrayBig;
    return 0;
}



void pirminiaiSk(){

    for(int i=2; i<=N; i++){
        tinka=true;
        for(int j=2; j<i; j++){
            if(i % j == 0) { tinka=false;}
        }
        if(tinka) {
            //cout <<i<<"\n";
            arrayBig[arrLen] = i;
            arrLen++;
        }

    }
}
