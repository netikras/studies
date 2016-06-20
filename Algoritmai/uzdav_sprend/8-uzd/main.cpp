#include <iostream>
#include <fstream>
#include <string.h>

#define ifile "input"

/*
8. Parašykite programą, kuri nustatys matricos MxN pagrindinėje
    įstrižainėje esančių elementų sumą. Matrica yra faile.
*/


using namespace std;

int main()
{
    string line="";
    int ilgis=0, cnt=0, n=0, index=0, suma=0;

    ifstream file(ifile);
    if(file.is_open()){
                cnt=0;
            getline(file, line);
            ilgis=line.length();
            char *eilute = new char[ilgis];
            strncpy(eilute, line.c_str(), ilgis);

            for(int i=0; i<ilgis; i++){
                if(eilute[i]==' ') cnt++;
            }
            ++cnt;
            delete eilute;

        file.clear(); // remove EOF flag
        file.seekg(0,ios::beg); //return to beginning of the file
        while(file.good()){
            for(int i=0; i<cnt; i++){
                for(int j=0; j<cnt; j++){
                    file>>n;

                    if(i==index && j==index){
                            //cout << n << " ";
                        index++;
                        suma+=n;
                    }
                }
            }
        }
        cout << suma<<endl;

    } else {
        cerr << "Negaliu atverti failo "<<ifile<<"skaitymui\n";
        return 3;
    }

file.close();
    return 0;
}
