#include <iostream>
#include <string.h>
#include <fstream>
using namespace std;

int main(int argc, char* argv[]){
    string line;
    strcat(argv[0],".cpp");
    ifstream inpf(argv[0]);
    while(inpf.good()){
        getline(inpf, line);
        cout<<line<<endl;
    } inpf.close();
    return 0;
}
