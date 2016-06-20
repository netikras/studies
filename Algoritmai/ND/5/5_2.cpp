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
      3,  24,  7,
     12,  27, 35,
      6, 18,  21
};


void BubbleSort(int* myArr, int len){
    // Surūšiuos skaičius pagal pirmą skaitmenį
    int temp=0;
    for(int i=0; i<len; i++){
        for(int j=0; j<len; j++){
            if(myArr[j] < myArr[i]){ /*
            if(getFirstDig(myArr[j]) < getFirstDig(myArr[i])){ //*/
                temp=myArr[j];
                myArr[j]=myArr[i];
                myArr[i]=temp;
            }
        }
    }
}

int SparseArray(int *myArr,int* newArray, int myArrLen){
    int sveika=0;
    int liekana=0;
    int naujasIlgis=0;

    for(int i=0; i<myArrLen; i++){   // Einama per visus skaitmenis masyve
        sveika=myArr[i];            // Laikinam kintamajam priskiriama masyvo elemento reikšmė
        //cout << i << endl;

        while(sveika>=10){ // jeigu tikrinamas "skaitmuo" susideda iš kelių skaitmenų
            liekana=sveika%10;
            sveika/=10;
            newArray = (int*)realloc(newArray, sizeof(int)*(++naujasIlgis));
            newArray[naujasIlgis-1] = liekana;
        }
            newArray = (int*)realloc(newArray, sizeof(int)*(++naujasIlgis));
            newArray[naujasIlgis-1] = sveika;
    }
    return naujasIlgis;
}



//*
void makeDivisibleBy3(int* myArr, int myArrLen) {
    int sum=0;

    for(int newLength=myArrLen-1; newLength>0; newLength++){ // Darome prielaidą, kad nedalus iš 3. Todėl teks mažinti masyvo elementų skaičių, kad rastume dalų iš 3 derinį
        int *myTempArr = new int[newLength]; // laikinas masyvas

//        myTempArr = (int*)memcpy(myTempArr, myArr, sizeof(int)*newLength);


        delete(myTempArr);
    }

    /*for(int i=myArrLen-1; i>=0; i--){
        if(myArr[i] == 0) continue; // prametamas : neturi reikšmės dalybai iš 3

        if(i<myArrLen-2){ // Jei ne paskutinis elementas
            for(int j=i; j<myArrLen-1; j++)
                myArr[j] = myArr[j+1]; // shiftinama vienu elementu atgal
        }
        //myArr

    }
    */
}
//*/

int main() {

    int* sparsedArray = new int[0];
    int sparsedLength=0;
    int sum=0;
    int arrLen = sizeof(arr) / sizeof(arr[0]);

    sparsedLength=SparseArray(arr, sparsedArray, arrLen);
    BubbleSort(sparsedArray, sparsedLength);

    for(int i=0;i<sparsedLength;i++){

        if(sparsedArray[i] == 5 //Jei radom penketą, gali būti, kad teks jį kelti į galą - padaryt skaičių dalų iš 5.
           && sparsedArray[sparsedLength-1] != 0 // jau išrikiuota mažėjančiai, tai nulis bus gale, jei išvis yra
           && sparsedArray[sparsedLength-1] != 5 // O gal jau penketas buvo nukeltas į galą ankstesnėse iteracijose?
           ){
            int temp=sparsedArray[i]; // šitą kelsime į galą, nes turim penketą!

            for(int j=i; j<sparsedLength-1; j++)
                sparsedArray[j] = sparsedArray[j+1]; // shifting stuff back. Atlaisvinam vietos gale
            sparsedArray[sparsedLength-1] = temp; // numetam penketą į galą
        }
        sum+=sparsedArray[i];
        cout << "sparsedArray[" << i << "]:\t" << sparsedArray[i]<<endl;
        buffer<< sparsedArray[i];
    }

    if(sparsedArray[sparsedLength-1]%5 != 0){
        cout << "Keiskite masyvo elementus -- neįmanoma sudėlioti skaičiaus, dalaus iš 5\n";
        return 1;
    }

    if(sum%3 != 0){
        cout << "Keiskite masyvo elementus -- neįmanoma sudėlioti skaičiaus, dalaus iš 3\n";
        return 1;
    }

    cout << "Skaitmenų suma - " << sum << endl;
    cout << "Dalus iš 15 skaičius: \n\t" << buffer.str()<<endl;
    return 0;

}
