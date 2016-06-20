#include <iostream>
#include <stdio.h>
#include <math.h>
/*
6. Parašykite programą, kuri užpildo NxN matricą skaičiais nuo 1 iki N^2 pagal spiralę, pradedant nuo kairiojo viršutinio kampo.
*/


using namespace std;

void rodytiMasyva(int **mas, int n);

int **matrica, N, a;
int x=0;


int main(){
    cout << "Įveskite skaičių - masyvo kraštinės ilgį\n\t>>";
    cin >> N;
    /*
    if((double)sqrt(N) != (int)sqrt(N)) { cout <<"nesitraukia šaknis\n"; return 1; }
    a=sqrt(N);
    */

    matrica = new int*[N];
    for(int i=0; i<N; i++) matrica[i] = new int[N];


    int kvadr_sk = (N+1)/2;
    int skaicius=1;
    int sono_ilgis=N;

    int j;

    for (int i = 0; i < kvadr_sk; i++) {
      for (j = 0; j < sono_ilgis; j++) {
        matrica[i][i + j] = skaicius++;
      }

      for (j = 1; j < sono_ilgis; j++) {
        matrica[i + j][N - 1 - i] = skaicius++;
      }

      for (j = sono_ilgis - 2; j > -1; j--) {
        matrica[N - 1 - i][i + j] = skaicius++;
      }

      for (j = sono_ilgis - 2; j > 0; j--) {
        matrica[i + j][i] = skaicius++;
      }

      sono_ilgis -= 2;
    }

    rodytiMasyva(matrica, N);

    return 0;
}



void rodytiMasyva(int **mas, int n){
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
        cout << mas[i][j] << (mas[i][j]<10?"  ":" ");

        }
    cout << "\n";
    }

}
