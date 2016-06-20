//inline panaudojimas

#include <iostream.h>

inline float cube(const float s) { return s*s*s; }

  main()
      {
             cout<<"Iveskite kubo krastines ilgi:";
             float side;
             cin>>side;
             cout<<"Kubo turis, kurio krastine "<<side<<" lygus "<<cube(side)<<endl;
             
             system("PAUSE"); 
             return 0;
             }
                  
