#include <iostream>
#include <stdio.h>
#include <math.h>
/*
2. Nustatykite pirmadienių ir penktadienių kiekį pagal metų laikus 2012 metams (n metams).
Naudotasi algoritmu iš
http://everything2.com/title/How+to+calculate+the+day+of+the+week+for+a+given+date
*/
using namespace std;



int CalendarSystem;
    /*
    CalendarSystem = 1 will return results based on the Gregorian Calendar
    CalendarSystem = 0 will return results based on the Julian Calendar
    */
int getDayOfWeek(int month, int day, int year, int CalendarSystem);
bool isALeapYear(int year); //naudosiuosi gregorijaus kalendoriumi
int febDays(int year);
int monDays(int year, int month, int need_dow);


//int first_days[2]; //0-pirmas metų pirmadienis, 1-pirmas metų penktadienis
int metai=0;
int m_fst_dow=0;
int DOM[13] = {0, 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


int main(){
    cout << "Įveskite metus\n> ";
    cin >> metai;

    CalendarSystem=1;
    DOM[2]=febDays(metai);

cout << metai << "m.\tpavasarį buvo:\n";
cout <<"\t"<<monDays(metai,3,1)+monDays(metai,4,1)+monDays(metai,5,1) <<" pirmadienių ir "<<monDays(metai,3,5)+monDays(metai,4,5)+monDays(metai,5,5) <<" penktadienių.\n";
cout << "\tvasarą:\n";
cout <<"\t"<<monDays(metai,6,1)+monDays(metai,7,1)+monDays(metai,8,1)<<" pirmadienių ir "<<monDays(metai,6,5)+monDays(metai,7,5)+monDays(metai,8,5)<<" penktadienių.\n";
cout << "\trudenį:\n";
cout <<"\t"<<monDays(metai,9,1)+monDays(metai,10,1)+monDays(metai,11,1)<<" pirmadienių ir "<<monDays(metai,9,5)+monDays(metai,10,5)+monDays(metai,11,5)<<" penktadienių.\n";
cout << "\tžiemą:\n";
cout <<"\t"<<monDays(metai,12,1)+monDays(metai,1,1)+monDays(metai,2,1)<<" pirmadienių ir "<<monDays(metai,12,5)+monDays(metai,1,5)+monDays(metai,2,5)<<" penktadienių.\n";

    return 0;
}


int febDays(int year){
    if(isALeapYear(year)) return 29;
    return 28;
}


int monDays(int year, int month, int need_dow){
    int days_found=0;
    for(int i=1; i<=DOM[month]; i++){
        if(getDayOfWeek(month, i, year, 1) == need_dow){
            days_found++;
        }

    }
    return days_found;
}



int getDayOfWeek(int month, int day, int year, int CalendarSystem){
     // CalendarSystem = 1 for Gregorian Calendar
     if (month < 3)
     {
           month = month + 12;
           year = year - 1;
     }
     return (
             day
             + (2 * month)
             + int(6 * (month + 1) / 10)
             + year
             + int(year / 4)
             - int(year / 100)
             + int(year / 400)
             + CalendarSystem
            ) % 7;
}



bool isALeapYear(int year){
	if ( (year % 4 == 0 && year % 100 != 0) || ( year % 400 == 0))
		return true;
	else
		return false;
}

