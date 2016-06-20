#include <fstream.h>
#include <iostream.h>

using namespace std;

struct Node {
       int left, right;
       Node* p;
       };
       
Node* push(Node* top, const int l, const int r);
Node* pop(Node* top, int &l, int &r);

//---------------------------------------------
main()
{
    int i, n;
    float middle, temp;
      
    ifstream   fin;
    fin.open("sort1.txt", ios::in);
    fin >> n;

    float* arr = new float[n];
    for (i=0; i<n; i++) 
    fin>>arr[i];
    
    //rusiavimas
    
    int j, left, right;
    Node* top=0;
    top=push(top, 0, n-1);
    
    while (top) {
          top =pop(top, left, right);
          while (left<right) {
                //Isskirstymas {arr[left]..arr[right]}
                i=left; j=right;
                middle=arr[(left+right)/2];
                while (i<j) {
                      while (arr[i]<middle) i++;
                      while (middle<arr[j]) j--;
                      if (i<=j) {
                                temp=arr[i]; arr[i]=arr[j]; arr[j]=temp;
                                i++; j--;
                                }
                                }
                                if (i<right) {
                                             //Irasymas i steko uzklausa is desines
                                             top=push(top, i, right);
                                             }
                                             right=j;
                                             //Dabar left ir right apriboja kairiaja puse
                                             }
                                             }
                                             //Rezultato pateikimas
                                             for (i=0; i<n; i++) 
                                             cout<< arr[i] << ' ';
                                             cout<<endl;
                                             system("pause");
                                             return 0;
                                             }
                                             
                                             //Ádëti á stekà
                                             
                                             Node* push(Node* top, const int l, const int r) 
                                             {
                                                   Node* pv=new Node;   
                                                   pv->left=l;          
                                                   pv->right=r;	
                                                   pv->p=top;	
                                                   return pv;	
                                                   }

                                                   //Paimti ið steko
                                                   Node* pop(Node* top, int& l, int& r) 
                                                   {
                                                         Node* pv= top -> p;	
                                                         l=top->left;	
                                                         r=top->right;	
                                                         delete top;	
                                                         return pv;	
                                                         }

