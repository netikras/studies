// lookup3 by Bob Jekins, code is public domain.
// Modified by Darius Juodokas a.k.a. netikras

//#include "Platform.h"

#include <iostream>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

using namespace std;

#define rot(x,k) (((x)<<(k)) | ((x)>>(32-(k))))

#define mix(a,b,c) \
{ \
  a -= c;  a ^= rot(c, 4);  c += b; \
  b -= a;  b ^= rot(a, 6);  a += c; \
  c -= b;  c ^= rot(b, 8);  b += a; \
  a -= c;  a ^= rot(c,16);  c += b; \
  b -= a;  b ^= rot(a,19);  a += c; \
  c -= b;  c ^= rot(b, 4);  b += a; \
}

#define final(a,b,c) \
{ \
  c ^= b; c -= rot(b,14); \
  a ^= c; a -= rot(c,11); \
  b ^= a; b -= rot(a,25); \
  c ^= b; c -= rot(b,16); \
  a ^= c; a -= rot(c,4);  \
  b ^= a; b -= rot(a,14); \
  c ^= b; c -= rot(b,24); \
}

uint32_t lookup3 ( const void * key, int length, uint32_t initval )
{
  uint32_t a,b,c;                                          /* internal state */

  a = b = c = 0xdeadbeef + ((uint32_t)length) + initval;

  const uint32_t *k = (const uint32_t *)key;         /* read 32-bit chunks */

  /*------ all but last block: aligned reads and affect 32 bits of (a,b,c) */
  while (length > 12)
  {
    a += k[0];
    b += k[1];
    c += k[2];
    mix(a,b,c);
    length -= 12;
    k += 3;
  }

  switch(length)
  {
    case 12: c+=k[2]; b+=k[1]; a+=k[0]; break;
    case 11: c+=k[2]&0xffffff; b+=k[1]; a+=k[0]; break;
    case 10: c+=k[2]&0xffff; b+=k[1]; a+=k[0]; break;
    case 9 : c+=k[2]&0xff; b+=k[1]; a+=k[0]; break;
    case 8 : b+=k[1]; a+=k[0]; break;
    case 7 : b+=k[1]&0xffffff; a+=k[0]; break;
    case 6 : b+=k[1]&0xffff; a+=k[0]; break;
    case 5 : b+=k[1]&0xff; a+=k[0]; break;
    case 4 : a+=k[0]; break;
    case 3 : a+=k[0]&0xffffff; break;
    case 2 : a+=k[0]&0xffff; break;
    case 1 : a+=k[0]&0xff; break;
    case 0 : { return c; }              /* zero length strings require no mixing */
  }

  final(a,b,c);

  return c;
}

void lookup3_test ( const void * key, int len, uint32_t seed, void * out )
{
  *(uint32_t*)out = lookup3(key,len,seed);
}

/* Manually added function */
void die(string reason, int exitcode=0){
	cerr << reason <<endl;
	exit(exitcode);
	}

int main(int argc, char **argv){
	/* Part for running without argv
	 * const void * key, int length, uint32_t initval
	 * 
	 * string KY = "TEST";
	 *	cout << lookup3(&KY,KY.length(),42);
	*/
	
	
	if(argc < 2) die("Please provide a string to encrypt", 1);
	cout << "Encrypting \"" << argv[1] << "\" in -lookup3-" <<endl;
	//uint32_t lookup3 ( const void * key, int length, uint32_t initval )
	cout << lookup3(argv[1], strlen(argv[1]),42)<<endl;
	return 0;
}
