#import <iostream>

/*
3. Duotas natūralus skaičius n (n1000).
Atspausdinti ši skaičių žodžiais
(trylika, šimtas penki, du šimtais keturiasdešimt vienas ir t.t ).
*/

using namespace std;

int main(){

    int n=0;

    int tukst=0, simt=0, desimt=0, vnt=0;

    string BUFFER="";

    cout << "Įveskite skaičių ([0; 1000])\n-> ";
    cin >> n;

    vnt=n%10;
    desimt=(n/10)%10;
    simt=(n/100)%10;
    tukst=(n/1000)%10;

//cout<<"Tūkstančiai: "<<tukst << "\nŠimtai: "<<simt<<"\nDešimtys: "<<desimt<<"\nVienetai: "<<vnt<<"\n";



    if(tukst>0){
        if(tukst==1){
            BUFFER=BUFFER+"Vienas Tūkstantis ";
        }else{
            switch(tukst){
                case 2: BUFFER+="Du ";break;
                case 3: BUFFER+="Trys "; break;
                case 4: BUFFER+="Keturi "; break;
                case 5: BUFFER+="Penki "; break;
                case 6: BUFFER+="Šeši "; break;
                case 7: BUFFER+="Septyni "; break;
                case 8: BUFFER+="Aštuoni "; break;
                case 9: BUFFER+="Devyni "; break;
            }
            BUFFER+="Tūkstančiai ";
        }
    }


    if(simt>0){
        if(simt==1){
            BUFFER=BUFFER+"Vienas Šimtas ";
        }else{
            switch(simt){
                case 2: BUFFER+="Du ";break;
                case 3: BUFFER+="Trys "; break;
                case 4: BUFFER+="Keturi "; break;
                case 5: BUFFER+="Penki "; break;
                case 6: BUFFER+="Šeši "; break;
                case 7: BUFFER+="Septyni "; break;
                case 8: BUFFER+="Aštuoni "; break;
                case 9: BUFFER+="Devyni "; break;
            }
            BUFFER+="Šimtai ";
        }
    }

    if(desimt>1){
        switch(desimt){
                case 2: BUFFER+="Dvi";break;
                case 3: BUFFER+="Tris"; break;
                case 4: BUFFER+="Keturias"; break;
                case 5: BUFFER+="Penkias"; break;
                case 6: BUFFER+="Šešias"; break;
                case 7: BUFFER+="Septynias"; break;
                case 8: BUFFER+="Aštuonias"; break;
                case 9: BUFFER+="Devynias"; break;
            }
            BUFFER+="dešimt ";
    }
    if(desimt==1){
        switch(vnt){
                case 1: BUFFER+="Vienuo";break;
                case 2: BUFFER+="Dvy";break;
                case 3: BUFFER+="Try"; break;
                case 4: BUFFER+="Keturio"; break;
                case 5: BUFFER+="Penkio"; break;
                case 6: BUFFER+="Šešio"; break;
                case 7: BUFFER+="Septynio"; break;
                case 8: BUFFER+="Aštuonio"; break;
                case 9: BUFFER+="Devynio"; break;
            }
            (vnt==0?BUFFER+="Dešimt ":BUFFER+="lika");
    } else      switch(vnt){
                case 1: BUFFER+="Vienas";break;
                case 2: BUFFER+="Du";break;
                case 3: BUFFER+="Trys"; break;
                case 4: BUFFER+="Keturi"; break;
                case 5: BUFFER+="Penki"; break;
                case 6: BUFFER+="Šeši"; break;
                case 7: BUFFER+="Septyni"; break;
                case 8: BUFFER+="Aštuoni"; break;
                case 9: BUFFER+="Devyni"; break;
            }

if(tukst==0 && simt==0 && desimt==0 && vnt==0) BUFFER="Nulis";

BUFFER+="\n";
cout << BUFFER;


    return 0;
}
