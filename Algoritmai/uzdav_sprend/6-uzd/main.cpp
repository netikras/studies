#include <iostream>
#include <fstream>
#include <string.h>

#define f1 "Num_1"
#define f2 "Num_2"

/*
6. Parašykite programą, kuri nustatys ir išves į ekraną
visus sutampančius failų Num_1 ir Num_2 elementus.
atkreipti dėmesį į pozicijas
*/

using namespace std;



template<class T>
int notOpen(T file){
    cerr << "Could not open file: "<<file<<endl;
    return 3;
}



int positionWise(ifstream& file1, ifstream& file2){
       if(file1.is_open()){
        if(file2.is_open()){
            cout << "\n\t\033[34mĮjungtas tikrinimas atsižvelgiant į elemento poziciją eilutėje\033[0m\n\n";

            string line1="", line2="";
            int cnt=0, ilgis1=0, ilgis2=0, sk1=0, sk2=0;

            while(file1.good() && file2.good()){
                    cout << "\nEilute #"<<++cnt<<":\t";

                getline(file1,line1);
                getline(file2,line2);

                ilgis1=line1.length();
                ilgis2=line2.length();

                char* lineMas1 = new char[ilgis1];
                char* lineMas2 = new char[ilgis2];

                strncpy(lineMas1, line1.c_str(), ilgis1);
                strncpy(lineMas2, line2.c_str(), ilgis2);

                //cout << sk1 << " " << sk2 <<endl;

                for(int i=0; i<ilgis1; i++){
                    if(lineMas1[i] == lineMas2[i]) cout << lineMas1[i]<<"("<<i+1<<")"<<" ";
                }

                //if(isPrimary(abs(sk1)) && isPrimary(abs(sk2)) && sk1==sk2) cnt++;

                delete lineMas1;
                delete lineMas2;
            }
            //cout << cnt<<endl;

            file1.close();
            file2.close();
            return 0;
        } else return notOpen("file2");
    } else return notOpen("file1");
}

int main()
{

    string line1="", line2="";
    int ilgis1=0, ilgis2=0;

    ifstream file1(f1);
    ifstream file2(f2);

    return positionWise(file1, file2);

    cout << "\n\t\033[34mĮjungtas tikrinimas neatsižvelgiant į elemento poziciją eilutėje\033[0m\n\n";

    if(file1.is_open() && file2.is_open()){

        while(file1.good()){
            getline(file1, line1);
            ilgis1=line1.length();

            char *eilute1 = new char(ilgis1);

            strncpy(eilute1, line1.c_str(), ilgis1);
            for(int i=0; i<ilgis1; i++){
                while(file2.good()){
                    getline(file2, line2);
                    ilgis2=line2.length();

                    char *eilute2 = new char(ilgis2);
                    strncpy(eilute2, line2.c_str(), ilgis2);

                    for(int j=0; j<ilgis2; j++){
                        if(eilute1[i]==eilute2[j]){
                            cout << eilute1[i]<<endl;
                        }
                    }

                    delete eilute2;
                }

                    file2.clear(); // remove EOF flag
                    file2.seekg(0,ios::beg); //return to beginning of the file


            }
            delete eilute1;
        }

    } else {
        cerr << "Negaliu atverti failų skaitymui\n";
        return 3;
    }

file1.close();
file2.close();

    return 0;
}
