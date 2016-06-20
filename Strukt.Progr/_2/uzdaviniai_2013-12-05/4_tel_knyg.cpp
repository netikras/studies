#include <iostream>

/*
4. Aprašykite struktūrą, kurią galima naudoti kaip telefono žinyną
    (vardas pavardė, adresas, telefonas).
Sukurkite struktūros egzempliorių ir užpildykite jį duomenimis.
*/

using namespace std;

struct tel_kn {

    char vardas[20];
    char pavarde[20];
    char adresas[20];
    char telefonas[20];
};



tel_kn arr[50];



int main(){

    bool a=true;

    struct tel_kn adresai[50];


    char x[1]={ 't' };
    int count=0;

        while(++count<50){
            x[0]='t';

            cout << "Norite sukurti naują adresatą? [t/n] Telefonų knygoje įrašų yra: "<<count-1<<endl;

            cin >> x[0];
            if(x[0] == 'n') break;

            cout << "Įveskite adresato "<<count<<" vardą\n";

            cin >> adresai[count-1].vardas;

            cout << "Įveskite adresato "<<count<<" pavardę\n";

            cin >> adresai[count-1].pavarde;

            cout << "Įveskite adresato "<<count<<" adresą\n";

            cin >> adresai[count-1].adresas;

            cout << "Įveskite adresato "<<count<<" telefoną\n";

            cin >> adresai[count-1].telefonas;
        }

cout << "\nTelefonų knygoje sukurta įrašų: "<<count-1<<"\n\nNorite peržiūrėti?\n\t[t/n]> ";
cin>>x[0];
if(x[0] == 't') {
    for(int i=0; i<count-1; i++){
        cout <<"Vardas:  \t"<< adresai[i].vardas << "\n";
        cout <<"Pavardė:\t"<< adresai[i].pavarde << "\n";
        cout <<"Adresas:\t"<< adresai[i].adresas << "\n";
        cout <<"Telefonas:\t"<< adresai[i].telefonas << "\n--------------\n\n";

    }
}


    return 0;
}
