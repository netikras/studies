#include <iostream>
#include <sstream>

using namespace std;

//#define trainCnt 8

stringstream BUFFER;

#define TASK_DESCRIPTION "\
** 3 užd. ********************************************* Darius Juodokas **\n\
** 9. Aprašyti struktūrą vardu TRAIN, sudarytą iš tokių laukų:          **\n\
**       * Paskirties taškas (vietovė);                                 **\n\
**       * Traukinio numeris;                                           **\n\
**       * Išvykimo laikas.                                             **\n\
**    Parašyti programą, kuri atlieka tokius veiskmus:                  **\n\
**       a) Įveda iš klaviatūros duomenis į masyvą, kuris yra sudarytas **\n\
**          iš 8-nių struktūrų TRAIN tipo; įrašai turi būti išrūšiuoti  **\n\
**          pagal traukinio numerį;                                     **\n\
**       b) Išveda į ekraną informaciją apie traukinį, kurio numeris    **\n\
**         įvedamas iš klaviatūros.                                     **\n\
**           !!! Jeigu tokių reisų nėra, išveda atitinkamą pranešimą.   **\n\
**************************************************************************\n\
"

#define TABLE_HEADER "\n\
     ==============================================================\n\
     | Traukinio |                     |_________IŠVYKIMAS________|\n\
     |  Numeris  |       Vyksta į      | MMMM | mm | dd | hh | mm |\n\
     |------------------------------------------------------------|\n\
"
//\
<-5->|<---11---->|<--------21--------->|<-4-->|<2->|<2->|<2->|<2->|

#define TABLE_FOOTER "\
     ==============================================================\n"

const string dests[]={
    "Vilnius",
    "Kaunas",
    "Klaipeda",
    "Palanga",
    "Panevezys",
    "Siauliai",
    "Birzai",
    "Krokuva",
    "Ryga",
    "Talinas",
    "Varsuva",
    "Maskva",
    "Minskas"
};

struct TRAIN {
    string dest;
    int number;
    int leaves[5]; // [0] metai; [1] mėnuo; [2] diena; [3] valanda; [4] minutės;
};

template <class T>
string printCentered(int availablePositions, T whatToPrint){
/*
    Funkcija grąžins suformatuotą eilutę taip,
    kad ji būtų kiek įmanoma centre duotuose rėžiuose.
    T.y. jei turime 13 pozicijų lentelės langelyje,
    kuriose reikia atvaizduoti žodį, funkcija grąžins
    jau suformatuotą eilutę, kurią beliks tik išvesti į
    langelį
*/

    int spacesFront=0;
    int spacesEnd=0;
    int neededSpaces=0;
    int i=0;
    stringstream result;
    result.str("");

    BUFFER.str("");
    BUFFER << whatToPrint;

    while(BUFFER.str()[i++]!=0) neededSpaces++;

    spacesFront=(availablePositions - neededSpaces) / 2;
    if(spacesFront*2+neededSpaces == availablePositions)
        spacesEnd=spacesFront;
    else spacesEnd=spacesFront+1; // Jei nelyginis skaičius, tekstą meskime arčiau kairės pusės

    for(i=0; i<spacesFront; i++)
        result <<" ";
        result << BUFFER.str();
    for(i=0; i<spacesEnd; i++)
        result <<" ";

        return result.str();
}





int countChars(char* line){
/*
    Funkcija grąžins simbolių sekos ilgį
    Aktualu, kai nėra žinomas *masyvo ilgis.
*/
    int cnt=0;
    int ind=0;
    while(line[ind++]!='\0') cnt++;

    return cnt;
}


