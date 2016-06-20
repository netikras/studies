#include <iostream>
#include <stdio.h>
/*
8. Duotas dvimatis masyvas iš 15 eilučių ir 2 stulpelių. Rasti minimalią sumą elementų dvejose gretimose eilutėse.
*/
using namespace std;

int masyvas[15][2]={
    {17,92},
    {3,4},
    {5,6},
    {7,8},
    {9,10},
    {11,12},
    {13,14},
    {15,16},
    {17,18},
    {19,20},
    {21,22},
    {23,24},
    {25,26},
    {27,28},
    {29,30},
};

int min_suma=999999, el=55;

int main(){

    for(int i=0; i<14; i++){
        if((masyvas[i][0] + masyvas[i][1] + masyvas[i+1][0] + masyvas[i+1][1]) < min_suma) {min_suma=masyvas[i][0] + masyvas[i][1] + masyvas[i+1][0] + masyvas[i+1][1]; el=i;}
    }

    printf("Mažiausia dviejų gretimų eilučių elementų suma yra %d:\n\t%d + %d + %d + %d = %d\n\n", min_suma, masyvas[el][0], masyvas[el][1], masyvas[el+1][0], masyvas[el+1][1], min_suma);
    return 0;
}
