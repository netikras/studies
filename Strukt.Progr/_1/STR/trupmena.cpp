// Trupmena.
#include <cstdlib>
#include <iostream>
#include <math.h>
using namespace std;

main()
{
  int dbd, a, b;
  struct trupmena
  {
    int skaitiklis;
    int vardiklis;
  };
  
  struct trupmena trup;
  cout<<"Ivesk trupmenos skaitikli ir vardikli"<<endl;
  cin>>trup.skaitiklis;
  cin>>trup.vardiklis;
  cout<<"Pradine trupmena: " << trup.skaitiklis<< "/" <<trup.vardiklis<<endl;
  
  a = trup.skaitiklis;                                            // Duomenø kopijos. 
  b = trup.vardiklis;

  while ( abs(a) && abs(b))                                        // DBD radimas.
    if (abs(a) > abs(b))
      a %= b; 
	else
      b %= a; 
  dbd = a + b;
  
  trup.skaitiklis /= dbd;                                          // Prastinimas.
  trup.vardiklis /= dbd;
  
  cout<<"Suprastinta trupmena: " << trup.skaitiklis<< "/" <<trup.vardiklis<<endl;
  
  system("PAUSE");
  return EXIT_SUCCESS;
}
