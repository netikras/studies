#include <iostream>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <fstream>
#include <time.h>
#include <map>
#include "main.h"

using namespace std;

    uint32_t seed;
    string test;
    long long loopCount;
    clock_t timeBefore;
    clock_t timeAfter;
    std::map<string, int> wordCounts;
    bool s2f; // save hashes to file?

void (*void_Fpt)(string, int, uint32_t);

void showAll2(string local_test, uint32_t local_seed){
    cout << "ASCII:\t\t\t"<<local_test<<endl<<endl;
    for(int i=0; i<ALGORITHMS_COUNT; i++){
        cout << ALGORITHMS[i] << ":\t\t" << (*str_Fpt[i])(local_test, local_test.length(), local_seed)<<endl;
    }
}

void showAll(string local_test, uint32_t local_seed){
    uint32_t hash86[4];
    uint64_t hash64[4];
    cout << "ASCII:\t\t\t" << local_test << endl;
    cout << endl;
    cout << "CityHash32:\t\t" << CityHash32(local_test.c_str(), local_test.length()) << endl;
    cout << "CityHash64:\t\t" << CityHash64(local_test.c_str(), local_test.length()) << endl;
    /*
    cout << "City128:\t\t" << CityHash128(test.c_str(), test.length()) << endl;
    // no idea how to cout << std::pair<>
    */
    cout << "lookup3:\t\t" << lookup3(local_test.c_str(), local_test.length(),local_seed)<<endl;
    cout << "SuperFastHash:\t\t" << SuperFastHash(local_test.c_str(), local_test.length(), local_seed)<<endl;
        MurmurHash3_x86_32(local_test.c_str(), local_test.length(),local_seed, hash86);
    cout << "MurmurHash3_x86_32:\t\t" << hash86[0]<<endl;
        MurmurHash3_x86_128(local_test.c_str(), local_test.length(),local_seed, hash86);
    cout << "MurmurHash3_x86_128:\t\t" << hash86[0]<<hash86[1]<<hash86[2]<<hash86[3]<<endl;
        MurmurHash3_x64_128(local_test.c_str(), local_test.length(),42, hash64);
    cout << "MurmurHash3_x64_128:\t\t" << hash64[0]<<hash64[1]<<hash64[2]<<hash64[3]<<endl;

}

