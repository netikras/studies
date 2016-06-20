#include <iostream>
/*
3. Duotas sveikas skaičius n>2. Atspausdinti visus pirminius skaičius intervale [2, n].
*/

using namespace std;

int n;
bool tinka=false;

int main() {

    cout << "Iveskite skaiciu 'n' (didesni uz 1):\n> ";
    cin >> n;

    for(int i=2; i<=n; i++){
        tinka=true;
        for(int j=2; j<i; j++){
            if(i % j == 0) { tinka=false;}
        }
        if(tinka) cout <<i<<"\n";
    }

    return 0;
}
