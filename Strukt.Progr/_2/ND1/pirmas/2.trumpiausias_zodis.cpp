#import <iostream>

/*
2. Duotas sakinys. Rasti trumpiausią žodį jame.
*/

using namespace std;

int ilgis=99;

int main(){

    char sakinys[ilgis], dabartinis_zodis[ilgis], min_zodis[ilgis];
    int dabartinis_zodis_ilgis=0, min_ilgis=ilgis;

    for(int i=0; i<ilgis; i++) { //inicializuojame masyvus
        sakinys[i]=2;
        min_zodis[i]=2;
        dabartinis_zodis[i]=2;
    }

    cout << "Įveskite keletą žodžių:\n-> ";
    cin.get(sakinys, ilgis);
    for(int i=0; i<ilgis; i++){
        if(sakinys[i] == ' ' || sakinys[i] == '\0' || sakinys[i] == '-'){
            if(dabartinis_zodis_ilgis < min_ilgis && dabartinis_zodis_ilgis > 0){
                min_ilgis=dabartinis_zodis_ilgis;
                for(int j=0; j<dabartinis_zodis_ilgis; j++) min_zodis[j] = dabartinis_zodis[j];
            }
                dabartinis_zodis_ilgis=0;
        } else {
            dabartinis_zodis[dabartinis_zodis_ilgis]=sakinys[i];
            dabartinis_zodis_ilgis++;
        }

    }

cout << "\nTrumpiausias žodis - "; for(int i=0; i<min_ilgis; i++) cout << min_zodis[i];


    return 0;
}
