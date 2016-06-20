#include <iostream>
#include <string.h>
#include <sstream>
#include <cstdlib>
/*
    9.	Išnagrinėti, kas yra perkraunamos funkcijos.
    Parašyti programą panaudojant perkraunamas funkcijas,
    kuri atliks sveikųjų, realiųjų ir simbolinių duomenų
    sudėtį. Iškviesti šią funkciją programoje su skirtingu
    parametrų kiekiu.
*/

using namespace std;

stringstream ss;

string add(string a, string b){
    ss.str("");
    string retVal;
    ss<<a<<b;
    retVal=ss.str();
    ss.str("");
    return retVal;
}

string add(string a, string b, string c){
    ss.str("");
    string retVal;
    ss<<a<<b<<c;
    retVal=ss.str();
    ss.str("");
    return retVal;
}

string add(string a, string b, string c, string d){
    ss.str("");
    string retVal;
    ss<<a<<b<<c<<d;
    retVal=ss.str();
    ss.str("");
    return retVal;
}

int add(int a, int b){
    return a+b;
}

int add(int a, int b, int c){
    return a+b+c;
}

int add(int a, int b, int c, int d){
    return a+b+c+d;
}

double add(double a, double b){
    return a+b;
}

double add(double a, double b, double c){
    return a+b+c;
}

double add(double a, double b, double c, double d){
    return a+b+c+d;
}


char* add(char a, char b){

    char* ar=new char[3];
    ar[0]=a;
    ar[1]=b;
    ar[2]='\0';
    return ar;
}

char* add(char a, char b, char c){
    char* ar=new char[4];
    ar[0]=a;
    ar[1]=b;
    ar[2]=c;
    ar[3]='\0';
    return ar;
}

char* add(char a, char b, char c, char d){
    char* ar=new char[5];
    ar[0]=a;
    ar[1]=b;
    ar[2]=c;
    ar[3]=d;
    ar[4]='\0';
    return ar;
}


int main(){
    cout << "string: " << add("labas ", "rytas", " tau", " sakau")<<endl;
    cout << "char:   " << add('x', 'z' )<<endl;
    cout << "char:   " << add('x', 'z', ' ', 'y' )<<endl;
    cout << "int:    " << add(3,4,5)<<endl;
    cout << "double: " << add(33.786, 12.87, 183.55)<<endl;

	return 0;
}
