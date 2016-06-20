#include <iostream>
#include <fstream>
#include <string.h>

/*
2. Duotas failas input, kurio elementai yra išdėstyti stulpeliu.
    Parašykite programą, kuri surūšiuos failo elementus didėjančiai
    ir išves išrūšiuotą variantą į failą output.
*/

#define inpf "input"
#define outpf "output"

using namespace std;

int main()
{

    string line;
    int flength=-1, cnt=0;
    char elem = ' ';

    ifstream inpfile (inpf);
    ofstream outpfile (outpf);

    if (inpfile.is_open()){
        while (inpfile.good()){
            flength++;
            getline(inpfile,line);
        }
        line="";
        flength--;

        cout << "Total lines: "<<flength<<endl;

        char *values = new char[flength];

        inpfile.clear(); // remove EOF flag
        inpfile.seekg(0,ios::beg); //return to beginning of the file

        while (inpfile.good()){
            getline(inpfile,line);
            strncpy(&elem, line.c_str(), 1);
            values[cnt++]=elem;
            //cout <<elem<<endl;
        }

//Burbuliatorius
        for(int i=0; i<flength; i++){
            for(int j=0; j<flength; j++){
                if(values[j]>values[j+1]){
                    elem=values[j];
                    values[j]=values[j+1];
                    values[j+1]=elem;
                }
            }
        }


    if (outpfile.is_open()){
        for(int i=0; i<flength; i++){
           //outpfile << values[i]<<"\n";
           elem=values[i];
           outpfile<<elem<<endl;
           cout << elem<<endl;
        }
        outpfile.close();
    } else {
        cerr << "Negaliu atverti failo: "<<outpf<<endl;
        inpfile.close();

        return 3;
    }
    inpfile.close();
    } else {
        cerr << "Negaliu atverti failo: "<<inpf<<endl;
        return 3;
    }


    return 0;
}
