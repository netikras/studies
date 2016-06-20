//prog8
//8. Parasyti rekursine funkcija, kuri nustato, kiek skaitmenu yra naturiniame skaiciuje.
#include <iostream>
#include <cstdlib>

using namespace std;

void recur(int n);

int sk_sk=0;

main(){
  cout<<"8. Parasyti rekursine funkcija, kuri nustato, kiek skaitmenu yra naturiniame skaiciuje.\n\n";

    int skaicius;

    cout<<"Iveskite naturini skaiciu >";
    cin>>skaicius;

    recur(skaicius);
    cout<<"Skaicius "<<skaicius<<" sudarytas is "<<sk_sk<<" skaitmenu.\n";


    return 0;
}//main()

void recur(int n){
    n/=10;
    sk_sk++;
    //cout<<"n="<<n<<";\tsk_sk="<<sk_sk<<"\n";
    if(n>0){
        recur(n);
    }
}//recur()
