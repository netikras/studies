#include<stdio.h>   //Ivedimo - isvedimo funkcijoms
#include<stdlib.h>   //exit funkcijai

struct EL            //saraso elemento struktura
{
    char ch;         //Duomenis (simbolis)
    EL   *next;      //Rodykle i kita elementa
};

EL  *start;          //Rodykle i saraso pradzia

//Funkciju prototipai
void Init_ls(void);
void Dest_ls(void);
void Add_end(char c);
void Add_beg(char c);
void Del_end(void);
void Del_beg(void);
void Create_end(void);
void Create_beg(void);
void Print_ls(void);
void After_Add(char find, char add);
void Before_Add(char find, char add);
void After_Del(char find);
void Before_Del(char find);

int main (void)            //Grazina 0 (nuli) sekmingai ivykdzius
{
    Init_ls();             //Saraso inicializavimas

/*
Saraso uzpildymas simboliais is ls.dat failo:
       pirmas perskaitytas simbolis YRA saraso pradzioje
*/    

Create_beg();
Print_ls();                //Sarasas isvedamas i ekrana
Dest_ls();                 //Sarasas sunaikinamas (destroy)
After_Del('3');            //Pasalinamas po '3'
Print_ls();
Add_end('C');              //'C' pridedamas i saraso gale
After_Del('3');            //Pasalinamas po '3'
Print_ls();
Dest_ls();                 //Sarasas sunaikinamas (destroy)

/*
Saraso uzpildymas simboliais is ls.dat failo:
       paskutinis perskaitytas simbolis YRA saraso gale
*/ 

Create_end();
Print_ls();                //Sarasas isvedamas i ekrana
Dest_ls();                 //Sarasas sunaikinamas (destroy)
Add_end('C');              //I saraso gala pridedamas 'C'
Add_end('D');              //I saraso gala pridedamas 'D'
Add_beg('B');              //I saraso pradzia pridedamas 'B'
Add_beg('A');              //I saraso pradzia pridedamas 'A'
Print_ls();
Del_end();                 //Saraso paskutiniojo elemento pasalinimas
Del_beg();                 //Sararo pirmojo elemento pasalinimas
Print_ls();
Dest_ls();                 //Sarasas sunaikinamas (destroy)

//Saraso uzpildymas penkiais dvejetukais
for (int i=1; i<=5; i++)
    Add_end('2');
    Print_ls();
    
//Prideti '1' pries '2'
Before_Add('2','1');
Print_ls();

//Prideti '3' po '2'
After_Add('2','3');
Print_ls();

//Prideti '1' pries '1'
Before_Add('1','1');
Print_ls();

//Prideti '2' po '2'
After_Add('2','2');
Print_ls();

//Prideti '2' po '2'
After_Add('2','2');
Print_ls();

//Prideti '3' po '3'
After_Add('3','3');
Print_ls();
After_Del('3');         //Pasalinimas po '3'
Print_ls();
Before_Del('1');        //Pasalinimas pries '1'
Print_ls();
Dest_ls();              //Sarasas sunaikinamas (destroy)

system("PAUSE");
return 0;
    }
    
//********************************************************
//Saraso inicializavimas
void Init_ls (void)
{
     start=NULL;        //Pradzioje sarasas tuscias
     
     return;
     }    
     
//*********************************************************
//Saraso sunaikinimas (destroy)
void Dest_ls(void)
{
     if (start==NULL)
     {
                     printf("\n Sarasas tuscias. Nera ka salinti");
                     return;
                     }
     while (start != NULL)
           Del_beg();           //Pirmoje saraso elemento pasalinimas
           
           return;
     }

//*********************************************************
//Elemento pridejimas i saraso gala
void Add_end(char c)           //Pridedamojo elemento duomenis
{
     //Rodykle i nauja (pridedama) saraso elementa
     EL        *temp,
               *cur;            //Rodykle i esama elementa
               
     temp = new EL;             //1: dinaminis elemento isdestymas
     if (temp==NULL)
     {
     printf("\n Saraso elementas nera padetas");
     exit(1);               
     }
     temp->ch=c;                 //2:duomens ikelimas
     temp->next=NULL;            //3:naujas elementas yra  paskutinis
     if (start==NULL)            //Naujas sarasas (tuscias)
        start=temp;              //4a: rodykle i saraso pradzia
     else
     {
         //4b: praeiname visa sarasa nuo pradzios, kol esamas elementas netaps paskutiniuoju
         cur=start;
         while (cur->next != NULL)
               //Judame per sarasa
               cur=cur->next;
         //4c: paskutiniuojo elemento nuoroda i nauja elementa, pridedama i saraso gala
         cur->next=temp;
         }
         return;
     }
     
