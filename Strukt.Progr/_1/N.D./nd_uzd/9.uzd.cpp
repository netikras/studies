#include <iostream>
#include <stdio.h>
#include <math.h>
/*
9. Duotas dvimatis masyvas 9x9. Rasti skaičių seką, kuri gaunasi skaitant šį masyvą spirale (pav.: spirale.png)
*/


using namespace std;


int N=9;

int matrica[9][9]={
{1,2,3,4,5,6,7,8,9},
{32,33,34,35,36,37,38,39,10},
{31,56,57,58,59,60,61,40,11},
{30,55,72,73,74,75,62,41,12},
{29,54,71,80,81,76,63,42,13},
{28,53,70,79,78,77,64,43,14},
{27,52,69,68,67,66,65,44,15},
{26,51,50,49,48,47,46,45,16},
{25,24,23,22,21,20,19,18,17},
};


int main(){

    int kvadr_sk = (N+1)/2;
    int skaicius=1;
    int sono_ilgis=N;

    int j=1;

    for (int i = 0; i < kvadr_sk; i++) {
        //## Viršus
      for (j--; j < sono_ilgis; j++)  cout<< matrica[i][i + j] << " ";

        //## Dešinysis šonas
      for (j = 1; j < sono_ilgis; j++) cout << matrica[i + j][N - 1 - i] << " ";

        //## Apačia
      //for (j = sono_ilgis - 2; j > -1; j--) {
      for (j-=2; j > -1; j--) cout << matrica[N - 1 - i][i + j] << " ";

        //## Kairysis šonas
      for (j = sono_ilgis - 2; j > 0; j--) cout << matrica[i + j][i] << " ";

      sono_ilgis -= 2;
    }


    return 0;
}

