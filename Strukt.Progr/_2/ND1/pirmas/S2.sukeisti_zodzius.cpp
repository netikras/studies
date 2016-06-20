#import <iostream>

/*
2. Duotas sakinys pakeisti vietomis pirmą ir paskutinius žodžius.
*/

using namespace std;

int main(){
    int ilgis=500;
    int zodis1_ilgis=0, zodis2_ilgis=0, zodis2_pradzia=0;
    char sakinys[ilgis];
    bool reikia_zodis_1=true;
    char zodis1[ilgis], zodis2[ilgis];
    for(int i=0; i<ilgis; i++){zodis1[i]=2;zodis2[i]=2;sakinys[i]=2;}

cout<<"Įveskite sakinį (iki "<<ilgis<<" simbolių)\n";
cin.get(sakinys, ilgis);

    for(int i=0; i<ilgis; i++){

        if(reikia_zodis_1){
            (sakinys[i]!=' '?zodis1[i]=sakinys[i]:reikia_zodis_1=false); //pirmas tarpas suvalgomas čia
            zodis1_ilgis++;
            continue;
        }

        zodis2[zodis2_ilgis]=sakinys[i];
        if(sakinys[i+2]==2) break; else zodis2_ilgis++;

        if(sakinys[i]==' ' && sakinys[i+1]!=2) { //apsauga nuo vieno 'trailing space'
            zodis2_ilgis=0;
            zodis2_pradzia=i+1;
        }

    }


for(int i=0; i<zodis2_ilgis+1; i++) cout<<zodis2[i];
for(int i=zodis1_ilgis-1; i<zodis2_pradzia; i++) cout << sakinys[i];
for(int i=0; i<zodis1_ilgis; i++) cout << zodis1[i];


//kitas būdas būtų surasti rėžius tų žodžių ir tiesiog juos išspausdinti tiesiai iš sakinio - t.y. nekurti
//masyvų pirmam ir paskutiniam žodžiams.
    return 0;
}
