#include <cstdlib>
#include <stdio.h>
#include <time.h>
/*
7. Funkcijos, kuri generuoja atsitiktinius skaičius, pagalba aprašyti žaidimo kauliuką, kuris po metimo išveda skaičių iš intervalo 1...6.
*/

using namespace std;

int randGen();

int main(){

    printf("Iškrito:\t%i",randGen());
    return 0;
}


int randGen(){
    srand(time(0));
    return (rand() % 6)+1;
}
