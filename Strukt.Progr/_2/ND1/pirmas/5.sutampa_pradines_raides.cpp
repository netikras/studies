#include <iostream>

/*
5. Duoti du žodžiai. Nustatyti, kiek pradinių raidžių juose sutampa.
*/

using namespace std;


int main(){
    int ilgis=50;
    char zodis1[ilgis], zodis2[ilgis];
    for(int i=0; i<ilgis; i++){zodis1[i]=2;zodis2[i]=2;}

    cout << "Įveskite pirmą žodį\n";
    cin.get(zodis1,ilgis);
    cout << "Įveskite antrą žodį\n";
    cin.ignore();
    cin.get(zodis2,ilgis);

    int sutampa=0;

    for(int i=0; i<ilgis; i++) if (zodis1[i]==zodis2[i])sutampa++; else break;


    cout<<"Radau sutapusių simbolių: "<<sutampa;

return 0;
}
