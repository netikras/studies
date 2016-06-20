#include <iostream>
#include <fstream>
#include <string.h>
#include <cstdlib>

/*
7. Parašykite programą, kuri nustatys ir išves į ekraną
    tarpusavyje paprastų (pirminių) skaičių porų kiekį.
    Skaičius A imamas iš failo input, o skaičius B – iš
    output.
*/

using namespace std;

#define inpf "input"
#define outpf "output"


template<class T>
int notOpen(T file){
    cerr << "Could not open file: "<<file<<endl;
    return 3;
}

template<class T>
void gotoBOF(T& file){
    file.clear();
    file.seekg(0,ios::beg);
}

template<class T>
int linesCount(T& file){
    int cnt=0, curr=0; string eil;
    gotoBOF(file);
    while(file.good()){
        getline(file, eil);
        cnt++;
    }
    gotoBOF(file);

    return cnt;
}

int isPrimary(int n){
    n=abs(n);
    if(n<3) return 0;
    for(int i=2; i<n; i++)
        if(!(n%i)) return 0;
    return 1;
}


int positionWise(ifstream& file1, ifstream& file2){
    if(file1.is_open()){
        if(file2.is_open()){
            cout << "\n\t\033[34mĮjungtas tikrinimas atitinkamose eilutėse\033[0m\n\n";

            string line1="", line2="";
            int cnt=0, ilgis1=0, ilgis2=0, sk1=0, sk2=0;

            while(file1.good() && file2.good()){
                getline(file1,line1);
                getline(file2,line2);

                //cout << sk1 << " " << sk2 <<endl;

                sk1=atoi(line1.c_str());
                sk2=atoi(line2.c_str());

                if(isPrimary(abs(sk1)) && isPrimary(abs(sk2)) && sk1==sk2) cnt++;

            }
            cout <<"\nViso:\t" << cnt<<endl;

            file1.close();
            file2.close();
            return 0;
        } else return notOpen("file2");
    } else return notOpen("file1");
}


int main()
{
    cout << "Sąlygoje nenurodyta, kaip suformatuotas failas, \n\
tad laikoma, kad failas sudarytas TIK iš skaičių, \n\
o atskiri skaičiai saugomi atskirose eilutėse\n\n" << endl;

    string line="";
    int eilSk1=0, cnt=0, cnt2=0, total=0;
    int eilSk2=0;

    ifstream inpfile(inpf);
    ifstream outpfile(outpf);

    return positionWise(inpfile, outpfile);
    cout << "\n\t\033[34mĮjungtas tikrinimas neatsižvelgiant į eilutės numerius\033[0m\n\n";

    if(inpfile.is_open()){
        if(outpfile.is_open()){
            eilSk1=linesCount(inpfile);
            eilSk2=linesCount(outpfile);

            int *prims1 = new int[eilSk1];
            int *prims2 = new int[eilSk2];
            int num=0;

            gotoBOF(inpfile);
            gotoBOF(outpfile);

            while(inpfile.good()){
                getline(inpfile,line);
                if(line != "") num=atoi(line.c_str());
                //cout << num<<endl;
                if(isPrimary(num)) prims1[cnt++]=num;
            }
            num=0;
            while(outpfile.good()){
                getline(outpfile,line);
                if(line != "") {
                    num=atoi(line.c_str());
                    //cout << num<<endl;
                    if(isPrimary(num)) prims2[cnt2++]=num;
                }
            }

            cout << "Sutampantys pirminiai skaičiai:\n";
            for(int i=0; i<cnt; i++){
                for(int j=0; j<cnt2; j++){
                    if(prims1[i]==prims2[j]) {
                        cout << prims1[i]<<endl;
                        total++;
                    }
                }
            }
            cout << "\nViso:\t"<<total<<endl;

            delete prims1;
            delete prims2;
        } else return notOpen(outpf);
    } else return notOpen(inpf);

    return 0;
}
