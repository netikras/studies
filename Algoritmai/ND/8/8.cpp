#include <string>
#include <iostream>
/*
    8.	Sukurkite algoritmą ir parašykite programą,
    kuri romėniškus skaičius pavers arabiškais.
*/

using namespace std;

string numerals = "VXLCDM";

int main()
{
	char roman_Numeral;
	int arabic_Numeral = 0;

	cout << "Įveskite romėnišką skaičių didžiosiomis raidėmis (pvz.: CCXIX) : ";
	while(cin.get(roman_Numeral))
	{
		if(roman_Numeral == 'M')
			arabic_Numeral = arabic_Numeral + 1000;

		else if(roman_Numeral == 'D')
		{
			roman_Numeral = cin.peek();
			if(numerals.find(roman_Numeral, 5) != string::npos)
			{
				arabic_Numeral = arabic_Numeral - 500;
				continue;
			}
			else
			{
				arabic_Numeral = arabic_Numeral + 500;
				continue;
			}
		}

		else if(roman_Numeral == 'C')
		{
			roman_Numeral = cin.peek();
			if(numerals.find(roman_Numeral, 4) != string::npos)
			{
				arabic_Numeral = arabic_Numeral - 100;
				continue;
			}
			else
			{
				arabic_Numeral = arabic_Numeral + 100;
				continue;
			}
		}

		else if(roman_Numeral == 'L')
		{
			roman_Numeral = cin.peek();
			if(numerals.find(roman_Numeral, 3) != string::npos)
			{
				arabic_Numeral = arabic_Numeral - 50;
				continue;
			}
			else
			{
				arabic_Numeral = arabic_Numeral + 50;
				continue;
			}
		}

		else if(roman_Numeral == 'X')
		{
			roman_Numeral = cin.peek();
			if(numerals.find(roman_Numeral, 2) != string::npos)
			{
				arabic_Numeral = arabic_Numeral - 10;
				continue;
			}
			else
			{
				arabic_Numeral = arabic_Numeral + 10;
				continue;
			}
		}

		else if(roman_Numeral == 'V')
		{
			roman_Numeral = cin.peek();
			if(numerals.find(roman_Numeral, 1) != string::npos)
			{
				arabic_Numeral = arabic_Numeral - 5;
				continue;
			}
			else
			{
				arabic_Numeral = arabic_Numeral + 5;
				continue;
			}
		}

		else if(roman_Numeral == 'I')
		{
			roman_Numeral = cin.peek();
			if(numerals.find(roman_Numeral) != string::npos)
			{
				arabic_Numeral = arabic_Numeral - 1;
				continue;
			}
			else
			{
				arabic_Numeral = arabic_Numeral + 1;
				continue;
			}
		}
		else
			break;
	}
	cout << arabic_Numeral << endl;
	return 0;
}
