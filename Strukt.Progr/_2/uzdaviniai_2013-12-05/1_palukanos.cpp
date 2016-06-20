#include <iostream>

/*
1. Kiek metų reikia laikyti pinigus taupomajame banke,
    kad, mokant x % palūkanų, pradinis indelis padvigubėtų.
*/

using namespace std;


int main(){
    double paluk_dyd=0.0;
    double x=500.0, y=x;
    int metai=0;
    cout << "Įveskite palūkanų dydį procentais:\n--> ";
    cin >> paluk_dyd;

    while(y<2*x){
        metai++;
        y+=y*paluk_dyd/100;
    }

    cout << "teks taupyti " << metai <<" metus\n";

return 0;
}
