/*Aplink vedantyj� stovi N �moni�, kurie yra sunumeruoti pagal laikrod�io rodykle
 skai�iais nuo 1 iki N. Vedantysis, pradedant nuo pirmojo, atskai�iuoja M �moni�, 
 ir tas, ties kuriuo skai�iavimas sustos, i�eina i� rato. Skai�iavimas t�siamas 
 nuo sekan�io �mogaus ir taip iki tol, kol neliks vienas �mogus. Reikia nustatyti 
 likusio �mogaus numer�.*/
 #include <iostream.h>
 #include <conio.h>
 int List[100], before_start, i, N, M, Start;
 void Find();                 //Funkcijos prototipas
 main()
 {
       cin>>N>>M;             //Laukiama dvieju kintamuju
       for (i=0; i<N-1; i++)
           List[i]=i+1;     //Kiekvienas masyvo elementas lygus sekanciam po jo
                            //elemento indeksui
       List[N-1]=0;         //Paskutinis elementas nurodo i nulini
       while (Start!=List[Start])
       {
             Find();       //Vykdoma funkcija, kuri atskaiciuoja M zmoniu
             //Pasalinamas zmogus is eiles, kurio numeris Start
             List[before_start]=List[Start];
             Start=List[Start];
             }
             cout<<(Start+1);               //Isveda rezultata
             getch();                       //Uzlaiko ekrana
             return 0;                      //Uzbaigia programa
       }
       
 void Find()                //Funkcijos aprasymas
 {
      for (i=0; i<M-1; i++)
      {
          before_start=Start;  //Isimenemas zmogaus numeris duotame etape
          Start=List[Start];   //Zmogaus numeris, ties kuriuo sustojo skaiciavimas
          }
      }