//**********************************************************
//Elemento pridejimas i saraso pradzia
void Add_beg(char c)           //Pridedamojo elemento duomenis
{
     //Rodykle i nauja (pridedama) saraso elementa
     EL        *temp;
              
     temp = new EL;             //1: dinaminis elemento isdestymas
     if (temp==NULL)
     {
     printf("\n Saraso elementas nera padetas");
     exit(2);               
     }
     temp->ch=c;                  //2:duomens ikelimas
     temp->next=start;            //3:naujas elementas yra  nurodo i saraso pradzia
     start=temp;                  //4: naujas elementas tampa pirmuoju
     
         return;
     }
     
//************************************************************
//Tiesinio saraso uzpildymas simboliais is failo ls.dat:
//pirmas perskaitytas simbolis saraso pradzioje
void Create_beg(void)
{
     //Elemento domuo, kuris papildomas i saraso gala
     char c;
     //Rodykle i struktura su duomenimis apie faila skaitymui
     FILE      *f_in;
     
     //Atidarome faila skaitymui
     if ((f_in=fopen("ls.dat", "r"))== NULL)
     {
      printf("\n Failas ls.dat nera atidarytas skaitymui");
      exit(3);
        }
        //Sukuriame sarasa
        while ((c=(char)fgetc(f_in)) !=EOF)
        {
              Add_end(c);
              }
        //Uzdarome faila
        if ((fclose(f_in))==EOF)
        {
        printf("\n Failas ls.dat neuzdarytas");
        exit(4);
        }
        return;
     }
     
//*************************************************************
//Tiesinio saraso uzpildymas simboliais is ls.dat failo:
//paskutinis nuskaitytas simbolis - saraso pradzioje
void Create_end(void)
{
     //Elemento domuo, kuris papildomas i saraso gala
     char c;
     //Rodykle i struktura su duomenimis apie faila skaitymui
     FILE      *f_in;
     
     //Atidarome faila skaitymui
     if ((f_in=fopen("ls.dat", "r"))== NULL)
     {
      printf("\n Failas ls.dat nera atidarytas skaitymui");
      exit(5);
        }
        //Sukuriame sarasa
        while ((c=(char)fgetc(f_in)) !=EOF)
        {
              Add_beg(c);
              }
        //Uzdarome faila
        if ((fclose(f_in))==EOF)
        {
        printf("\n Failas ls.dat neuzdarytas");
        exit(6);
        }
        return;
     } 
     
//Saraso paskutiniojo elemento pasalinimas
void Del_end (void)
{
     EL      *prev,            //Rodykle i priespaskutini elementa
             *end;             //Rodykle i paskutini elementa
             
     if (start==NULL)
     {
      printf("\n Sarasas tuscias. Nera ka salinti");
      return;                    
      }      
      //1:paskutiniojo (ir priespaskutiniojo) elementu paieska
      prev=NULL;
      end=start;
      while (end->next !=NULL)
      {
            prev=end;
            end=end->next;                //Judejimas pagal sarasa
            }
            delete end;                   //2:Paskutiniojo elemento pasalinimas
            if (prev != NULL)
            {
             //3:buves priespaskutinis elementas tampa paskutiniu
             prev->next=NULL;
                     } 
              else
              {
                  start=NULL;          //3:sarase buvo vienas elementas
                  }            
                  return;
     }
     
//Pirmojo saraso elemento pasalinimas
void Del_beg(void)
{
     EL           *del;              //Rodykle i pasalinama elementa
     if (start==NULL)
     {
       printf("\n Sarasas tuscias, nera ka salinti");
       return;              
       }
       //1: pirmojo elemento paruosimas pasalinimui
       del=start;
       start=del->next;             //2:start paslenka i antra elementa
       delete del;                  //1:pirmojo elemento pasalinimas
       return;
     }  
     
//****************************************************************************** 
//Saraso turinio spausdinimas i ekrana
void Print_ls(void)
{
     EL    *prn;              //Rodykle i spausdinama elementa
     if (start==NULL)
     {
        printf("\n Sarasas tuscias. nera ka spausdinti");
       return;             
        }
        prn=start;         //Rodykle i saraso pradzia
        printf("\n");
        while (prn!=NULL)  //Iki saraso pabaigos
        {
              //Elemento duomenu (simboliu) spausdinimas
              printf("%c", prn->ch);
              prn=prn->next;        //Judejimas pagal sarasa
              }
        return;
     }         
     
//******************************************************************************
//add elemento pridejimas po elemento find
void After_Add(char find, char add)
{
     EL      *temp,            //Rodykle i elementa, kuri papildysime
             *cur;             //Rodykle i esama elementa
             
     if (start==NULL)
     {
        printf("\n Sarasas tuscias. Negalima surasti reikiama elementa");
        return;
        }
//Elementu, turinciu simboli find paieska su pridejimu po ju simbolio add
cur=start;
while (cur!=NULL)
{
      if (cur->ch==find)
      {
         //Reikiamas elementas surastas (jis yra esamas)
         //1:Dinaminis elemento isdestymas
         temp=new EL;
         if (temp==NULL)
         {
           printf("\n Saraso elementas nera isdestytas");
           exit(7);
           }
           //2:duomens itraukimas
           temp->ch=add;
           //3:nuoroda i elementa, kuris buvo uz sekancio
           temp->next=cur->next;
           //4:esamas elementas nurodo i nauja
           cur->next=temp;
           //5:naujas elementas tampa esamu
           cur=temp;
         }
         cur=cur->next;               //Judejimas pagal sarasa
      }        
      return;
     }  
     
