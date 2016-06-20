#include <iostream>
#include <cstdlib>
#include <sstream>
#include <string.h>
/*
    5.	Duota tam tikra skaitmenų seka,
    nemažiau kaip iš 9 narių. Parašykite programą,
    kuri nustatys, kokį maksimalų skaičių
    galima sudaryti iš šios sekos, su sąlyga,
    kad jis turi dalintis iš 15.
*/

// dalijasi iš 15, vadinasi, dalinsis ir iš 3, ir iš 5.
// iš 5 dalijasi tie, kurie baigiasi ...0 arba ...5
// iš 3 dalijasi tie, kurių skaitmenų suma dalijasi iš 3.

using namespace std;

stringstream buffer;

// Masyvo elementai turi būti unikalūs
int arr[]={
      3,  4,  9,
     2,  7, 0,
      6, 1,  1
};

int getFirstDig(int n){
    int liekana=0;
    int sveika=n;

    while(sveika>=10)
        sveika/=10;

    return sveika;
}


void semiBubbleSort(int* myArr, int len){
    // Surūšiuos skaičius pagal pirmą skaitmenį
    int temp=0;
    for(int i=0; i<len; i++){
        for(int j=0; j<len; j++){
            //if(myArr[j] > myArr[i]){ /*
            if(getFirstDig(myArr[j]) < getFirstDig(myArr[i])){ //*/
                temp=myArr[j];
                myArr[j]=myArr[i];
                myArr[i]=temp;
            }
        }
    }
}

bool dividesBy3(int* n, int len){
    int liekana=0;
    int sveika=0;
    int suma=0;

    for(int i=0; i<len; i++){   // Einama per visus skaitmenis masyve
        sveika=n[i];            // Laikinam kintamajam priskiriama masyvo elemento reikšmė
        while(sveika>=10){ // jeigu tikrinamas "skaitmuo" susideda iš kelių skaitmenų
            liekana=sveika%10;
            sveika/=10;
            suma+=liekana;
        }

        suma+=sveika;
    }
    cout << "Skaitmenų suma: " << suma << endl;
    if(suma%3==0)
        return true;

        return false;
}

int printStrRec(int* myArr,int* ans,int howManyToPrint,int startPoint){
    bool toSkip;

    if(startPoint==howManyToPrint){
            if(ans[howManyToPrint-1] % 5 == 0){
                for(int i=0; i<howManyToPrint; i++)
                    cout << ans[i];
                cout <<endl;
                return 1; }
    }
    else{
        for(int x=0;x<howManyToPrint;x++){
                toSkip=false;
            ans[startPoint]=myArr[x];
            if(startPoint > 0 && ans[startPoint-1] == ans[startPoint]) toSkip=true;
            for(int i=0; i<startPoint; i++){
                if(ans[i] == ans[startPoint])
                    toSkip=true;
            }
            if(toSkip) continue;
            if(printStrRec(myArr,ans,howManyToPrint,startPoint+1) == 1) return 1;
        }

    }
}

void printStrings(int* myArr,int howManyToPrint){
    int startPoint=0;
// Laikinas masyvas
    int * ans = new int[howManyToPrint];

    printStrRec(myArr,ans,howManyToPrint,startPoint);
}



int main() {

    int* lastDig = NULL;
    int lastDigLen=0;
    int arrLen = sizeof(arr) / sizeof(arr[0]);

    // Neverta tęsti, jei masyvo elementų suma nėra dali iš 3.
    if(!dividesBy3(arr, arrLen)) {
        cout << "Keiskite masyvo elementus -- neįmanoma sudėlioti skaičiaus, dalaus iš 3\n";
        return 1;
    }

    // getting the last numbers (divisible by 5)
    for(int i=0; i<arrLen; i++){
        if(arr[i]%5 == 0){
            lastDig=(int*)realloc(lastDig, sizeof(int)*(++lastDigLen));
            lastDig[lastDigLen-1] = arr[i];
        }
    }

    if(lastDig == NULL){
        cout << "Keiskite masyvo elementus -- neįmanoma sudėlioti skaičiaus, dalaus iš 5\n";
        return 1;
    }

// Ieškome didžiausio skaičiaus. Tai greičiau padarysime, jei
// masyvą surūšiuosime mažėjimo tvarka.
    semiBubbleSort(arr, arrLen);

    printStrings(arr,arrLen);
    return 0;
}
