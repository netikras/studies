#include <iostream>
#include <math.h>
#include <stdio.h>
/*
10. Duotas dvimatis masyvas:
	a. Rasti k-tojo stulpelio visų elementų sumą.
	b. Rasti minimalių elementų kiekį.
	c. Rasti didžiausią stulpelių elementų sumą. Uždavinį išspręsti dviem būdais.
	d. Didžiausią masyvo elementą sukeisti vietomis su mažiausiu.
	e. Surūšiuoti didėjančiai lyginius masyvo elementus. Lyginių elementų indeksai yra pastovūs.
*/

using namespace std;

int a();
int b();
int c(int budas);
void d();
int e();

int masyvas[9][9]={

{2,1,3,4,5,6,7,8,9},
{32,33,34,35,36,37,38,39,10},
{31,56,57,58,59,60,61,40,11},
{30,55,72,73,74,75,62,41,12},
{29,54,71,80,81,76,63,42,13},
{28,53,70,79,78,1,64,43,14},
{27,52,69,68,67,1,65,44,15},
{26,51,50,49,48,47,46,45,16},
{25,24,23,22,21,20,19,18,17},

};


int main(){

    int xx=10;
    int mass[xx];
    cout << "suma: " << a()<< "\n";

    cout << "mažiausių elementų yra " << b() << "\n";

    c(0);

    d();

    e();


    return 0;
}



int a() {
    cout << "\na. Rasti k-tojo stulpelio visų elementų sumą.\n";
    int suma=0;
    int st_nr=-1;
    cout << "Kelinto stulpelio elementų sumą norite sužinoti [0;9]?\n>";
    cin >>st_nr;
    if(st_nr == -1) { cout << "Neįvedėte jokio stulpelio numerio\n"; cin >>st_nr; return 0; }

    for(int i=0; i<9; i++){
        suma=suma+masyvas[i][st_nr];
    }



   return suma;
}


int b(){
    cout << "\nb. Rasti minimalių elementų kiekį.\n";
    int min_el[2]={ masyvas[0][0], 0 };

    for(int i=0; i<9; i++){
        for(int j=0; j<9; j++){
            if(masyvas[i][j] < min_el[0]) {
                min_el[0]=masyvas[i][j];
                min_el[1]=1;
            } else {
                if(masyvas[i][j] == min_el[0]) min_el[1]++;
            }
            //cout << min_el[1]<<"\n";
        }
    }
    cout << "mažiausias elementas yra " << min_el[0]<<"\n";
    return min_el[1];
}


int c(int budas=0){
    cout <<"\nc. Rasti didžiausią stulpelių elementų sumą. Uždavinį išspręsti dviem būdais.\n";
    int suma=0;
    int sumos[9]={};
    switch(budas){
        case 0:
            cout <<"Pasirinkite kuriuo būdu skaičiuoti: 1 ar 2: >";
            cin >> budas;
            suma=c(budas);
        break;
        case 1:

            cout << "Pirmas būdas:\t";
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    sumos[i] += masyvas[i][j];
                }
            }

            for(int i=0; i<9; i++){
                (sumos[i] > suma?suma=sumos[i]:false);
            }
            return suma;
        break;
        case 2:
            int suma_tmp=0;
            cout << "Antras būdas:\t";
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    suma_tmp += masyvas[i][j];
                }
                (suma_tmp > suma?suma=suma_tmp:false);
                suma_tmp=0;
            }

            return suma;
        break;
    }

    cout << "Didžiausia stulpelio suma yra: " << suma << endl;


}


void d(){
    cout << "\nd. Didžiausią masyvo elementą sukeisti vietomis su mažiausiu.\n";
    int min=masyvas[0][0], max=masyvas[0][0];
    for(int i=0; i<9; i++){
        for(int j=0; j<9; j++){
            if(masyvas[i][j] < min){
                min = masyvas[i][j];
            } else {
                if(masyvas[i][j] > max){
                    max=masyvas[i][j];
                }
            }

        }
    }

    for(int i=0; i<9; i++){
        for(int j=0; j<9; j++){
            if(masyvas[i][j] == min){
                masyvas[i][j] = max;
            } else {
                if(masyvas[i][j] == max){
                    masyvas[i][j] = min;
                }
            }
        }
    }





    cout << "Sukeistas masyvas:\n";
    for(int i=0; i<9; i++){
        cout << "\t";
        for(int j=0; j<9; j++){
            cout << masyvas[i][j] <<(masyvas[i][j]<10?"  ":" ");
        }
        cout << "\n";
    }

}


int e(){
    cout <<"\ne. Surūšiuoti didėjančiai lyginius masyvo elementus. Lyginių elementų indeksai yra pastovūs.\n";
    int zyma=0;
    int lyg[81], lyg_sk_sk=0;

    for(int i=0; i<9; i++){
        for(int j=0; j<9; j++){
            if((masyvas[i][j] % 2) == 0 ) {
                lyg[lyg_sk_sk] = masyvas[i][j];
                lyg_sk_sk++;
               }


        }

    }

    int temp;
    int *sorted_lyg = new int[lyg_sk_sk];
    for(int i=lyg_sk_sk-1; i>=0; i--){

        for(int j=0; j<=i; j++){

            if(lyg[j+1] < lyg[j]){
                temp=lyg[j];
                lyg[j] = lyg[j+1];
                lyg[j+1] = temp;
            }
        }
    }

cout << "\nSurikiuotas masyvas:\n";
    for(int i=0; i<9; i++){
        cout << "\t";
        for(int j=0; j<9; j++){
            if((masyvas[i][j] % 2) == 0 ) {
                masyvas[i][j] = lyg[zyma++];

               }
            cout << masyvas[i][j] <<(masyvas[i][j]<10?"  ":" ");

        }
        cout << "\n";
    }
cout << zyma<<"/"<<lyg_sk_sk;
    return 0;
}
