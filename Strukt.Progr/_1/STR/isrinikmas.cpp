// Rikiavimo iðrinkimu metodas.
#include <cstdlib>
#include <iostream>
using namespace std;

main()
{
    int i, dydis = 5;                               // Masyvo kintamasis ir dydis.
    int mas[dydis];                                 // Masyvas.
    bool keitimas = true;                           // Þymi, ar buvo atliktas keitimas.
    int laik, j, n, nuo, t;                         // Pagalbiniai kintamieji.
    
    cout<<"Iveskite masyvo elementus"<<endl;        // Masyvo ávedimas.
    for(i=0; i<=dydis-1; i++)
    {
      cout<<"-->";
      cin>>mas[i];
    }
    
    cout<<"Nesurikiuotas masyvas:"<<endl;           // Nesurikiuotas masyvas.
    for(i=0; i<=dydis-1; i++)
      cout<<mas[i]<<" "<<endl;

                                                    // Rikiavimo iðrinkimu metodas.
    for(i = 0; i < dydis; i++)
    {
       nuo = i;
       for(j = i+1; j < dydis; j++)
            if (mas[j] < mas[nuo]) nuo = j;
        t = mas[i];
        mas[i] = mas[nuo];
        mas[nuo] = t;
    }



    
    cout<<"Surikiuotas masyvas:"<<endl;             // Surikiuotas masyvas.
    for(i=0;i<=dydis-1;i++)
      cout<<mas[i]<<" "<<endl;
      
    system("PAUSE");
    return EXIT_SUCCESS;
}
