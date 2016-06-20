#include <iostream>
#include <fstream>
#include <string.h>
#include <limits.h>

/*
1. Parašykite programą, kuri suranda maksimalią (minimalią) reikšmę faile.
*/

#define failovardas "1-ilgasfailas"

using namespace std;

int main(){
    string line;
    int ilgis = 0, maxval=0,minval=INT_MAX, tempval=0, value=0;

    ifstream inpfile (failovardas);

    if (inpfile.is_open()){
        while (inpfile.good()){

            getline(inpfile, line);
            ilgis=line.length();
            char *eilute = new char[ilgis];
            strncpy(eilute, line.c_str(), ilgis);

            //cout << "\n"<<line << endl;
            for(int i=0; i<ilgis; i++){

                if(eilute[i] >= '0' && eilute[i] <= '9'){
                    value=(int)eilute[i]-48;
                    //cout << eilute[i]<<endl;
                    //cout << "tempval" <<"="<<tempval<<"*10+"<<value<<"\n";
                    tempval=tempval*10+value;
                    //cout << tempval<<"\n";
                } else {
                    if(tempval > maxval) maxval=tempval;
                    if(tempval < minval && tempval != 0) minval=tempval;
                    tempval=0;
                }
            }
            delete eilute;
        }

        inpfile.close();
    } else {
            cerr << "Negaliu atverti failo: "<<failovardas<<endl;
            return 3;
    }

    if (minval == INT_MAX){
        cout << "Neradau skaičių"<<endl;
        return 1;
    }

    cout << "Mažiausias skaičius:\t"<<minval<<"\n";
    cout << "Didžiausias skaičius:\t"<<maxval<<"\n";
	return 0;
}