//******************************************************************************
//Elemento add pridejimas pries elementa find
void Before_Add(char find, char add)
{
     EL     *temp,         //Rodykle i pridedama elementa
            *cur,          //Rodykle i esama elementa
            *prev;         //Rodykle i elementa esanti pries esama elementa
            
     if (start==NULL)
     {
       printf("\n Sarasas tuscias. Negalima surasti reikiama elementa");
       return;
       }
//Elementu, turinciu find simboli paieska, pridedant pries ji elementa su simboliu add
cur=start;
while (cur != NULL)
{
      //Reikiamas elementas surastas (jis yra esamas)
      if (cur->ch == find)
      {
         //1:Dinaminis elemento isdestymas
         temp=new EL;
         if (temp==NULL)
         {
           printf("\n Saraso elementas nera isdestytas");
           exit(8);
           }
           //2:Duomenu ikelimas
           temp->ch=add;
           //3:Naujas elementas nurodo i elementa c simboliu find
           temp->next=cur;
           //4:Jeigu elementas su simboliu find buvo pirmas,
           //tai start paslenka i kaire (i nauja elementa)
           if (cur==start)
              start=temp;
              else
                  //4:elementas esantis pries cur nurodo i nauja
                  prev->next=temp;
         }
         prev=cur;                //Esamo ir pries tai buvusio elementu poslinkis pagal sarasa
         cur=cur->next;
      }       
      return;
     }       
     
//******************************************************************************
//Elemento pasalinimas po elemento find
void After_Del(char find)
{
     EL     *del,        //Rodykle i pasalinama elementa
            *cur;        //Rodykle i esama elementa
            
     if (start==NULL)
     {
        printf("\n Sarasas tuscias. Negalima rasti reikiamo elemento");
        return;             
        }      
     if (start->next==NULL)
     {
       printf("\n Sarase tik vienas elementas. Tokios operacijos atlikti negalima.");
       return;
       }      
//Elementu, turinciu simboli find paieska, pasalinant elementus, esancius uz surastojo
cur=start;
do
{
  if (cur->ch==find)
  {
    //Reikiamas elementas surastas (jis yra esamas)
    //1:rodykle i elementa pasalinimui
    del=cur->next;
    //2:sarysis esamo elemento su elementu esanciu po salinamojo
    cur->next=del->next;
    delete del;         //3:elemento pasalinimas
    //4:ar yra dabar esamas elementas paskutinis? Jeigu "taip" - 
    //iseiname is ciklo ir funkcijos
    if (cur->next== NULL)
    return;
    }
    cur=cur->next;                  //Judejimas pagal sarasa
  }          
  while (cur->next!= NULL);
  return;
     }                                        
  
//******************************************************************************
//Elemento pasalinimas pries elementa find
void Before_Del (char find)
{
     //Rodykle i priespriespaskutini elementa pagal uzduota
     EL    *pprev,
           *del,           //Rodykle i salinama elementa
           *cur;           //Rodykle i esama elementa
           
     if (start==NULL)
     {
       printf("\n Sarasas tuscias. Negalima rasti reikiamo elemento");
       return;
       }
       if (start->next==NULL)
       {
         printf("\n Sarase tik vienas elementas. Negalima atlikti duotos operacijos.");
         return;
         }
//Elementu turinciu find simboli paieska, su pries tai esanciu elementu pasalinimu
pprev=NULL; cur=start->next;
while (cur != NULL)
{
      if (cur->ch==find)
      {
        //Reikiamas elementas surastas (jis yra esamas)
        //Rastas elementas yra antras tiesiniame sarase
        if (pprev==NULL)
        {
         //1:Salinsime pagrindini (head) elementa
         del=start;
         //2:pirmu saraso elementu dabar bus buves antras
         start=start->next;
         }
         else
         {
             //1:Rodykle i salinama elementa
             del=pprev->next;
             //2:sarysis priespriesbuvusio ir esamo elementu
             pprev->next=del->next;
             }
             delete del;           //Elemento pasalinimas
        }
        else
        {
            //Rodykles ppev postumis butinas, jeigu kitame zingsnyje nebuvo
            //pasalintas elementas
            if (pprev==NULL)
               pprev=start;
            else
                pprev=pprev->next;
            }
            cur=cur->next;                    //Sitos rodykles postumis butinas VISADA
      }        
      return;
     }  