char* trim(const char* line){
/*
    Funkcija pašalins tuščius tarpus ir tabuliatoriaus
    žymes prieš ir po frazės, tačiau nelies šių simbolių
    teksto viduje.
*/
    int ind=0;
    int realText=0;
    int lock=0; // 0-begining; 1-ending
    char* result=0;

    while(line[ind]!='\0'){

        if((line[ind] == '\t' || line[ind] == ' ') && lock == 0){
          ind++;
          continue;
        }

        result=(char*)realloc(result, sizeof(char)*(++realText));
        result[realText-1]=line[ind];
        lock=1; // stops removing spaces
        ind++;
    }

    ind=countChars(result);

    while(ind>=0 && lock==1){

        if(result[ind-1] == '\t' || result[ind-1] == ' '){
            ind--;
            continue;
        }
        lock--;
    }
    result = (char*)realloc(result, sizeof(char)*(ind+1));
    result[ind] = '\0';

    return result;
}


char** split(const char* line, char delim, int& length){
/*
    Funkcija jai pateiktą simbolių eilutę padalins
    į dalis pagal skyriklį (delimiter).
    Po padalinimo esančių blokų skaičius atsispindės
    parametre 'length'
*/
    char **result=0;
    length=0;
    char* buffer=0;
    int substrLen=0;
    int ind = 0; // index

    int keepLoop=1;

    while(keepLoop){
            //cout << "line["<<ind<<"]=" << line[ind]<<endl;
        if(line[ind] == '\0') break;//keepLoop--;
        if(line[ind] != delim && line[ind] != '\0'){ // formuojamas komponentas
            buffer=(char*)realloc(buffer, sizeof(char)*(++substrLen));
            buffer[substrLen-1]=line[ind];
            //cout << "1\n";
        }
        else { // rastas delimiter'is, vadinasi, komponentas jau suformuotas. Beliko supakuoti
            length++;
            buffer=trim(buffer);

            substrLen=countChars(buffer);
            buffer=(char*)realloc(buffer, sizeof(char)*(++substrLen));
            buffer[substrLen-1] = '\0';

            result=(char**)realloc(result, sizeof(char*)*length+1); // pridedama vietos dar vienam pointeriui ir '\0'
            //result[length-1]=(char*)realloc(result, sizeof(char)*(substrLen)); // pridedama vietos pointeryje
            result[length-1]=buffer; //įrašomas pointeris, numetus tuščius tarpus nuo galų
            result[length]=0;
            buffer=(char*)malloc(sizeof(char)); // buferio adresas jau išsaugotas masyve, todėl mums reikia naujo buferio, o ne perdirbti senąjį

            buffer[0]='\0';
            substrLen=0;
        }
        ind++;
    }
    return result;
}



void INIT(TRAIN *train, int trainCnt, bool manually=false){
/*
    Funkcija užpildys traukinių masyvą reikšmėmis.
    Jei nenurodytas 'manually' parametras, arba
    jo reikšmė yra false - reikšmės bus priskirtos
    automatiškai
*/
    string line;
    char** splitValues=0;
    int splValLen=0;


    if(manually){
        cout << TABLE_HEADER;
        for(int i=0; i<trainCnt; i++){
            cout << i<<"/"<<trainCnt-1<<": |";

            getline(cin, line);
            splitValues=split(line.c_str(), '|', splValLen);
            train[i].number = atoi(splitValues[0]);
            train[i].dest = splitValues[1];
                train[i].leaves[0] = atoi(splitValues[2]);
                train[i].leaves[1] = atoi(splitValues[3]);
                train[i].leaves[2] = atoi(splitValues[4]);
                train[i].leaves[3] = atoi(splitValues[5]);
                train[i].leaves[4] = atoi(splitValues[6]);
        }
        cout << TABLE_FOOTER<<endl;
    }
    else {
        for(int i=0; i<trainCnt; i++){
        // Visiems traukiniams bus priskiriamos pradinės reikšmės
            train[i].number = i+1;
                train[i].leaves[0]=2014;           //metai
                train[i].leaves[1]=(i*7+15)%11+1;  //mėnesiai // negali būti 00
                train[i].leaves[2]=(i*5+51)%28+1;  //dienos   // negali būti 00
                train[i].leaves[3]=(i*17+13)%23;   //valandos
                train[i].leaves[4]=(i*19+23)%59;   //minutės
           train[i].dest=dests[i%(sizeof(dests)/sizeof(dests[0]))];
        }
    }
}

