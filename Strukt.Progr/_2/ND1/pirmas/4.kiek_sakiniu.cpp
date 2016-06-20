#import <iostream>

/*
4. Duotas tekstas. Rasti, kiek  jame yra sakinių.
*/

using namespace std;

int main(){
    bool radau_zenkla=false;
    int ilgis=500;
    char tekstas[ilgis]; for(int i=0; i<ilgis; i++) tekstas[i]=2;
    int sakiniai=0;

    cout << "Įveskite tekstą\n-> ";
    cin.get(tekstas, ilgis);

    for(int i=0; i<ilgis; i++){
        if(tekstas[i]=='!' || tekstas[i]=='.' || tekstas[i]=='?' || tekstas[i]=='\0'){
            radau_zenkla=true;
            //sakiniai++;
        }

        if(((radau_zenkla) && (tekstas[i] > 'A') && (tekstas[i] < 'Z')) || (tekstas[i] == '\0')){
            radau_zenkla=false;
            sakiniai++;
        }

    }


    cout << "Tekste radau sakinių: " << sakiniai;

return 0;
}
