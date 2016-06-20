#include <iostream>
#include <fstream>
#include <string.h>

/*
4. Yra failas input, kuriame yra tam tikros pavardės, pagal vieną eilutę.
Parašykite programą, išvedančią į failą output kiekvieną i-tąją pavardę,
kurios visi simboliai įeina į tam tikrą j-tąją pavardę, t.y. jeigu failas
input yra toks:

Kaunasiene
Kaunas

Tai output faile bus išvesta pavardė Kaunas, kadangi visi simboliai duotos pavardės yra pirmoje pavardėje.
*/

#define inpf "input"
#define outpf "output"

using namespace std;

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


int main()
{
    string line1="",line2="";
    int cnt=0, ilgis=0, eilSk=0;

    ifstream inpfile(inpf);
    ofstream outpfile(outpf);

    if(inpfile.is_open()){
        eilSk=linesCount(inpfile);
        ifstream inpfile2(inpf);

        while(inpfile.good()){
            getline(inpfile,line1);
            for(int i=0; i<eilSk; i++){
                getline(inpfile2, line2);
                if(line2.find(line1)!=string::npos && i!=cnt && line1 != "")
                    //cout <<line1<<" in " <<line2<<endl;
                    outpfile << line1 <<"\tžodyje\t"<<line2<< endl;
            }
            gotoBOF(inpfile2);
            cnt++;
        }

    inpfile.close();
    inpfile2.close();
    outpfile.close();
    } else {
        return notOpen(inpf);
    }





/*    string line;
    int ilgis=0, zodziai=0,cnt=0, index=0;

    ofstream outpfile (outpf);
    ifstream inpfile (inpf);

    if (inpfile.is_open() && outpfile.is_open()){
            getline(inpfile, line);

            ilgis=line.length();
            char *eilute = new char[ilgis];
            strncpy(eilute, line.c_str(), ilgis);

            for(int i=0; i<ilgis; i++)
                if(eilute[i]==' ') zodziai++;
                zodziai++;
           // cout << zodziai<<" "<<ilgis;
            int *z_pr = new int[zodziai];
            int *wlength = new int[zodziai];

            cnt=0;
            z_pr[cnt++]=0;
            for(int i=0; i<ilgis; i++){

                if(eilute[i]==32) {
                        //cout <<eilute[i]<<"\n";
                    z_pr[cnt++]=i;
                }
            }

            //cout << z_pr[1]<<endl;
            cnt=0;

            for(int i=0; i<zodziai; i++){
                    if(i==0) wlength[i]=z_pr[i+1];
                    else if(i+1==zodziai) wlength[i]=ilgis - z_pr[i]-1;
                    else wlength[i]=z_pr[i+1] - z_pr[i]-1;
            }

            char **pavardes = new char*[zodziai];

            for(int i=0; i<zodziai; i++){
                pavardes[i] = new char[wlength[i]];
            }

            cnt=0;
            for(int i=0; i<ilgis; i++){
                    if(i < z_pr[cnt+1]){

                        pavardes[cnt][index++]=eilute[i];
                    } else if(i>z_pr[zodziai-1]) {
                        pavardes[cnt][index++]=eilute[i];
                    }else{
                        cnt++;
                        index=0;
                    }
            }
        cnt=0;
        index=0;
            for(int m=0; m<zodziai; m++)
                for(int i=0; i<wlength[m]; i++){
                    for(int n=0; n<zodziai; n++){
                        for(int j=0; j<wlength[n]; j++){
                            if(m!=n && pavardes[m][i] == pavardes[n][j]){
                                cnt++;
                                if(cnt==wlength[m]){
                                    cout << "Zodis "<<m<<endl;
                                }
                                if(cnt==wlength[n]){
                                    cout << "Zodis "<<n<<endl;
                                }
                            } else {cnt=0; index=0;}
                        }
                    }
                }

        for(int i=0; i<zodziai; i++) delete pavardes[i];
        delete wlength;
        delete z_pr;
        delete pavardes;
        delete eilute;
            }

*/
    return 0;
}


