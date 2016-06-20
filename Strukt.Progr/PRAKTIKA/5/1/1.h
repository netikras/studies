#include <iostream>
#include <cstdlib>
#include <time.h>

using namespace std;

#define TASK_DESCRIPTION "\
** 1 užd. ***************************************** Darius Juodokas **\n\
** 9. Vienmačiame masyve, kurį sudaro n realių elementų, rasti:     **\n\
**             a. Maksimalų pagal modulį masyvo elementą;           **\n\
**             b. Masyvo elementų sumą, esančių tarp pirmo ir antro **\n\
**                teigiamo elemento.                                **\n\
**     Išrūšiuoti masyvą tokia tvarka, kad nuliniai elementai būtų  **\n\
**     po visų likusių.                                             **\n\
**********************************************************************\n\
"


struct ARRAY_META {
    // Struktūra, vienijanti masyvo adresą ir jo ilgį
    int* theArray;
    int length;
};

template<class arrayBound>
void randomizeArray(
                    ARRAY_META *myArr,
                    arrayBound minArrayLen,
                    arrayBound maxArrayLen,
                    arrayBound minArrayValue,
                    arrayBound maxArrayValue
                    ){
// Funkcija į masyvą įrašo atsitiktines reikšmes

    srand(time(0));
    if(myArr->length==0) myArr->length = minArrayLen + rand()%(maxArrayLen - minArrayLen);

    myArr->theArray=(int*)realloc(myArr->theArray, sizeof(int) * myArr->length);
    cout << "Elementų masyve:\t" << myArr->length <<endl;
    cout << "Masyvo reikšmės:\t";
    for(int i=0; i<myArr->length; i++){
        myArr->theArray[i] = minArrayValue + rand() % (maxArrayValue - minArrayValue);
        cout << myArr->theArray[i]<<" ";
    }

}

template<class arrayBound>
int INIT(
            ARRAY_META* myArr,
            arrayBound minArrayLen,
            arrayBound maxArrayLen,
            arrayBound minArrayValue,
            arrayBound maxArrayValue
            ){
// Funkcija, skirta masyvo struktūros užpildymui, reikšmių sutteikimui.

    int keepLoop = 1;
    string line;
    while(keepLoop){ // Kai kintamojo reikšmė taps '0', iš ciklo bus išeita
        cout << endl;
        cout << "1. Įvesti masyvą ranka\n";
        cout << "2. Naudoti visiškai atsitiktinį masyvą\n";
        cout << "3. Naudoti fiksuoto dydžio atsitiktinių reikšmių masyvą\n";
        cout << "\n0. Išeiti\n";
        cout << "  > ";
        getline(cin, line);

        switch(atoi(line.c_str())){
            case 0:
                myArr->length = -1; // EXIT
                keepLoop=0;
                break;
            case 1:
                cout << "Įveskite naujo masyvo dydį:\n  > ";
                getline(cin, line);
                myArr->length = atoi(line.c_str());
                myArr->theArray = (int*)realloc(myArr->theArray,
                                                sizeof(int)*myArr->length);
                for(int i=0; i<myArr->length; i++){
                    cout << "Įveskite "<<i<<"-ą elementą: > ";
                    cin >> myArr->theArray[i];
                }
                keepLoop=0;
                break;
            case 2:
                randomizeArray(
                            myArr,
                            minArrayLen,
                            maxArrayLen,
                            minArrayValue,
                            maxArrayValue
                            );
                keepLoop=0;
                break;
            case 3:
                cout << "Įveskite naujo masyvo dydį:\n  > ";
                getline(cin, line);
                myArr->length = atoi(line.c_str());
                randomizeArray(myArr, minArrayLen, maxArrayLen, minArrayValue, maxArrayValue);
                keepLoop=0;
                break;
            default:
                break;
        }
    }
    return myArr->length;
}
