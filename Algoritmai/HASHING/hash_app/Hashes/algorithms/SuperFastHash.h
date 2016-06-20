#include <stdint.h>

/* USAGE:
uint32_t seed=0;
cout << hash_inc(argv[1], strlen(argv[1]), seed)<<endl;
*/

uint32_t SuperFastHash(const char * data, int len, uint32_t hash);

