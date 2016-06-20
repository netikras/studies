#include <iostream>
#include <cstdlib>
#include <time.h>
#include <sstream>

using namespace std;

#define  TASK_DESCRIPTION "\
** 2 užd. ********************************************** Darius Juodokas **\n\
** 9. Duota sveikaskaitinė stačiakampė matrica. Rasti pirmo iš           **\n\
**    stulpelių numerį, kuriame yra bent vienas nulinis elementas.       **\n\
**                                                                       **\n\
** Sveikaskaitinės matricos eilutės charakteristika vadinsime            **\n\
**    sumą jos neigiamų lyginių elementų. Perstatydami duotosios         **\n\
**    matricos eilutes, išdėstykite jos charakteristikų mažėjimo tvarka. **\n\
***************************************************************************\n\
"

stringstream BUFFER;


struct ARRAY_META {
    // Struktūra, vienijanti masyvo adresą ir jo ilgį
    int* theArray;
    int width;
    int height;
};


template<class arrayBounds>
void randomizeArray(
                    ARRAY_META *myArr,
                    arrayBounds minArrayWidth,
                    arrayBounds maxArrayWidth,
                    arrayBounds minArrayHeight,
                    arrayBounds maxArrayHeight,
                    arrayBounds minArrayValue,
                    arrayBounds maxArrayValue
                    ){
// Funkcija į masyvą įrašo atsitiktines reikšmes

    srand(time(0));
    if(myArr->width==0 || myArr->height==0){ // negali kuris nors matmuo būti nulis
        myArr->width = minArrayWidth + rand()%(maxArrayWidth - minArrayWidth);
        myArr->height = minArrayHeight + rand()%(maxArrayHeight - minArrayHeight);
    }


    myArr->theArray=(int*)realloc(myArr->theArray, sizeof(int) * myArr->width * myArr->height);
    cout << "Matricos dydis:\t" << myArr->width << "x" << myArr->height <<endl;
    cout << "Matrica:\n";

    for(int i=0; i<myArr->width * myArr->height; i++){
        myArr->theArray[i] = minArrayValue + rand() % (maxArrayValue - minArrayValue);
        if(i % myArr->width == 0) cout << endl;
        if(myArr->theArray[i]>=0) cout << " "; // kompensuojame minuso ženklą
        if(myArr->theArray[i] < 10 && myArr->theArray[i] > -10) cout << " ";

        cout << myArr->theArray[i]<< " ";

    }
    cout <<endl;
}


template<class arrayBounds>
void INIT(
            ARRAY_META* myArr,
            arrayBounds minArrayWidth,
            arrayBounds maxArrayWidth,
            arrayBounds minArrayHeight,
            arrayBounds maxArrayHeight,
            arrayBounds minArrayValue,
            arrayBounds maxArrayValue
            ){
// Funkcija, skirta masyvo struktūros
//užpildymui, reikšmių suteikimui.

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

            case 0: // "\n0. Išeiti\n";
                myArr->width = -1; // EXIT
                keepLoop=0;
                break;

            case 1: // "1. Įvesti masyvą ranka\n";
                cout << "Įveskite naujos matricos plotį:\n  > ";
                getline(cin, line);
                myArr->width = atoi(line.c_str());
                cout << "Įveskite naujos matricos aukštį:\n  > ";
                getline(cin, line);
                myArr->height = atoi(line.c_str());

                myArr->theArray = (int*)realloc(myArr->theArray,
                                            sizeof(int)*myArr->width * myArr->height);
                for(int i=0; i<myArr->height; i++){
                    for(int j=0; j<myArr->width; j++){
                        cout << "Įveskite elementą ["<<i<<"]["<<j<<"] > ";
                        cin >> myArr->theArray[i*myArr->height + j];
                    }
                }
                keepLoop=0;
                break;

            case 2: // "2. Naudoti visiškai atsitiktinį masyvą\n";
                randomizeArray(
                               myArr,
                               minArrayWidth,
                               maxArrayWidth,
                               minArrayHeight,
                               maxArrayHeight,
                               minArrayValue,
                               maxArrayValue
                               );
                keepLoop=0;
                break;

            case 3: // "3. Naudoti fiksuoto dydžio atsitiktinių reikšmių masyvą\n";
                cout << "Įveskite naujos matricos plotį:\n  > ";
                getline(cin, line);
                myArr->width = atoi(line.c_str());
                cout << "Įveskite naujos matricos aukštį:\n  > ";
                getline(cin, line);
                myArr->height = atoi(line.c_str());
                randomizeArray(
                               myArr,
                               minArrayWidth,
                               maxArrayWidth,
                               minArrayHeight,
                               maxArrayHeight,
                               minArrayValue,
                               maxArrayValue
                               );
                keepLoop=0;
                break;
            default:
                break;
        }
    }
}
