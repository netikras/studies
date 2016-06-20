#include <iostream>
#include <cstdlib>
#include "1.h"

/*!
    9. Vienmačiame masyve, kurį sudaro n realių elementų, rasti:
            a. Maksimalų pagal modulį masyvo elementą;
            b. Masyvo elementų sumą, esančių tarp pirmo ir antro teigiamo elemento.
        Išrūšiuoti masyvą tokia tvarka, kad nuliniai elementai būtų po visų likusių.
*/

using namespace std;


int findHighestValue(ARRAY_META* myArr){
// Funkcija ras indeksą didžiausio pagal modulį masyvo elemento
// Nebus naudojama <Math.h> biblioteka

    int tempMax=myArr->theArray[0];
    int maxIndex=0;

    for(int i=0; i<myArr->length; i++)
        if(myArr->theArray[i] > tempMax){
            tempMax = myArr->theArray[i];
            maxIndex=i;
        }
        else if(myArr->theArray[i] < (0 - tempMax)){
            tempMax = 0 - myArr->theArray[i];
            maxIndex = i;
        }
        return maxIndex;
}


int sumBetweenPositives(ARRAY_META* myArr){
// Funkcija ras sumą elementų reikšmių,
// esančių tarp pirmų dviejų teigiamų elementų

    int sum=0;
    int summingInProgress = 0; // 0 - neįjungtas; 1 - įjungtas; 2 - atliktas

    for(
            int i=0;
                i<myArr->length &&       // nutraukti ciklą, jei buvo sutikrintas visas masyvas
                summingInProgress < 2; // nutraukti ciklą, jei sumavimo procesas jau atliktas
            i++
        )
        if(myArr->theArray[i] > 0) summingInProgress++; // Jei teigiamas - perjungiamas jungiklis
        else if(summingInProgress == 1) sum+=myArr->theArray[i];
    if(summingInProgress == 2)  // Jei buvo rasti bent du teigiami elementai sekoje
        return sum;
        return 0; // grąžins nulį, jeigu nerastas antras arba pirmas teigiamas skaičius.
}

void zeroesToEnd(ARRAY_META* myArr){
// Funkcija surikiuos masyvą taip, kad nuliai būtų jo gale

    int temp;
    for(int i=0; i<myArr->length; i++){
        //if(myArr->theArray[i] == 0){    // Jei elementas - nulis
        if(myArr->theArray[i] % 10 == 0 || myArr->theArray[i] % -10 == 0){    // Jei elementas - nulinis
            temp = myArr->theArray[i];
            for(int j=i; j<myArr->length-1; j++) // einame per likusią seką atskirai
                myArr->theArray[j] = myArr->theArray[j+1]; // paslinkti ją vienu elementu atgal
            myArr->theArray[myArr->length-1] = temp; // ir sekos gale įrašyti elementą
        }
        cout << myArr->theArray[i]<<" ";
    }
    cout <<endl;
}

int main(){

    cout << TASK_DESCRIPTION << endl;
    ARRAY_META *arr; // .h faile

// Jei masyvas bus generuojamas atsitiktinai, bus naudojami rėžiai:
    int minArrayLen    =   5;     // masyvo ilgio MIN dydis
    int maxArrayLen    =  20;     // masyvo ilgio MAX dydis
    int minArrayValue  = -20;     // masyvo reikšmių MIN dydis
    int maxArrayValue  =  20;     // masyvo reikšmių MAX dydis

    arr->theArray = new int[0];
    arr->length   = 0;

    INIT(arr, minArrayLen, maxArrayLen, minArrayValue, maxArrayValue); // .h faile

    if(arr->length == -1) return 0;
    cout << endl<<endl;


    cout << "a) Didžiausias pagal modulį elementas yra |"
        << arr->theArray[findHighestValue(arr)]
        << "|"<<endl;

    cout << "b) Elementų suma tarp pirmų lyginių elementų yra "
        << sumBetweenPositives(arr)
        <<"."<<endl;


    cout << "Masyvas surikiuotas taip, kad nuliniai elementai būtų jo gale:\n\t";
        zeroesToEnd(arr);


	return 0;
}
