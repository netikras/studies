#include <iostream>
#include <cstdlib>
#include <string.h>
/*
    7.	Parašykite programą, kuri patikrins,
    ar teisingai išraiškoje yra sudėlioti
    skliaustai, kurie gali būti 3-jų tipų:
        (), {} ir [].
*/


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
            }
            else if(val==-2 && readFromStack(Stack) == eilute[i]){
                remFromStack(&Stack);
            }
            else if(val==-3 && readFromStack(Stack) == eilute[i]){
                remFromStack(&Stack);
            }else{
                cout << "NERASTA:\t"<<eilute[i]<<"("<<i<<")\tTikėtasi:  "<<readFromStack(Stack)<<endl;
            }
        }

    }

int cnt=0;
    Show(Stack);//cout <<"Frazė:\t"<<fraze<< endl;

    delete eilute;


    return 0;
}
