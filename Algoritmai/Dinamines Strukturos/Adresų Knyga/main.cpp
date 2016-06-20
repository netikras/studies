#include <iostream>
#include <fstream>
#include <string.h>
#include <cstdlib>
#include <sstream>

using namespace std;

#define sourcefile "kontaktai"
//const char delim[3]={'/','_','/'};
const string delim="/_/";
const int delim_length=3;


/*#############################################
 ######## Functions to deal with files ########
################################################
                                               #
                                               #*/
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
    int cnt=0; string eil;
    gotoBOF(file);
    while(file.good()){
        getline(file, eil);
        cnt++;
    }
    gotoBOF(file);

    return --cnt;
}
                                             /*#
                                               #
################################################
 ##### END of Functions to deal with files ####
################################################*/

int isNum(const char *n);
int isNum(const std::string & s);
int addContact(const char* DBfile);
int findContact(string number);
int showMenu();

void cls(){cout << "\ec";}
bool term(char* x){return (*x=='.'?true:false);}
int yesNo(string justification, string ans1="Taip", string ans2="Ne");

string itos(int x){
    stringstream ss;
    ss << x;
    return ss.str();
}

struct NOTE {
    char vardas[20]={'\0'};
    char pavarde[20]={'\0'};
    char tel_nr[20]={'\0'}; // imamas ne INT, nes naudotojas gali pageidauti įvesti numerį: +<kodas-numeris> -- tarptautinis
    int gim_data[3]={-1}; // ('1990', '09', '18')
    NOTE *next=0;
    NOTE *current=0;
    NOTE *prev=0;
    NOTE *first=0;
    NOTE *last=0;
    NOTE *base=0; // the 0-th element - controlling element. Contains metadata of the stack
};


void sortNOTE(NOTE* pNOTE, NOTE* nNOTE);
NOTE* shiftNote(NOTE *src);
int writeToFile(NOTE* srcNOTE);
NOTE* readFromFile(NOTE *srcNOTE);
void printNOTE(NOTE *pNOTE);
NOTE* addNote(NOTE* last, NOTE* nNOTE);
int removeContact();
string modContact();
NOTE* SLtoNOTE(string S_Liner);
string NOTEtoSL(NOTE* pNOTE);
int totalCount(NOTE* pNOTE);

void mark(){
cout << "---MARK---" << endl;
}

int main(){
    string sear_str;
    while (true) {
        switch(showMenu()){
        case 0:
            exit(0);break;
        case 1:
          addContact(sourcefile);break;

        case 2:
            cout << "Įveskite ieškomą tel. nr.:\n > "; getline(cin,sear_str);
            findContact(sear_str); break;
        case 3:
            removeContact(); break;
        case 4:
            cout << modContact()<<endl; break;
        default:
            cout <<"Shifting...\n";continue;
        }
    }
    //addContact(sourcefile);


    return 0;
}

/*###########################
 ####### FUNCTIONS #########
#############################
                            #
                            #*/

int showMenu(){
    int retVal=-1;
    string line;
    cout << "1. Naujas kontaktas"<<endl;
    cout << "2. Rasti įrašą (pagal telefono numerį)"<<endl;
    cout << "3. Pašalinti įrašą"<<endl;
    cout << "4. Redaguoti įrašą"<<endl;
    cout << "\n0. Išeiti"<<endl;

    cout << "-----------------\n\n> ";
    getline(cin, line);
    cls();
    if(atoi(line.c_str()) <= 4 && atoi(line.c_str()) >= 0 && line < "5" && line >= "0")
        retVal=atoi(line.c_str());
    else
        cerr << "Nėra tokio pasirinkimo\n";
    return retVal;
}


