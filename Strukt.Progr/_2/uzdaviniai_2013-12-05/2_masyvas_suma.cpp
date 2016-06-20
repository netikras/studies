#include <iostream>
#include <cmath>

/*
2. Duotas realiųjų skaičių masyvas.
Apskaičiuokite sumą skaičių, esančių tarp mažiausio ir didžiausio masyvo elemento.
*/


using namespace std;


int main(){

    int masyvas[10]={4, 2, 3, 5, 3, 8, 5, 8, 4, 7}, allmin[10], allmax[10];

    int min=999999, max=0, imin=0, imax=0, suma=0, delta=10, minc=0, maxc=0;

    for(int i=0; i<10; i++){
        if(min > masyvas[i]) { min=masyvas[i]; imin=i; }
        if(max < masyvas[i]) { max=masyvas[i]; imax=i; }

    }

    for(int i=0; i<10; i++){ //susirenkame visus indeksus, kur yra mažiausios ir didžiausios reikšmės
        if(masyvas[i] == min){allmin[minc]=i; minc++; }
        if(masyvas[i] == max){allmax[maxc]=i; maxc++; }
    }

    // randame mažiausią atstumą (delta) tarm min/max indeksų
    for(int i=0; i<minc; i++){
        for(int j=0; j<maxc; j++){
            min=abs(allmin[i] - allmax[j]);
            if(min < delta) {
                delta=min;
                imin=allmin[i];
                imax=allmax[j];
            }
            //cout << "i="<<i<<", j="<<j<<"\nallmin="<<allmin[i]<<", allmax="<<allmax[j]<<"\ndelta="<<delta<<"\n";

        }
    }

    if(imax<imin) {int temp=imin; imin=imax; imax=temp;}
    //cout << min << " " << max;


    for(int i=imin+1; i<imax; i++) {
        cout << masyvas[i] << " ";
        suma+=masyvas[i];
    }

    cout << "\n\n::::: Suma: "<<suma;

    return 0;
}
