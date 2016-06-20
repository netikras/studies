// Masyvo vidurkis.
#include <cstdlib>
#include <iostream>
using namespace std;

main()
{
  int i, n=10, mas[n], sum=0;
  float vid;
  
  for(i=0; i<=n-1; i++)                         // Masyvo ávedimas.
  {
     cout<<"-->";
     cin>>mas[i];
  }
  
  cout<<"Ivedete tokius duomenis:"<<endl;       // Masyvo spausdinimas.
  for(i=0; i<=n-1; i++)
  {
     cout<<mas[i]<<endl;
  }
  
  for(i=0; i<=n-1; i++)
    sum = sum + mas[i];
  vid = (float) sum / n;
  cout<<"Masyvo elementu vidurkis lygus "<< vid <<endl;
  system("PAUSE");
  return EXIT_SUCCESS;
}
