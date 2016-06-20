#include <iostream>
#include <string.h>
#include "2.h"

/*!
    9. Duota sveikaskaitinė stačiakampė matrica. Rasti pirmo iš stulpelių numerį,
        kuriame yra bent vienas nulinis elementas.
       Sveikaskaitinės matricos eilutės charakteristika vadinsime sumą jos neigiamų
        lyginių elementų. Perstatydami duotosios matricos eilutes,
        išdėstykite jos charakteristikų mažėjimo tvarka.
*/

using namespace std;


int columnNo(ARRAY_META *myArr){
// Funkcija randa kairiausio stulpelio
// numerį, kuriame yra elementas NULIS
// Jei nulis neras tas - grąžina -1

    int retVal=-1;
    int col=0;

    for(int i=0; i<myArr->width && retVal<0; i++)
        for(int j=i; j<myArr->width * myArr->height && retVal<0; j+=myArr->width)
              if(myArr->theArray[j] == 0) retVal=i;
    //cout << retVal<<endl;
    return retVal;
}


string swapLines(ARRAY_META *myArr){
// Funkcija ras kiekvienos matricos eilutės
// charakteristiką ir pertvarkys matricą
// charakteristikų mažėjimo tvarka

    int* tempLine = (int*) malloc(sizeof(int)*myArr->width);
    int sum1=0, sum2=0;

    BUFFER.str("");

    // BURBULAS
    // burbulo ciklas1
    for(int i=0; i<myArr->height; i++){

            sum1=0;
            //skaičiuojama suma
        for(int k=0; k<myArr->width; k++) // einama per eilutę ir skaičiuojama neigiamų lyginių reikšmių suma
            if(myArr->theArray[i*myArr->width+k]<0 && myArr->theArray[i*myArr->width+k]%2 == 0) sum1+=myArr->theArray[i*myArr->width+k];
            //cout << "\tsum1="<<sum1<<endl;

        //burbulo ciklas2
        for(int j=0; j<myArr->height; j++){

            sum2=0;
            //skaičiuojama antra suma
            for(int l=0; l<myArr->width; l++) // einama per eilutę ir skaičiuojama neigiamų lyginių reikšmių suma
                if(myArr->theArray[j*myArr->width+l]<0 && myArr->theArray[j*myArr->width+l]%2 == 0)
                        sum2+=myArr->theArray[j*myArr->width+l];

               // cout << "sum2="<<sum2<<endl;
            if(sum2<sum1){ // burbulo širdis
                memcpy( tempLine,                            &myArr->theArray[i*myArr->width],   sizeof(int) * myArr->width );
                memcpy( &myArr->theArray[i*myArr->width],    &myArr->theArray[j*myArr->width],   sizeof(int) * myArr->width );
                memcpy( &myArr->theArray[j*myArr->width],    tempLine,                           sizeof(int) * myArr->width );
            }
            // tai - dvimatis masyvas, todėl darom prielaidą, kad bet kurios iteracijos metu galėjo
            // pasikeisti sum1 reikšmė. Todėl po kiekvienos iteracijos ją perskaičiuojame.
            sum1=0;
            for(int k=0; k<myArr->width; k++){ // einama per eilutę ir skaičiuojama neigiamų lyginių reikšmių suma
                if(myArr->theArray[i*myArr->width+k]<0 && myArr->theArray[i*myArr->width+k]%2 == 0) sum1+=myArr->theArray[i*myArr->width+k];
            }
        }
    }

// Sugeneruojamas naujas masyvas (vizualiai)
    for(int i=0; i<myArr->width * myArr->height; i++){
            if(i % myArr->width == 0) BUFFER << endl; // nauja eilutė
            if(myArr->theArray[i]>=0) BUFFER << " "; // kompensuojame minuso ženklą
            if(myArr->theArray[i] < 10 && myArr->theArray[i] > -10) BUFFER << " "; // kompensuojame x<10
            BUFFER << myArr->theArray[i]<< " ";
        }
    BUFFER <<endl;

    return BUFFER.str();
}

int main(){
    cout << TASK_DESCRIPTION << endl;
    ARRAY_META *arr = new ARRAY_META(); // .h faile
    int columnWithZero=-1;

    arr->theArray = new int[1];
    arr->width    = 0;
    arr->height   = 0;

// Jei masyvas bus generuojamas atsitiktinai, bus naudojami rėžiai:
    int minArrayWidth   =   5,  maxArrayWidth    = 10;     // matricos plotis
    int minArrayHeight  =   5,  maxArrayHeight   = 10;     // matricos aukštis
    int minArrayValue   = -20,  maxArrayValue    = 20;     // masyvo reikšmių

    INIT(
         arr,
         minArrayWidth,
         maxArrayWidth,
         minArrayHeight,
         maxArrayHeight,
         minArrayValue,
         maxArrayValue
         ); // .h faile


   cout << endl;

   cout << "a) Stulpelio su nuliu numeris: ";
        columnWithZero=columnNo(arr);
        if(columnWithZero<0) cout << "nerastas..."; else cout << columnWithZero;
        cout << endl;


    cout << "b) Pertvarkyta matrica:\n";
        cout << swapLines(arr);


	return 0;
}
