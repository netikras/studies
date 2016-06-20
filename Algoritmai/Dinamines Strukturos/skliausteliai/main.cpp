#include <iostream>
#include <cstdlib>
#include <string.h>

using namespace std;


struct STACK{
    char skl;
    int pos;
    STACK *next;
};

int Bracket(char val){
    int br = (int)val;
    switch(br){
        case 40:    // (
            return 1; break;
        case 91:    // [
            return 2; break;
        case 123:   // {
            return 3; break;
        case 41:    // )
            return -1; break;
        case 93:    // ]
            return -2; break;
        case 125:   // }
            return -3; break;
        default:    // NOT a bracket at all
            return 0;
    }
}

int isEmpty(STACK *ptop){
    if(ptop==NULL)
        return 0;
    return 1;
}

void addToStack(STACK **ptop, char skliaust){
    STACK *temp = new STACK;
    temp->skl=skliaust;
    temp->next=*ptop;
    *ptop=temp;
}

void remFromStack(STACK **ptop){
    STACK *temp = new STACK;
    *ptop=(*ptop)->next;
    delete temp;
}

char readFromStack(STACK *ptop){
    STACK *temp = ptop;
    if(temp!=NULL)
        return temp->skl;
}

void Show(STACK *ptop){
     STACK *temp=ptop;

     while(temp!=NULL){
        cout << temp->skl << " ";
        temp=temp->next;
    }
}

int sAdd(STACK *ptop, char val){
    int var=(int)val;
    switch(var){
    case 40:
        addToStack(&ptop, (char)41); return 0; break;
    case 91:
        addToStack(&ptop, (char)93); return 0; break;
    case 123:
        addToStack(&ptop, (char)125); return 0; break;
    case 41:
        if(readFromStack(ptop) == val) remFromStack(&ptop);
        else return 1; return 0; break;
    case 93:
        if(readFromStack(ptop) == val) remFromStack(&ptop);
        else return 1; return 0; break;
    case 125:
        if(readFromStack(ptop) == val) remFromStack(&ptop);
        else return 1; return 0; break;
    }
    return -1;
}



int main()
{
    STACK *Stack = NULL;

    string fraze;
    int ilgis=0, klaida=-1, val=0;


    cout << "\nĮveskite frazę su skliausteliais\n\t> "; getline(cin, fraze);
    //fraze="Laba(s[ry]{ta})s";
    ilgis=fraze.length();

    char *eilute = new char[ilgis];

    strncpy(eilute, fraze.c_str(), ilgis);




//cout <<ilgis<< "::"<<fraze<<endl;
    for(int i=0; i<ilgis; i++){
        val=Bracket(eilute[i]);
        if(val>0){
            if(val==1)   //atsidaro
                addToStack(&Stack, ')');
            else if(val==2)
                addToStack(&Stack, ']');
            else if(val==3)
                addToStack(&Stack,'}');
        } else
        if(val<0){
            if(val==-1 && readFromStack(Stack) == eilute[i]){
                remFromStack(&Stack);
                //cout << "RASTA:\t"<<eilute[i]<<endl;
            }
            else if(val==-2 && readFromStack(Stack) == eilute[i]){
                remFromStack(&Stack);
               // cout << "RASTA:\t"<<eilute[i]<<endl;
            }
            else if(val==-3 && readFromStack(Stack) == eilute[i]){
                remFromStack(&Stack);
               // cout << "RASTA:\t"<<eilute[i]<<endl;
            }else{
                cout << "NERASTA:\t"<<eilute[i]<<"("<<i<<")\tTikėtasi:  "<<readFromStack(Stack)<<endl;
            }
        }


//        cout << eilute[i]<<" ";
    }

int cnt=0;
    Show(Stack);cout <<"Frazė:\t"<<fraze<< endl;

/*
    for(int i=ilgis-1; i>=0; i--){
        val=Bracket(eilute[i]);
        if(val!=0 && eilute[i] == readFromStack(Stack)){
            cout << "SUTAMPA("<<i<<"):\t"<<eilute[i]<<":"<<readFromStack(Stack)<<endl;
            remFromStack(&Stack);
        }
        else
        if(val!=0){
            cout << "NESUTAMPA("<<i<<"):\t"<<eilute[i]<<":"<<readFromStack(Stack)<<endl;
            remFromStack(&Stack);
        }

    }

/*
    for(int i=ilgis-1; i>=0; i--){
        if(Bracket(eilute[i])!=0){  // Brackets found
                //cout << Bracket(eilute[i])<<endl;
                cout << "Rasta:\t"<<eilute[i]<<"\tTikėtasi:\t"<<readFromStack(Stack)<<endl;
            if(Bracket(eilute[i])%2!=0 && eilute[i] == readFromStack(Stack)){   // Opening brackets
                remFromStack(&Stack);   //  Remove from Stack if found there
                cout << ++cnt<< " removing "<<eilute[i]<<endl;
            }
            else
            if(eilute[i] == readFromStack(Stack)){  //  Closing brackets (expected in Stack)
                cout << "Užsidaro:\t"<<eilute[i]<<endl;
                klaida=i;
                remFromStack(&Stack);
            } else {
                cout << "Klaida rasta pozicijoje: "<<klaida<<"\n\tTikėtasi: "<<readFromStack(Stack)<<"\n\tRasta: "<<eilute[i]<<endl;
            }
            Show(Stack);cout << endl;
        }
    }

*/
/*
    if(klaida>=0)
        cout << "Klaida rasta pozicijoje: "<<klaida<<endl;
    else
        cout << "Klaidų nerasta"<<endl;
*/
    delete eilute;


    return 0;
}
