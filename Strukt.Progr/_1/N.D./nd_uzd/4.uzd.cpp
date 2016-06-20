#include <iostream>
#include <cstdlib>
/*
4. Parašyti funkciją ir ją pritaikyti programoje, kuri užpildo vienmatį sveikųjų skaičių masyvą atsitiktiniais skaičiais intervale nuo 0 iki N.
*/

void randGen();


int N;
const int arrLength=10;
int arrayRand[arrLength];



using namespace std;

int main(){


    randGen();

    cout<<"10 atstiktinių skaičių intervale [0;"<<N<<"]:\n\t";
    for(int i=1; i<=arrLength; i++){
        cout <<arrayRand[i] <<"\n\t";
    }

    return 0;
}


void randGen(){

    cout << "Iveskite skaiciu N - masyvas bus uzpildytas atsitiktiniais skaiciais nuo 0 iki N\n\tN=";
    cin >> N;
    for(int i=1; i<=arrLength; i++){
        arrayRand[i]=(rand() % (1+N));
        //cout << arrayRand[i]<<"\n";
    }
}
