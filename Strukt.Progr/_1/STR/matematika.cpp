//Matematines bibliotekos panaudojimo pavyzdys

#include <iostream.h>
#include <math.h>

main()
      {
         int veiksmas;
         float skaicius, laipsnis, rezultatas;
         cout<<"Pasirinkite veiksma:"<<endl;
         cout<<"\t[1]-skaiciaus kelimas laispniu"<<endl; 
         cout<<"\t[2]-kvadratine saknis"<<endl;
         cout<<"\t[3]-exp(x) apskaiciavimas"<<endl;
         cin>>veiksmas;
         switch (veiksmas) {
               case 1:{ 
                        cout<<"Iveskite skaiciu:"<<endl;
                        cin>>skaicius;
                        cout<<"Iveskite laipsni:"<<endl;
                        cin>>laipsnis;
                        rezultatas=pow(skaicius, laipsnis);
                        cout<<"Atsakymas: "<<rezultatas<<endl;
                        }; break;
               case 2:{
                       cout<<"Iveskite skaiciu:"<<endl;
                        cin>>skaicius;
                        rezultatas=sqrt(skaicius);
                        cout<<"Atsakymas: "<<rezultatas<<endl;
                        } break;
               case 3:{
                       cout<<"Iveskite skaiciu:"<<endl;
                        cin>>skaicius;
                        rezultatas=exp(skaicius);
                        cout<<"Atsakymas: "<<rezultatas<<endl;
                        } break;
               default: cout<<"Daugiau atidumo..."<<endl;
               }
         system("PAUSE"); 
         return 0;
         
         }
