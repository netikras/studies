#include <iostream>
#include <fstream>
#include <string.h>

/*
9. Dominuojančiu elementu faile vadinsime elementą,
    kuris jame sutinkamas daugiau negu N/2 kartų.
    Parašykite programą, kuri nustatys ir išves į
    ekraną visus esančius faile dominuojančius
    elementus.
*/

using namespace std;

#define inpf "input"


template<class T>
int notOpen(T file){
    cerr << "Could not open file: "<<file<<endl;
    return 3;
}

template<class T>
void gotoBOF(T& file){
    file.clear();
    file.seekg(0,ios::beg);
}

template<class T>
int linesCount(T& file){
    int cnt=0, curr=0; string eil;
    gotoBOF(file);
    while(file.good()){
        getline(file, eil);
        cnt++;
    }
    gotoBOF(file);

    return cnt;
}


int main()
{
    string line="";
    int ilgis=0,cnt=0,chr=0, offset=32,total=0;
    //94
    ifstream inpfile(inpf);
    int elementai[94];

    for(int i=0; i<94; i++)
        elementai[i]=0;

    if(inpfile.is_open()){
        while(inpfile.good()){
            getline(inpfile, line);
            if(line=="") continue;
            ilgis=line.length();

            char *str = new char[ilgis];
            strncpy(str,line.c_str(),ilgis);

            for(int i=0; i<ilgis; i++){
                elementai[(int)str[i]-offset]++;
//                cout << str[i]<<" "<<elementai[(int)str[i]-offset]<<endl;
                total++;
            }

            ilgis=0;



            delete str;
            for(int i=0; i<94; i++){
                if(elementai[i]>total/2){
                    cout << (char)(i+offset)<<endl;
                    ilgis++;
                }
            }
            //cout << total <<" "<<total/2<<endl;

            if(ilgis==0) cout << "\n\t\033[91mDominuojančio simbolio nėra\033[0m\n\n";

        }
        inpfile.close();
    } else notOpen(inpf);
    return 0;
}
