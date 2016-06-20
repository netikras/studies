#include <iostream>
#include <cstdlib>
#include <time.h>

/*
    6.	Duotas natūrinis skaičius n ir realūs
    skaičiai x1, ..., xn (n≥2). Rasti seką
    x1-xn, x2-xn, ..., xn-1-xn.
    Uždavinį išspręsti panaudojant sąrašą.
*/

using namespace std;

struct ARRAY_META{
    double *theArray;
    int length;
};



void randomizeArray(
            ARRAY_META* myArr,
            int minArrayLen,
            int maxArrayLen,
            double minArrayValue,
            double maxArrayValue
            ){
// Funkcija į masyvą įrašo atsitiktines reikšmes

    srand(time(0));
    if(myArr->length==0) myArr->length = minArrayLen + rand()%(maxArrayLen - minArrayLen);

    myArr->theArray=(double*)realloc(myArr->theArray, sizeof(double) * myArr->length);
    cout << "Elementų masyve:\t" << myArr->length <<endl;
    cout << "Masyvo reikšmės:\t";
    for(int i=0; i<myArr->length; i++){
        myArr->theArray[i] =
            minArrayValue
            +
             (((double)rand()) / (double)RAND_MAX) * (maxArrayValue - minArrayValue)
            ;

        cout << myArr->theArray[i]<<" ";
    }

}


int INIT(
            ARRAY_META* myArr,
            int minArrayLen,
            int maxArrayLen,
            double minArrayValue,
            double maxArrayValue
            ){

    int keepLoop = 1;
    string line;
    while(keepLoop){ // Kai kintamojo reikšmė taps '0', iš ciklo bus išeita
        cout << endl;
        cout << "1. Įvesti seką ranka\n";
        cout << "2. Naudoti visiškai atsitiktinę seką\n";
        cout << "3. Naudoti fiksuoto dydžio atsitiktinių reikšmių seką\n";
        cout << "\n0. Išeiti\n";
        cout << "  > ";
        getline(cin, line);

        switch(atoi(line.c_str())){
            case 0:
                myArr->length = -1; // EXIT
                keepLoop=0;
                break;
            case 1:
                cout << "Įveskite sekos dydį:\n  > ";
                getline(cin, line);
                myArr->length = atoi(line.c_str());
                myArr->theArray = (double*)realloc(myArr->theArray,
                                                sizeof(double)*myArr->length);
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
                cout << "Įveskite sekos dydį:\n  > ";
                getline(cin, line);
                myArr->length = atoi(line.c_str());
                randomizeArray(
                            myArr,
                            minArrayLen,
                            maxArrayLen,
                            minArrayValue,
                            maxArrayValue
                            );
                keepLoop=0;
                break;
            default:
                break;
        }
    }
    return myArr->length;
}


struct LIST{
    double x;
    LIST* next;
};


LIST* addToList(LIST* start, double number){
    LIST *Lnew  = new LIST();

    start->next = Lnew;
    start->x    = number;
    Lnew->next  = NULL;

    return Lnew;
}

void printList(LIST* start){
    LIST* tempList = start;
    while(tempList->next!=NULL){
        cout << tempList->x<<" \n";
        tempList = tempList->next;
    }
}


int main(){

    ARRAY_META *arr = new ARRAY_META();
    LIST *Lstart = new LIST(); // ·->·->·->·->0
    Lstart->next=NULL;
    LIST* Lend = Lstart;

    // Jei masyvas bus generuojamas atsitiktinai, bus naudojami rėžiai:
    int minArrayLen       =   5;     // masyvo ilgio MIN dydis
    int maxArrayLen       =  20;     // masyvo ilgio MAX dydis
    double minArrayValue  =   0;     // masyvo reikšmių MIN dydis
    double maxArrayValue  =  20;     // masyvo reikšmių MAX dydis

    INIT(
         arr,
         minArrayLen,
         maxArrayLen,
         minArrayValue,
         maxArrayValue
         );
    cout << endl;
    for(int i=0; i<arr->length-1; i++){
        Lend=addToList(
                         Lend,
                         arr->theArray[i] - arr->theArray[arr->length-1]
                         );
                         //cout << arr->theArray[i] <<"-"<< arr->theArray[arr->length-1]<<"\t=\t"<<arr->theArray[i] - arr->theArray[arr->length-1]<<endl;
    }
    cout << endl<<"\t[x(0)..x(n-1)]-xn:"<<endl;
    printList(Lstart);
    cout << endl;

	return 0;
}
