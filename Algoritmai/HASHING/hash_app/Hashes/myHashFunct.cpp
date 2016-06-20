#include <iostream>
#include <sstream>
#include <string.h>
using namespace std;
stringstream buffer;

string MyHashFunction(string key){
   buffer.str("");
    for(int i=0; i<key.length(); i++)
       buffer << (int)key.c_str()[i];
    return buffer.str();
}

int main() {
    cout << MyHashFunction("HELLO world!")<<endl;
    return 0;
}

