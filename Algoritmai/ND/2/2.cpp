#include <iostream>
#include <string.h>
#include <cstdlib>
/*
    2.	Rasti mažiausią bendrą sveikųjų skaičių masyvo N elementų kartotinį.
*/

using namespace std;


int dbd(int m, int n){
        int tmp;
        while(m){
            tmp = m;
            m = n % m;
            n = tmp;
        }
        return n;
}

int mbk(int m, int n) {
        return m / dbd(m, n) * n;
}

int main() {
    string line;
    int len=0;
    int *arr = NULL;// = (int*)malloc(sizeof(int));
    int prev=0;

    cout << "Įveskite skaičius MBK skaičiavimui atskiose eilutėse" <<endl
    << "Baigę vesti įveskite '.' (tašką)" << endl<<endl;
    while(line!="."){
            cout << " > ";
            getline(cin, line);
            if(line == "." || line.empty()) continue;
            arr=(int*)realloc(arr, sizeof(int) * (++len));
            arr[len-1] = atoi(line.c_str());
    }

    prev=arr[0];
    for(int i=0; i<len; i++)
        prev=mbk(prev, arr[i]);
    cout << "MBK = " << prev<<endl;
        return 0;
}
