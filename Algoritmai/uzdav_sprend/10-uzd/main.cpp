#include <iostream>
#include <fstream>
#include <cstdlib>

/*
10. Failas sudarytas iš sekos N natūralių skaičių.
    Parašykite programą, kuri nustatys ir papildys
    į failą mažiausią natūralų skaičių, kurio nėra
    sekoje.
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
    int ilgis=0, minval=1, maxval=0, cnt=0, eilSk=0;

    ifstream inpfile(inpf);

    if(inpfile.is_open()){
        eilSk=linesCount(inpfile);
        cout << "sąlygoje neapibrėžta, tai laikome, kad seka surašyta stulpeliu\n";
        while(inpfile.good()){  //randame didžiausią skaičių faile
            getline(inpfile, line);
            //cout << line<<endl;
            if(line != "") maxval=atoi(line.c_str());
        }

        gotoBOF(inpfile);
        for(int i=1; i<=maxval+1; i++){  //randame mažiausią skaičių, kurio nėra faile

            getline(inpfile,line);
            //cout << "i="<<i<<" line="<<line<<endl;
            if(atoi(line.c_str()) != i){
                minval=i; inpfile.close(); break;
            }
        }

    cout <<"\nMažiausias natūralus sekoje nesantis skaičius -- "<< minval<<endl;
    ofstream outpfile(inpf,ios::app);
    outpfile << "---\n"<<minval<<endl;
    outpfile.close();

    inpfile.close();
    } else  return notOpen(inpf);

    return 0;
}
