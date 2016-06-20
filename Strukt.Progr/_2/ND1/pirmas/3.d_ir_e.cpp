#include <iostream>
#include <ctime>
#include <cstdlib>

/*
3. Įsitikinti, ar yra dvimačiame simbolių masyve kiekvienoje eilutėje simbolis `e`,
    kuris yra dešiniau simbolio `d`.
    Žinoma, kad kiekvienoje eilutėje simbolis `d` gali būti tik vieną kartą.
*/

using namespace std;


int main(){

bool kiekvienoje=true;

char masyvas[5][5]={
    {'a','d','g','e','t'},
    {'p','e','c','d','e'},
    {'q','i','d','e','d'},
    {'f','m','k','d','e'},
    {'d','e','y','f','s'}
};

//cout << masyvas[0][0];

bool rasta_d=false, rasta_e=false, failed=false;;


for(int i=0; i<5; i++){
    for(int j=0; j<5; j++){
/*        if(!rasta_d && masyvas[i][j]=='e'){
            failed=true;
        }else{
            (masyvas[i][j]=='e'?rasta_e=true:false);
        }
        if(masyvas[i][j]=='d')
            rasta_d=true;

*/
        if(masyvas[i][j]=='d') rasta_d=true;
        if(rasta_d && masyvas[i][j]=='e') rasta_e=true;
    }

    (!rasta_e?failed=true:false);
    rasta_d=false, rasta_e=false;
}

    srand(time(0));
//cout << rand() % 7<<"\n"<<(char)(rand()%(126-32)+32);

cout << (failed?"NE":"")<<"visur simbolis 'e' rastas dešiniau simbolio 'd'.\n";


return 0;
}