// MAIN FUNCTIONS
NOTE* addNote(NOTE* last, NOTE* nNOTE){

    if(last->base==0){ // jei kontroleris dar nesukurtas
        nNOTE->base     =last;  // sukuriamas kontroleris
        //cout << "nnote->base="<<nNOTE->base<<endl<<"last="<<last<<endl<<endl;
        last->first     =nNOTE; // kontroleryje nustatomas pirmasis elementas
        last->last      =nNOTE; // kontroleryje nustatomas paskutinysis elementas
        last->next      =nNOTE; // kontroleryje nustatomas kitas elementas

        nNOTE->prev     =last;
        nNOTE->current  =nNOTE;

        return nNOTE;
    }

    //nNOTE->base->first=last->first;
    nNOTE->current  =nNOTE;
    nNOTE->base         =last->base; // perduodamas kontrolerio adresas
    nNOTE->base->last   =nNOTE;     // pakeičiamas paskutinio elemento adresas kontroleryje
    last->next          =nNOTE;     // naujas elementas prijungiamas prie buvusio
    nNOTE->prev         =last;      //buvęs elementas prijungiamas prie naujo

    return nNOTE;                   //grąžinamas naujas elementas
}


int addContact(const char* DBfile=sourcefile){
    /*
    Function adds a new contact to database
    Usage:
        addContact(database_file_name | path_to_database_file)
    */

    string line, bday,gimData, singleLiner;
    int cnt=0, lines_cnt=0, lineToAdd=0, bDay_i=0;
    int posFname=0, posLname=0, posBday=0, posPhone=0;

    NOTE* n_adresatas = new NOTE;

        cout <<"Įveskite naujo adresato duomenis:";
        cout <<"\n\t*Vardas:\t>"; cin >> n_adresatas->vardas; if(term(n_adresatas->vardas)) return 2;
        cout <<"\tPavardė:\t>"; cin >> n_adresatas->pavarde;if(term(n_adresatas->pavarde)) return 2;
        cout <<"\t*Gimimo data:";
            cout << "\n\t\tYYYY:\t>"; cin >> n_adresatas->gim_data[0];
            cout << "\t\tMM:\t>"; cin >> n_adresatas->gim_data[1];
            cout << "\t\tDD:\t>"; cin >> n_adresatas->gim_data[2];
        cout <<"\t*Tel.nr.:\t>"; cin >> n_adresatas->tel_nr;if(term(n_adresatas->tel_nr)) return 2;

/* Converting birth date from INT to STR */
    gimData=itos(n_adresatas->gim_data[0]);
        if (n_adresatas->gim_data[1]<10) gimData+="0";
    gimData=gimData + itos(n_adresatas->gim_data[1]);
        if (n_adresatas->gim_data[2]<10) gimData+="0";
    gimData=gimData + itos(n_adresatas->gim_data[2]);

    singleLiner=NOTEtoSL(n_adresatas);
cout << "STRING: " << singleLiner<<endl;

    bDay_i=atoi(gimData.c_str()); //converting to int in order to find which line the entry should be added to

    ifstream inpFile(DBfile);

    char** adresatai;

    if(inpFile.is_open()){
        lines_cnt=linesCount(inpFile);

        adresatai = new char*[lines_cnt];
        while(inpFile.good() && lines_cnt>0){
            getline(inpFile, line);
            // finding the line number the new entry will be stored in
            if(line.length()==0 || line == "" || line == " ") continue;

            posFname=0;
            posLname=line.find((string)delim, posFname)+delim_length;
            posPhone=line.find((string)delim, posLname)+delim_length;
            posBday=line.find((string)delim, posPhone)+delim_length;

            bday=line.substr(posBday,posPhone);

            if(bDay_i < atoi(bday.c_str())) lineToAdd++;
            adresatai[cnt] = new char[100];

            strncpy(adresatai[cnt], line.c_str(), line.length());
            cnt++;
        }
    }
    inpFile.close();

    ofstream outpFile(DBfile);
    if(outpFile.is_open()){
        while (outpFile.good()){
            for(int i=0; i<lines_cnt; i++){
                if(i==lineToAdd){
                    outpFile << singleLiner << "\n";
                    lineToAdd=-1;
                    i--;
                    continue;
                }
                outpFile << adresatai[i]<<"\n";
            }
            if(lineToAdd>0) outpFile << singleLiner<<endl;
            break;
        }
    }

    outpFile.close();
    delete adresatai;

    delete n_adresatas;
    return 0;
}


