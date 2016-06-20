#include <iostream>
#include <fstream>
#include <string.h>

/*
3. Parašykite programą, kuri sukuria naują failą output,
    kuriame bus visi pirminiai skaičiai, surasti faile input.
*/

#define inpf "input"
#define outpf "output"

using namespace std;

bool isPrimary (int n);

int main()
{
    string line;
    int ilgis=0, value=0,tempval=0;

    ofstream outpfile (outpf);
    ifstream inpfile (inpf);

    if (inpfile.is_open() && outpfile.is_open()){
        while (inpfile.good()){

         getline(inpfile, line);
            ilgis=line.length();
            char *eilute = new char[ilgis];
            strncpy(eilute, line.c_str(), ilgis);
            //eilute[ilgis]=' ';

            for(int i=0; i<ilgis; i++){
                value=(int)eilute[i]-48;
                cout << value<<" ";
                if(value >= 0 && value <= 9){
                        cout << endl;
                    tempval=tempval*10+value;
                    if(isPrimary(tempval) && (i==ilgis-1)) outpfile << tempval << endl;
                } else {
                    if(isPrimary(tempval)) outpfile << tempval << endl;
                    tempval=0;
                }
            }
        delete eilute;

        }

    } else {
            cerr << "Negaliu atverti failo: "<<endl;
            return 3;
    }
    inpfile.close();
    outpfile.close();
    return 0;
}

bool isPrimary (int n){
    bool ret=true;
    if (n==0||n==1||n==2) ret=false;
        for (int i=3; i<n; i++)
            if(!(n%i))
                ret=false;

        return ret;
}