void showSpeed(string local_test, uint32_t local_seed, long long cycles){
    cout << "Kiekvienas algoritmas bus leidžiamas  "<< cycles << " kartų."<<endl;
    cout << "Šis testas parodyt, kiek greiti yra algoritmai"<<endl;
    cout << endl;
    cout << "HASHinamas TEKSTAS: \""<< local_test<<"\""<<endl<<endl;
    cout << endl<<endl;

    { //CityHash32
    cout << "CityHash32\t\t\t";
            timeBefore = clock();
            for(long long j=0; j<cycles; j++)
             CityHash32(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }

    { //CityHash64
    cout << "CityHash64\t\t\t";
            timeBefore = clock();
            for(long long j=0; j<cycles; j++)
             CityHash64(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }

    { //lookup3
    cout << "CityHash32\t\t\t";
            timeBefore = clock();
            for(long long j=0; j<cycles; j++)
             lookup3(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }

    { //SuperFastHash
    cout << "SuperFastHash\t\t\t";
            timeBefore = clock();
            for(long long j=0; j<cycles; j++)
             SuperFastHash(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }

    { //MurmurHash3_x86_32
    cout << "MurmurHash3_x86_32\t\t\t";
            timeBefore = clock();
            for(long long j=0; j<cycles; j++)
             MurmurHash3_x86_32(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }

    { //MurmurHash3_x86_128
    cout << "MurmurHash3_x86_128\t\t\t";
            timeBefore = clock();
            for(long long j=0; j<cycles; j++)
             MurmurHash3_x86_128(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }

    { //MurmurHash3_x64_128
    cout << "MurmurHash3_x64_128\t\t\t";
            timeBefore = clock();
            for(long long j=0; j<cycles; j++)
             MurmurHash3_x64_128(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }


}

void showSpeed2(string local_test, uint32_t local_seed, long long cycles){
    cout << "Kiekvienas algoritmas bus leidžiamas  "<< cycles << " kartų."<<endl;
    cout << "Šis testas parodyt, kiek greiti yra algoritmai"<<endl;
    cout << endl;
    cout << "HASHinamas TEKSTAS: \""<< local_test<<"\""<<endl<<endl;
    cout << endl<<endl;
    for(int i=0; i<ALGORITHMS_COUNT; i++){
            timeBefore = clock();
        cout << ALGORITHMS[i] << ":\t\t\t";
            for(long long j=0; j<cycles; j++)
             (*str_Fpt[i])(local_test, local_test.length(), local_seed);
             timeAfter = clock();
        cout << (double)((timeAfter - timeBefore)/CLOCKS_PER_SEC)<<"."<<(double)((timeAfter - timeBefore)%CLOCKS_PER_SEC) << "s"<<endl;
    }
}

double countCollisions(std::map<string, int> mymap){
    double retVal=0;
     map<string, int>::const_iterator iter;
     for (iter=mymap.begin(); iter != mymap.end(); ++iter)
        if(iter->second > 1) retVal++;
    return retVal;
}

//http://www.cplusplus.com/forum/general/49980/
void dict2(uint32_t local_seed){
    // this implementation saves FileDescriptors though increases I/Os and RAM usage\
    // this algorithm is meant to be used with limited FDs/vast variety of algorithms and small dictionaries
    ifstream inpf(dictFile.c_str());
    string str;
    string str2;
    double fileLinesCount = linesCount(inpf);

cout << "Testo metu bus užkoduota kiekviena eilutė, esanti faile "
<< dictFile
<< ". "
<< endl
<< "Žodyno dydis : "
<< linesCount(inpf)
<< " žodžių (po žodį eilutėje)."
<<endl;
if(s2f) cout << "Užkoduotos eilutės bus išsaugotos atitinkamuose failuose: 'outp/<HashName>'";
cout <<endl<<endl;

    ofstream outf[ALGORITHMS_COUNT];
    for(int i=0; i<ALGORITHMS_COUNT; i++){

        strBuff << "outp"<<pathsep<<ALGORITHMS[i];
        outf[i].open(strBuff.str().c_str());
        strBuff.str("");



        if(inpf.is_open() && outf[i].is_open()){
                cout << "Atliekamas žodyno testas su algoritmu " << ALGORITHMS[i]<<"..."<<endl;
            while(inpf.good()){
                getline(inpf, str);
                    str2 = (*str_Fpt[i])(str, str.length(), local_seed);
                    if(s2f) outf[i] << str2<<endl;
                    wordCounts[str2]++;
            }
            //for(int i=0; i<ALGORITHMS_COUNT; i++)
                outf[i].close();
            cout << "Kolizijų: "<< countCollisions(wordCounts)<<endl;
            wordCounts.clear();

        } else cout << "Nepavyko atverti failo"<<endl;
        gotoBOF(inpf);

    }
    inpf.close();


}

void dict(uint32_t local_seed){ // this implementation has several times lower IOs demand but needs more FDs reserved
    ifstream inpf(dictFile.c_str());

    ofstream outf[ALGORITHMS_COUNT];
    for(int i=0; i<ALGORITHMS_COUNT; i++){
        strBuff << "outp"<<pathsep<<ALGORITHMS[i];
        outf[i].open(strBuff.str().c_str());
        strBuff.str("");
    }
    string str;

    if(inpf.is_open() && outf[0].is_open()){
        while(inpf.good()){
            getline(inpf, str);
            for(int i=0; i<ALGORITHMS_COUNT; i++)
                outf[i] << (*str_Fpt[i])(str, str.length(), local_seed)<<endl;
        }
        for(int i=0; i<ALGORITHMS_COUNT; i++)
            outf[i].close();
        inpf.close();

    } else cout << "Nepavyko atverti failo"<<endl;
}

void settings(){
    cout << LINE<<endl;
    cin.clear();
    string pick;
    string pick2;
    int num;
    cout << "Dabartiniai nustatymai:\n";
    cout << "1.\tCiklų skaičius:\t\t"<<loopCount<<endl;
    cout << "2.\tseed:\t\t\t"<<seed<<endl;
    cout << "3.\tHASHuojama eilutė:\t"<<test<<endl;
    cout << "4.\tSaugoti hashus į failą:\t"<<s2f<<endl;
    cout << "Pasirinkite ką norite keisti (0 - grįžti)\n  > ";
    getline(cin, pick);
    cout << LINE<<endl;
    if(pick == "0") return;
    switch(atoi(pick.c_str())){
        case 0: //wrong value
            settings();
            break;
        case 1:
            cout << "Įveskite naują 'loopCount' reikšmę:  > ";
            getline(cin, pick2);
            remove(pick2.begin(), pick2.end(), ' ');
            if(atoi(pick2.c_str()) > 0) loopCount=atoi(pick2.c_str());
            else cout << "Netinkamas skaičius\n";
            settings();
            break;
        case 2:
            cout << "Įveskite naują 'seed' reikšmę:  > ";
            getline(cin, pick2);
            remove(pick2.begin(), pick2.end(), ' ');
            if(atoi(pick2.c_str()) > 0) seed=(uint32_t)atoi(pick2.c_str());
            settings();
            break;
        case 3:
            cout << "Įveskite naują koduotiną eilutę:  > ";
            getline(cin, pick2);
            //remove(pick2.begin(), pick2.end(), ' ');
            if(pick2 != "") test=pick2;
            else cout <<"Tuščia eilutė netinka\n";
            settings();
            break;
        case 4:
            cout << "Įveskite 'true' arba 'false':  > ";
            getline(cin, pick2);
            if(pick2 == "true") s2f=true;
            else
            if(pick2 == "false") s2f=false;
            else cout <<"Netinkama reikšmė\n";
            settings();
            break;
        default:
            settings();
            break;
    }
    cout << LINE<<endl;
}


int MENU(){
    string pick;
    string TEXT="\
1. Nustatymai\n\
2(22). Parodyti HASH'us\n\
3(33). Greičio testas\n\
4(44). Žodyno testas (kolizijos)\n\
-----------------\n\
0. Išeiti";
cin.clear();
//cls();
    cout << LINE<<endl;
    cout << TEXT<<endl<<"\n > ";
    getline(cin, pick);
    cout << LINE<<endl;
    if(pick == "0") return -1;
    return atoi(pick.c_str());

}

int main() {
    checkIntegrity();

// initial values
    seed      =     42;
    test      =     "Hello world!";
    loopCount =     999999;
    s2f       =     false;

    int loopIt=1;
    while(loopIt){
            cout<<endl<<endl;
        switch(MENU()){
            case -1:
                loopIt=0;
                break;
            case 0: //wrong input
                continue;
            case 1: //settings
                settings();
                continue;
            case 2: // show examples
                showAll2(test, seed);
                continue;
            case 22: // show examples (simple)
                showAll(test, seed);
                continue;
            case 3: // show speeds
                showSpeed2(test, seed, loopCount);
                continue;
            case 33: // show speeds
                showSpeed(test, seed, loopCount);
                continue;
            case 4:
                dict2(seed);
                continue;
            case 44:
                dict(seed);
                continue;
            default:
                continue; //continue
        }

    }

    return 0;
}
