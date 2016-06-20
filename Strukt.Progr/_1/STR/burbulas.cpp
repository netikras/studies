// Burbulo metodas.
#include <cstdlib>
#include <iostream>
using namespace std;

main()
{
    int i, dydis = 5;                               // Masyvo kintamasis ir dydis.
    int mas[dydis];                                 // Masyvas.
    bool keitimas = true;                           // Þymi, ar buvo atliktas keitimas.
    int laik;                                       // Pagalbinis kintamasis.
    
    cout<<"Iveskite masyvo elementus"<<endl;        // Masyvo ávedimas.
    for(i=0; i<=dydis-1; i++)
    {
      cout<<"-->";
      cin>>mas[i];
    }
    
    cout<<"Nesurikiuotas masyvas:"<<endl;           // Nesurikiuotas masyvas.
    for(i=0; i<=dydis-1; i++)
      cout<<mas[i]<<" "<<endl;
    
   while (keitimas)                                 // Burbulo metodas.
   {
      keitimas = false;
      //j++;
      for (i = 0; i < dydis - 1; i++)
      {
         if (mas[i] > mas[i + 1])
         {
            laik = mas[i];
            mas[i] = mas[i + 1];
            mas[i + 1] = laik;
            keitimas = true;
                  }
            }
      }
   
    cout<<"Surikiuotas masyvas:"<<endl;             // Surikiuotas masyvas.
    for(i=0;i<=dydis-1;i++)
      cout<<mas[i]<<" "<<endl;
      
    system("PAUSE");
    return EXIT_SUCCESS;
}