int findContact(string number){
    string line;
    NOTE *nNOTE = new NOTE;

    nNOTE=readFromFile(nNOTE);
    nNOTE=nNOTE->base->next;

    while(nNOTE && number != "*"){
            if(nNOTE->tel_nr == number){
                printNOTE(nNOTE);
                cout<<endl;
            }
            nNOTE=shiftNote(nNOTE);
    }

    while(nNOTE && number == "*"){
        printNOTE(nNOTE);
        cout<<endl;
        nNOTE=shiftNote(nNOTE);
    }

    delete nNOTE;

    return 0;

}


int removeContact(){
    cls();
    NOTE* pNOTE = new NOTE;
    pNOTE=readFromFile(pNOTE);

    pNOTE=pNOTE->base->first;
    string line; int cnt1=0, ind=0;
    int *arrN = new int[totalCount(pNOTE)];

    cout << "Įveskite ieškomą frazę (bus ieškoma visuose laukuose):\n > ";
    getline(cin, line);

    string str;

    cout << "--------------" <<endl;
    cout << "Paieškos rezultatai:\n\n";

    while(1){
           str=NOTEtoSL(pNOTE);
        if(str.find(line) != string::npos) {
            cout <<ind<<". -----------\n";
            printNOTE(pNOTE);cout<<endl;
            arrN[ind++]=cnt1;
        }
        cnt1++;
        if(pNOTE->next==0) break;

        pNOTE=pNOTE->next;
    }


ind--;

    if(ind==-1) {
        cout << "\tNerasta!\n"<<endl;
        return ind;

    }

    cout << "\nPasirinkite paieškos rezultato numerį, kurį norite šalinti:\n > ";
    getline(cin, line);
    if(line=="." || line == "" || line == " ") return 2;


    if(isNum(line) && yesNo("Ar tikrai norite pašalinti rezultatą -"+line+"- ?" )){
        cnt1=arrN[atoi(line.c_str())];
        pNOTE=pNOTE->base->first;
        for(int i=0; i<cnt1; i++)
            pNOTE=pNOTE->next;
        cout << endl<<"Pašalintas įrašas:\n";
        printNOTE(pNOTE);

        //cout << "prev="<<pNOTE->prev<<"\nbase="<<pNOTE->base<<endl;

        if(pNOTE->prev==0) { //jei pirmasis elementas
            pNOTE->next->prev=pNOTE->prev;
            pNOTE->base->first=pNOTE->next;
            pNOTE->base->next=pNOTE->next;
        } else {
            if(pNOTE==pNOTE->base->last){ //jei paskutinis elementas
                pNOTE->base->last=pNOTE->prev;
                pNOTE->prev->next=0;
            } else {                // jei bet kuris kitas elementas
                pNOTE->prev->next = pNOTE->next;
                if(pNOTE->next)
                pNOTE->next->prev = pNOTE->prev;
            }
        }
        writeToFile(pNOTE->base->first);

    }
    cls();
    delete pNOTE;
    //delete arrN;

    return 0;

}

