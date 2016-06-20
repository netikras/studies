#include <stdint.h>
#include <sstream>
#include <string.h>
#include <iostream>
#include <fstream>

#ifdef _WIN32
const std::string pathsep("\\");
#include "algorithms\\CityHash.h"
#include "algorithms\\lookup3.h"
#include "algorithms\\MurMur3.h"
#include "algorithms\\SuperFastHash.h"
#else
const std::string pathsep("/");
#include "algorithms/CityHash.h"
#include "algorithms/lookup3.h"
#include "algorithms/MurMur3.h"
#include "algorithms/SuperFastHash.h"
#endif

void die(std::string reason, int exitcode=0){
	std::cerr << reason <<"\n\r";
	exit(exitcode);
}

void cls(){std::cout << "\033c";}

std::string itos(int number){
    std::stringstream ss;
    ss << number;
    std::string str;
    ss >> str;
    return str;
}

std::string uint32ToString(uint32_t number){
    std::stringstream ss;
    ss << number;
    std::string str;
    ss >> str;
    return str;
}

std::string uint64ToString(uint64_t number){
    std::stringstream ss;
    ss << number;
    std::string str;
    ss >> str;
    return str;
}

void gotoBOF(std::ifstream &file){
    file.clear();
    file.seekg(0,std::ios::beg);
}

double linesCount(std::ifstream &file){
    double cnt=0; std::string eil;
    gotoBOF(file);
    while(file.good()){
        getline(file, eil);
        cnt++;
    }
    gotoBOF(file);
    return --cnt;
}

std::stringstream strBuff;
std::string LINE="–––––––––––––––––––––––––––––––––––––––––––––––";
std::string dictFile = "DICT_FILE";

//---------------------------------------------------------\\
//---------------------------------------------------------\\
//---------------------------------------------------------\\


std::string MurmurHash3_x86_32(std::string what, int length, uint32_t seeding){
    uint32_t hsh[4];
    MurmurHash3_x86_32(what.c_str(), length,seeding, hsh);
    return uint32ToString(hsh[0]);
}

std::string MurmurHash3_x86_128(std::string what, int length, uint32_t seeding){
    uint32_t hsh[4];
    std::string retVal;
    MurmurHash3_x86_128(what.c_str(), length,seeding, hsh);
    strBuff.str("");strBuff << uint32ToString(hsh[0]) << uint32ToString(hsh[1]) << uint32ToString(hsh[2]) << uint32ToString(hsh[3]);
    //return uint32ToString(hsh[0]) + uint32ToString(hsh[1]) + uint32ToString(hsh[2]) + uint32ToString(hsh[3]);
    retVal=strBuff.str();
    strBuff.str("");
    return retVal;
}

std::string MurmurHash3_x64_128(std::string what, int length, uint32_t seeding){
    uint64_t hsh[4];
    std::string retVal;
    MurmurHash3_x64_128(what.c_str(), length,seeding, hsh);
    strBuff.str("");strBuff << uint64ToString(hsh[0]) << uint64ToString(hsh[1]) << uint64ToString(hsh[2]) << uint64ToString(hsh[3]);
    //return uint64ToString(hsh[0]) + uint64ToString(hsh[1]) + uint64ToString(hsh[2]) + uint64ToString(hsh[3]);
    retVal=strBuff.str();
    strBuff.str("");
    return retVal;
}



std::string CityHash32(std::string what, int length, uint32_t seeding){
    return uint32ToString(CityHash32(what.c_str(), length));
}

std::string CityHash64(std::string what, int length, uint32_t seeding){
    return uint64ToString(CityHash64(what.c_str(), length));
}

std::string lookup3(std::string what, int length, uint32_t seeding){
    return uint32ToString(lookup3(what.c_str(), length,seeding));
}

std::string SuperFastHash(std::string what, int length, uint32_t seeding){
    return uint32ToString(SuperFastHash(what.c_str(), length,seeding));
}

//---------------------------------------------------------\\
//---------------------------------------------------------\\
//---------------------------------------------------------\\


std::string (*str_Fpt[])(std::string what, int length, uint32_t seeding) = {
      CityHash32
    , CityHash64
    , lookup3
    , SuperFastHash
    , MurmurHash3_x86_32
    , MurmurHash3_x86_128
    , MurmurHash3_x64_128
};

const char* ALGORITHMS[] = {
          "CityHash32"
        , "CityHash64"
        , "lookup3"
        , "SuperFastHash"
        , "MurmurHash3_x86_32"
        , "MurmurHash3_x86_128"
        , "MurmurHash3_x64_128"
        };

int ALGORITHMS_COUNT = sizeof(ALGORITHMS) / sizeof(ALGORITHMS[0]);
int ALG_functions_COUNT = sizeof(str_Fpt) / sizeof(str_Fpt[0]);




int checkIntegrity(){
    if(ALGORITHMS_COUNT != ALG_functions_COUNT)
        die(
"ERROR:\n\
    Check main.h. Count of algorithms names\n\
and actual algorithms functions does not mach.\n\n\
Check arrays:\n\
    std::string ALGORITHMS\n\
and\n\
    std::string *str_Fpt\n"
, 1);
    return 0;
}
