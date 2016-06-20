#include <iostream>
#include <fstream>
#include <string.h>
#include <stdlib.h>
/*
    1.	Išnagrinėti įrašo duomenų tipą (struct).
    Parašyti programą. Tegul žinomi kiekvieno iš 22 žmonių
    ūgis ir lytis. Rasti vidutinį vyrų ūgį.
*/

#define srcFile "zmones.txt"

using namespace std;


struct zmogai {
    char lytis; // 'v' or 'm'
    double ugis;
    zmogai* kitas = NULL;
};

zmogai* next(zmogai* last){
    return last->kitas;
}

void addZmogas(zmogai* last, char gender, double height){
    last->lytis=gender;
    last->ugis=height;
    last->kitas = new zmogai;
    last=next(last);
}

zmogai* newZmogas(zmogai* last){
    last->kitas = new zmogai;
    //cout << "dabar: " << last << endl <<"kitas " << last->kitas << endl;
    return last->kitas;
}

void fillZmogija(zmogai* zmogas){
    string line;
    int ct=0;
    ifstream inpf(srcFile);
    if(inpf.is_open()){
        while(inpf.good()){
                //cout << zmogas << endl;
                getline(inpf, line);
                if(line.empty()) continue;
                zmogas->lytis = line.c_str()[0];
                zmogas->ugis=atof(line.substr(2).c_str());
            zmogas = newZmogas(zmogas);

        }
        inpf.close();
    }
    else cerr << "Negaliu atverti failo skaitymui: " << srcFile << endl;

}

int main(){
    int cnt=0;
    double sum=0;

    zmogai* ZMOGAS = new zmogai;
    zmogai* Adomas = ZMOGAS;
    fillZmogija(ZMOGAS);
    ZMOGAS=Adomas;
//return 0;
    while(ZMOGAS!=NULL){
        //cout << ZMOGAS->lytis << " " << ZMOGAS->ugis << endl;
        if(ZMOGAS->lytis == 'v'){
            cnt++;
            sum+=ZMOGAS->ugis;
            cout << ZMOGAS->ugis<<endl;
        }
        //ZMOGAS=next(ZMOGAS);
        if (ZMOGAS->kitas != 0) ZMOGAS=next(ZMOGAS);
        else ZMOGAS=NULL;

    }
    cout << "Vidurkis: " << sum << "/" << cnt << "="<<sum/cnt<<endl;

	return 0;
}
