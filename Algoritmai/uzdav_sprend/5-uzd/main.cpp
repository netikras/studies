#include <iostream>
#include <fstream>
#include <Math.h>

/*
5. Faile input ištisai išdėstyti tam tikri skaitmenys.
Ištisą skaitmenų sankaupą vadinsime skaičiumi.
Parašykite programą, kuri pakels duotą skaičių laipsniu N.
Skaitmenų kiekis skaičiuje ne daugiau negu tūkstantis.
*/

#define ifile "input"

using namespace std;

int main()
{
    ifstream inpfile(ifile);


    int ilgis=0, duomenys[1000];
    string line="";

    if (inpfile.is_open()){
        while (inpfile.good()){
            getline(inpfile, line);
            ilgis=line.length();
        }
    }

    return 0;
}