string modContact(){

    string retVal="";

    NOTE* pNOTE = new NOTE;

    pNOTE=readFromFile(pNOTE);
    pNOTE=pNOTE->base->first;

    string line;
    int cnt1=0, ind=0;
    int pos1=0, pos2=0;
    bool resort=false;
    int *arrN = new int[totalCount(pNOTE)];

    cout << "Įveskite ieškomą frazę (bus ieškoma visuose laukuose):\n > ";
    getline(cin, line);

    string str;

    cout << "--------------" <<endl;
    cout << "Paieškos rezultatai:\n\n";

    while(1){
           str=NOTEtoSL(pNOTE);
        if(str.find(line) != string::npos) {
            cout <<ind<<". -----------\n";
            printNOTE(pNOTE);cout<<endl;
            arrN[ind++]=cnt1;
        }
        cnt1++;
        if(pNOTE->next==0) break;

        pNOTE=pNOTE->next;
    }
    ind--;

    if(ind==-1) {
        cls();
        return "Toks įrašas nerastas\n";

    }

    cout << "\nPasirinkite paieškos rezultato numerį, kurį norite redaguoti:\n > ";
    getline(cin, line);
    if(line=="." || line == "" || line == " ") {cls();return "";}


    if(isNum(line)){
        cnt1=arrN[atoi(line.c_str())];
        pNOTE=pNOTE->base->first;
        for(int i=0; i<cnt1; i++)
            pNOTE=pNOTE->next;
            cls();
        cout << endl<<"Redaguojamas įrašas:\n";
        printNOTE(pNOTE);
        cout<<endl<<"-----------"<<endl;
            line="";
            NOTE* tmpNOTE = new NOTE;
            tmpNOTE->current=tmpNOTE;
            strcpy(tmpNOTE->vardas, pNOTE->vardas);
            strcpy(tmpNOTE->pavarde, pNOTE->pavarde);
            strcpy(tmpNOTE->tel_nr, pNOTE->tel_nr);
                tmpNOTE->gim_data[0]=pNOTE->gim_data[0];
                tmpNOTE->gim_data[1]=pNOTE->gim_data[1];
                tmpNOTE->gim_data[2]=pNOTE->gim_data[2];
            //cout<<pNOTE->gim_data[0]<<endl;
        cout<<"Dabartinis Vardas:\t"<<tmpNOTE->vardas<<endl;
        cout<<"\tNaujas\t> "; getline(cin, line);
            if(line!=tmpNOTE->vardas && line!="" && line.length()<20) {line+='\0'; strncpy(tmpNOTE->vardas,line.c_str(),line.length()); line="";} ;
        cout<<"Dabartinė Pavardė:\t"<<tmpNOTE->pavarde<<endl;
        cout<<"\tNauja\t> "; getline(cin, line);
            if(line!=tmpNOTE->pavarde && line!="" && line.length()<20) {line+='\0'; strncpy(tmpNOTE->pavarde, line.c_str(),line.length()); line=""; };
        cout<<"Dabartinis Tel.Nr.:\t"<<tmpNOTE->tel_nr<<endl;
        cout<<"\tNaujas\t> "; getline(cin, line);
            if(line!=tmpNOTE->tel_nr && line!="" && line.length()<20) {line+='\0'; strncpy(tmpNOTE->tel_nr,line.c_str(),line.length()); line="";};
        cout<<"Dabartinė gimimo data:\t"<<tmpNOTE->gim_data[0]<<"-"<<tmpNOTE->gim_data[1]<<"-"<<tmpNOTE->gim_data[2]<<endl;
        cout<<"\tNauja\t> "<<endl;
            cout<<"\t\tMetai: > ";getline(cin, line);
                if(isNum(line) && atoi(line.c_str())!=tmpNOTE->gim_data[0] && line!="" && line.length()<5) {
                        tmpNOTE->gim_data[0]=atoi(line.c_str()); line=""; resort=true;
                }
            cout<<"\t\tMėnuo: > ";getline(cin, line);
                if(isNum(line) && atoi(line.c_str())!=tmpNOTE->gim_data[1] && line!="" && line.length()<3) {
                        tmpNOTE->gim_data[1]=atoi(line.c_str()); line="";resort=true;
                }
            cout<<"\t\tDiena: > ";getline(cin, line);
                if(isNum(line) && atoi(line.c_str())!=tmpNOTE->gim_data[2] && line!="" && line.length()<3) {
                        tmpNOTE->gim_data[2]=atoi(line.c_str()); line=""; resort=true;
                }

        cls();//cout<<endl<<endl;
        cout<<"Originalus įrašas:\n\n"; printNOTE(pNOTE);
        cout<<"\nPakeistas įrašas:\n\n"; printNOTE(tmpNOTE);
        if(!yesNo("\nSaugoti pakeitimus?")) {delete tmpNOTE;cls();return "Pakeitimai neišsaugoti\n";}

        if(!resort) {delete tmpNOTE;cls();return "Įrašas pakeistas. Pakeitimai išsaugoti\n";}


        // unsetting the element
        if(pNOTE->next)
        pNOTE->next->prev   =pNOTE->prev;
        pNOTE->prev->next   =pNOTE->next;

        pNOTE->base->first=pNOTE->base->next;

        pNOTE=pNOTE->base->first;


        //cout<<"resorting"<<endl<<endl;

        sortNOTE(pNOTE, tmpNOTE);
        /*getline(cin,line);
        cout<<"All Notes2\n"<<endl;
        pNOTE=pNOTE->base->first;
        while (1){
            printNOTE(pNOTE);cout<<endl;
            if(pNOTE->next==0) break;
            pNOTE=pNOTE->next;
        }
        */
        pNOTE=pNOTE->base->first;
        if(writeToFile(pNOTE)==0)
            cout<<"Pakeitimai išsaugoti."<<endl;
            else
                cout<<"Pakeitimų išsaugoti nepavyko"<<endl;
        getline(cin,line);

    }

    cls();

    delete pNOTE;
    return retVal;
}

