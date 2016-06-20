#include <stdint.h>


/* USAGE
uint32_t hash[4];
MurmurHash3_x86_32(argv[1], strlen(argv[1]),42, hash);
cout << hash[0]<<endl;
MurmurHash3_x86_128(argv[1], strlen(argv[1]),42, hash);
cout << hash[0]<< hash[1]<< hash[2]<< hash[3]<<endl;
*/

void MurmurHash3_x86_32 ( const void * key, int len, uint32_t seed, void * out );
void MurmurHash3_x86_128 ( const void * key, const int len, uint32_t seed, void * out );
void MurmurHash3_x64_128 ( const void * key, const int len, const uint32_t seed, void * out );

