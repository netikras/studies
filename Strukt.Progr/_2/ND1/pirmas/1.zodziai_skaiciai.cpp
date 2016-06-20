#import <iostream>
#import <stdio.h>

/*
1. Duotas tekstas, kuriame yra skaičių. Rasti didžiausią skaičių.
*/


using namespace std;


int main(){

    int chr_len=30;
    char max=0;
    char eilute[chr_len];

    cout<<"Įveskite teksto eilutę su keletu skaičių\n>";
    cin.get(eilute, chr_len);


    for(int i=0; i<chr_len; i++){
        if(eilute[i] >= '0' && eilute[i] <= '9' && eilute[i] > max) max=eilute[i];
    }

    cout<<"max="<<max;


    return 0;
}
