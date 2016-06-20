#include <iostream>

//1. Iš visų skaitmenų nuo 1 iki 9 sudarykite tris triženklius skaičius, kurių sandauga būtų a) mažiausia b) didžiausia,

using namespace std;

void makeArrayNumbers();
void threeNumbers();

int min_sk1=0, min_sk2=0, min_sk3=0;
int max_sk1=0, max_sk2=0, max_sk3=0;
int max_result=0, min_result=999*999*999;

int num=0;
int arrLength=-1;
int allNums[1000];

int main(){


    makeArrayNumbers();
    threeNumbers();

    //cout << "3rd number is " << allNums[2] <<"\n\tLast entry is "<<arrLength<<": "<<allNums[arrLength]<<"\n";
    cout << "\nMin:\t" << min_sk1<<"*"<<min_sk2<<"*"<<min_sk3<<"="<<min_result<<"\n";
    cout << "\nMax:\t" << max_sk1<<"*"<<max_sk2<<"*"<<max_sk3<<"="<<max_result<<"\n";
return 0;
}



void makeArrayNumbers(){
    for(int a=1; a<10; a++){
        for(int b=1; b<10; b++){
            for(int c=1; c<10; c++){
              num=a*100+b*10+c;
              arrLength++;
              allNums[arrLength]=num;
            }
        }
    }
}




void threeNumbers(){
    for(int i=0; i<=arrLength; i++){
        for(int j=i+1; j<=arrLength; j++){
            for(int k=j+1; k<=arrLength; k++){
                if(min_result > allNums[i]*allNums[j]*allNums[k]) {
                    min_sk1=allNums[i];
                    min_sk2=allNums[j];
                    min_sk3=allNums[k];
                    min_result=allNums[i]*allNums[j]*allNums[k];
                }

                if(max_result < allNums[i]*allNums[j]*allNums[k]) {
                    max_sk1=allNums[i];
                    max_sk2=allNums[j];
                    max_sk3=allNums[k];
                    max_result=allNums[i]*allNums[j]*allNums[k];
                }
            }
        }
    }

}