// HELPER FUNCTIONS

int yesNo(string justification, string ans1, string ans2){
    /*
    yesNo() asks a question (arg1) and returns 1 if answer is "Yes" (or arg2)
    Returns 0 if answer is arg3 or anything else but arg2.
    */

    string ans;
    cout << justification<<endl;
    cout << endl<<"1. "<<ans1;
    cout << endl<<"2. "<<ans2;
    cout << endl << " >";
    getline(cin,ans);
    if(ans == ans1 || ans == "1") return 1;
    return 0;
}

void sortNOTE(NOTE* pNOTE, NOTE* nNOTE){

    while(1){
        if(nNOTE->gim_data[0] > pNOTE->gim_data[0] ||\
           (nNOTE->gim_data[0] == pNOTE->gim_data[0] && nNOTE->gim_data[1] > pNOTE->gim_data[1]) ||\
           (nNOTE->gim_data[0] == pNOTE->gim_data[0] && nNOTE->gim_data[1] == pNOTE->gim_data[1] && \
                nNOTE->gim_data[2] > pNOTE->gim_data[2]) || \
           (nNOTE->gim_data[0] == pNOTE->gim_data[0] && nNOTE->gim_data[1] == pNOTE->gim_data[1] && nNOTE->gim_data[2] == pNOTE->gim_data[2]) \
           ){
                //cout << "\t---BLIP---"<<endl;
                    //cout<<i<<"/"<<j<<endl<<"pNOTE:\n";printNOTE(pNOTE);cout<<endl<<"tmpNOTE:\n";printNOTE(tmpNOTE);

                pNOTE->prev->next   =nNOTE;

                nNOTE->prev         =pNOTE->prev;
                pNOTE->prev         =nNOTE;

                nNOTE->next         =pNOTE;
           break;

           }
           if(!pNOTE->next){ //paskutinis elementas, o vieta vis dar nerasta -- grūdame į galą
                pNOTE->next=nNOTE;
                nNOTE->prev=pNOTE;
                nNOTE->next=0;
                break;
           }
           pNOTE=pNOTE->next;
    }

}

NOTE* SLtoNOTE(string S_Liner){
    NOTE* n_NOTE = new NOTE;

    int pos1 = 0;
    int pos2 = 0;

    string substring;

/*    posLname=line.find((string)delim, posFname)+delim_length;
    posPhone=line.find((string)delim, posLname)+delim_length;
    posBday=line.find((string)delim, posPhone)+delim_length;
*/
    pos2=S_Liner.find(delim, pos1)+delim_length;
    if(pos2 == -1+delim_length) return 0;
    substring=S_Liner.substr(pos1,pos2-pos1-delim_length);
    strcpy(n_NOTE->vardas,substring.c_str());
    pos1=pos2;

    pos2=S_Liner.find(delim, pos1)+delim_length;
    if(pos2 == -1+delim_length) return 0;
    substring=S_Liner.substr(pos1,pos2-pos1-delim_length);
    strcpy(n_NOTE->pavarde,substring.c_str());
    pos1=pos2;

    pos2=S_Liner.find(delim, pos1)+delim_length;
    if(pos2 == -1+delim_length) return 0;
    substring=S_Liner.substr(pos1,pos2-pos1-delim_length);
    strcpy(n_NOTE->tel_nr,substring.c_str());
    pos1=pos2;

    if(pos2 == -1+delim_length) return 0;
    substring=S_Liner.substr(pos1);
    if(substring.length() < 4) { n_NOTE->gim_data[0] = atoi(substring.c_str());return n_NOTE;}
    n_NOTE->gim_data[0] = atoi(substring.substr(0, 4).c_str());
    if(substring.length() < 6) return n_NOTE;
    n_NOTE->gim_data[1] = atoi(substring.substr(4, 2).c_str());
    if(substring.length() < 8) return n_NOTE;
    n_NOTE->gim_data[2] = atoi(substring.substr(6, 2).c_str());

    //delete n_NOTE;

    return n_NOTE;

}

