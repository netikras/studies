#import <iostream>

/*
1. Duotas sakinys. Rasti didžiausią kiekį iš eilės einančių tarpų.
*/


using namespace std;

int main(){

    int ilgis=500;
    int tarp_max=0;
    int tarpai=0;
    char sakinys[ilgis]; for(int i=0; i<ilgis; i++) sakinys[i]=2;

    cout << "Įveskite sakinį su pasikartojančiais tarpais.\n-> ";
    cin.get(sakinys,ilgis);


    for(int i=0; i<ilgis; i++){
        if(sakinys[i]==' ')
            tarpai++;
        else {
            (tarpai>tarp_max?tarp_max=tarpai:false);
            tarpai=0;
        }
    }

cout << "\nDaugiausia iš eilės einanšių tarpų - "<<tarp_max;
    return 0;
}
