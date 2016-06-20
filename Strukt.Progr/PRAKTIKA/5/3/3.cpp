#include <iostream>
#include <cstdlib>
#include <string.h>
#include <stdio.h>
#include "3.h"

/*!
9. Aprašyti struktūrą vardu TRAIN, sudarytą iš tokių laukų:
       * Paskirties taškas (vietovė);
       * Traukinio numeris;
       * Išvykimo laikas.
    Parašyti programą, kuri atlieka tokius veiskmus:
       a) Įveda iš klaviatūros duomenis į masyvą, kuris yra sudarytas
          iš 8-nių struktūrų TRAIN tipo; įrašai turi būti išrūšiuoti
          pagal traukinio numerį;
       b) Išveda į ekraną informaciją apie traukinį, kurio numeris
         įvedamas iš klaviatūros.
           !!! Jeigu tokių reisų nėra, išveda atitinkamą pranešimą.
*/

using namespace std;


void showTrain(TRAIN train, bool printFooter=true){
/*
    Funkcija atspausdins VIENO traukinio informaciją
*/

    cout << TABLE_HEADER;

    cout <<"     |"<<printCentered(11, train.number);
    cout << "|"<<printCentered(21, train.dest);
    cout << "|"<<printCentered(6, train.leaves[0]);
    for(int i=1; i<5; i++)
        cout << "|"<<printCentered(4, train.leaves[i]);
    cout << "|"<<endl;
    if(printFooter) cout << TABLE_FOOTER;
}

template <class TRAINS_COUNT>
void showTrains(TRAIN* trains, TRAINS_COUNT trainCnt){
/*
    Funkcija atspausdins VISŲ traukinių sąrašą
*/

    cout << TABLE_HEADER;

    for(int i=0; i<trainCnt; i++){
        cout <<"     |"<<printCentered(11, trains[i].number);
        cout << "|"<<printCentered(21, trains[i].dest);
        cout << "|"<<printCentered(6, trains[i].leaves[0]);
        for(int j=1; j<5; j++)
            cout << "|"<<printCentered(4, trains[i].leaves[j]);
        cout << "|"<<endl;
    }
    cout << TABLE_FOOTER;

}

template <class TRAINS_COUNT>
void sortList(TRAIN* train, TRAINS_COUNT trainCnt){
/*
    Funkcija surūšiuos traukinių sąrašą
    pagal traukinio numerį mažėjančia tvarka
*/
    TRAIN temp;

    for(int i=0; i<trainCnt; i++){
        for(int j=0; j<trainCnt; j++){
            if(train[i].number > train[j].number){
                temp=train[j];
                train[j]=train[i];
                train[i]=temp;
            }
        }

    }
}


template <class TRAINS_COUNT>
void trainsMenu(TRAIN *train, TRAINS_COUNT trainCnt){
/*
    Funkcija išves meniu į ekraną, kuriame galima pasirinkti,
    kurio traukinio informaciją išvesti į ekraną.
*/
    int keepLoop=1;
    string line;
    int trainToPrint;
    while(keepLoop){
        trainToPrint=-1;
        cout << "b) Įveskite traukinio numerį, kurio duomenis norite peržiūrėti."<<endl;
        cout << "Įveskite nulį '0', kad atspausdintumėte visų traukinių grafikus."<<endl;
        cout << "\tNorėdami išeiti įveskite tašką."<<endl;
        cout << " > ";
        getline(cin, line);

        if(line==".") keepLoop=0;
        else if(line=="0") showTrains(train, trainCnt);
        else
            if(atoi(line.c_str())==string::npos)
                cerr << "Įveskite skaičių!"<<endl;
            else {
                for(int i=0; i<trainCnt; i++)
                    if(train[i].number == atoi(line.c_str()))
                        trainToPrint=i;
                if(trainToPrint>=0) showTrain(train[trainToPrint]);
                else cerr << "Nėra tokio traukinio"<<endl;
            }
    }
}

int main(){
    cout << TASK_DESCRIPTION <<endl; // .h faile
    int trainCnt = 8; // kiek traukinių bus masyve

    TRAIN *tr = new TRAIN[trainCnt]; // .h faile
   // cout << endl;

    INIT(tr, trainCnt, true); // bus sukurtas traukinių masyvas. Nenurodžius 'true' masyvas bus sukurtas automatiškai.
//a)
    cout << "a) Traukiniai surūšiuojami pagal numerį, mažėjimo tvarka:"<<endl;
    sortList(tr, trainCnt);
    showTrains(tr, trainCnt);
// b)
    cout << endl<<endl;
    trainsMenu(tr, trainCnt);

	return 0;
}