int totalCount(NOTE* pNOTE){
    int cnt=0; pNOTE=pNOTE->base->first;
    while(pNOTE->next){
        cnt++;
        pNOTE=pNOTE->next;
    }
    pNOTE=pNOTE->base->first;
    return cnt;
}

NOTE* readFromFile(NOTE *srcNOTE){
    /*
    Reads file to stack-like structure of NOTEs.
    Return value is the last element.
    Previous ones can be accessed via *NOTE->prev
    */
   ifstream inpFile(sourcefile);
   //int retVal=0;
   string line;
   NOTE *lastNOTE = srcNOTE;

    if (inpFile.is_open()){
        while(inpFile.good()){
            getline(inpFile, line);
            //cout << line<<endl;
            if(line.length()<=0) continue;
            lastNOTE=addNote(lastNOTE,SLtoNOTE(line));
        }
    } //else retVal=1;
    inpFile.close();

   return lastNOTE;
}

int writeToFile(NOTE* srcNOTE){
    /*
    USAGE: writeToFile(NOTE* srcNOTE); where srcNOTE is
        the 0th (the very first added) element of the stack.
    */
    ofstream outpFile(sourcefile);
    int retVal=0;

    if(outpFile.is_open()){
        while(outpFile.good() && srcNOTE){
            outpFile << NOTEtoSL(srcNOTE)<<endl;
            srcNOTE=shiftNote(srcNOTE);
        }
    } else retVal = -1;
    outpFile.close();

    return retVal;
}


NOTE* shiftNote(NOTE *src){
    return src->next;
}

void printNOTE(NOTE *pNOTE){
    cout <<"Vardas: \t"<< pNOTE->vardas<<endl;
    cout <<"Pavardė:\t"<< pNOTE->pavarde<<endl;
    cout <<"Tel.nr.:\t"<< pNOTE->tel_nr<<endl;
    cout <<"Gimimo data:\t"<< pNOTE->gim_data[0]<<"-"<<pNOTE->gim_data[1]<<"-"<<pNOTE->gim_data[2]<<endl;
//cout <<"Previous:\t"<<pNOTE->prev<<endl<<"Current:\t"<<pNOTE->current<<endl;cout <<"Next\t\t"<<pNOTE->next<<endl;

}

string NOTEtoSL(NOTE* pNOTE){
    string sl="";

    sl += (string)pNOTE->vardas;
    sl += delim;
    sl += (string)pNOTE->pavarde;
    sl += delim;
    sl += (string)pNOTE->tel_nr;
    sl += delim;

    sl += itos(pNOTE->gim_data[0]);
    if (pNOTE->gim_data[1]<10) sl += "0"; sl += itos(pNOTE->gim_data[1]);
    if (pNOTE->gim_data[2]<10) sl += "0"; sl += itos(pNOTE->gim_data[2]);
//cout << sl<<endl;
    //delete pNOTE;

    return sl;
}

int isNum(const std::string & s){
   if(s.empty() || ((!isdigit(s[0])) && (s[0] != '-') && (s[0] != '+'))) return false ;

   char * p ;
   strtol(s.c_str(), &p, 10) ;

   return (*p == 0) ;
}

int isNum(const char *n){
    if((int)n > 47 && (int)n < 58)
        return 1;
    return 0;
}

                          /*#
                            #
#############################
 #### END of FUNCTIONS #####
#############################*/
