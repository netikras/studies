#include <algorithm>
#include <stdlib.h>  // To check for glibc
#include <string.h>  // for memcpy and memset
#include <stdint.h>
#include <iostream>

typedef uint8_t uint8;
typedef uint32_t uint32;
typedef uint64_t uint64;
typedef std::pair<uint64, uint64> uint128;

/* USAGE:
cout << CityHash32(argv[1], strlen(argv[1])) << endl;
*/


uint32 CityHash32(const char *s, size_t len);
uint64 CityHash64(const char *s, size_t len);
uint128 CityHash128(const char *s, size_t len);

uint64 CityHash64WithSeed(const char *s, size_t len, uint64 seed);
uint128 CityHash128WithSeed(const char *s, size_t len, uint128 seed);

