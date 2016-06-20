#include <iostream>
#include <limits.h>
#include <string.h>
#include <sstream>

using namespace std;


string NumberToString ( int Number ) {
     ostringstream ss;
     ss << Number;
     return ss.str();
  }


int fHASH(string key){
    char symbols[key.length()+1]; // including '\0'
    strncpy(symbols, key.c_str(), key.length());
    int hashed=0;

    for(int i=0; i<key.length(); i++){
            hashed ^= (int)symbols[i] * hashed/2 ^ key.length();
    //if(hashed >= 216553)
        hashed %= 216553;
        //cout << (int)symbols[i] << " ";

    }
    //cout<<"\t"<<hashed%100<<endl;
    //cout << key << "\t---> " << hashed <<endl;
    //cout << endl;
    return hashed%216553;
};




int main()
{
    int arr[216553], vh=0, val=7, cols_ct=0;
    string k;
    //while(1){
    for(int i=1; i<=216553; i++){
       // getline(cin, k);
       if(k==".")break;

        //vh=fHASH(k);
        //cout << i << "\t--> ";
        vh=fHASH(NumberToString(i));
        if(arr[vh] == 7)
            //cout << "COLLISION!\n\n";
            cols_ct++;
        else
            arr[vh] = 7;


    }
cout << cols_ct<<endl;
    //cout << "test";
    //cout << INT_MAX<<endl<<INT_MAX*sizeof(int)<<"B = "<< INT_MAX*sizeof(int)/1024<<"KB \n= ";
    //cout << INT_MAX*sizeof(int)/1024/1024<<"MB = " << INT_MAX*sizeof(int)/1024/1024/1024<< "GB";
    return 0;
}


