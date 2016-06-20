#include <iostream>
/*
    3.	Apskaičiuoti penktadienių, kurie tenka mėnesio 13-tai dienai
    per visą XXI amžių, kiekį.

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

int main(){
    CalendarSystem = 1;
    int cnt=0;
    for(int year=2000; year<2100; year++){
        for(int month=1; month<=12; month++){
            if(getDayOfWeek(month, 13, year, CalendarSystem) == 5) {
                    cnt++;
                    cout << "Metai: "<<year<<"  mėnuo: "<<month<<endl;
            }
        }
        cout << endl;
    }
    cout << "-------------\nViso: " << cnt << endl;

	return 0;
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
