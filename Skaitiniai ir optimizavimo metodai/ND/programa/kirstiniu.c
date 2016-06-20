#include <math.h>
#include <stdio.h>




struct RESULT {
	double reslt;
	double x;
	int n;
	
	double prec;
};

typedef struct RESULT res;



double abs_dbl(double x){
   if(x>0) return x;
   return 0-x;
    
}


/**
    This function picks next usable X based on a formula
*/
double getNextX(double x_1, double x_2, double f_x_1, double f_x_2){
	double result;
	
	result = x_1 - (  ( (x_1-x_2) * f_x_1 )  /  (f_x_1 - f_x_2)  );
	
	printf("\tNextX := %lf - ( ( (%lf - (%lf)) * (%lf) ) / (%lf - (%lf)) ) = %lf\n", x_1, x_1, x_2, f_x_1, f_x_1, f_x_2, result);
	
	return result;
	
}


res CALC_kirstiniuMetodas(double (*f)(double), double prec, double x0, double x1){
	
	res retVal;
	retVal.reslt = 0;
	retVal.x = 0;
	retVal.n = -1;
	retVal.prec = prec;
	
	
	double X[] = {0, 0, x0,    x1   }; // first one will not be used
	double F[] = {0, 0, f(x0), f(x1)}; // first one will not be used
	
	const static int last = 3;

	retVal.n = 0;
	printf("n=[%d]  f(%lf)= %lf,    \t %lf %s %lf\n", retVal.n, X[last-1], F[last-1], abs_dbl(F[last-1]), (abs_dbl(F[last-1])>prec?"> ":"<="), prec);
	retVal.n = 1;
	printf("n=[%d]  f(%lf)= %lf,    \t %lf %s %lf\n", retVal.n, X[last], F[last], abs_dbl(F[last]), (abs_dbl(F[last])>prec?"> ":"<="), prec);
	
	
	
	//while(abs_dbl(F[last]) > prec){
	while(abs_dbl((X[last] - X[last-1]) ) > prec){
	    
	    
		retVal.n = retVal.n+1;
		
		
		X[last-2] = X[last-1];
		X[last-1] = X[last  ];
		
		F[last-2] = F[last-1];
		F[last-1] = F[last  ];
		
		X[last] = getNextX(X[last-1], X[last-2], F[last-1], F[last-2]);
		
		
		F[last] = f(X[last]);
		
	    //printf("n=[%d]  f(%lf)= %lf,    \t %lf %s %lf\n", retVal.n, X[last], F[last], abs_dbl(F[last]), (abs_dbl(F[last])>prec?"> ":"<="), prec);
	    printf("n=[%d]  f(%lf)= %lf,    \t %lf %s %lf\n", retVal.n, X[last], F[last], abs_dbl(X[last] - X[last-1]), (abs_dbl(X[last] - X[last-1])>prec?"> ":"<="), prec);
		
		retVal.x     = X[last];
		retVal.reslt = F[last];
		
		
	}
	
	
	return retVal;
	
}




double funct(double x){
    double result;
    
    result = 3*x - cos(x) - 1;
    
    return result;
    
}




double func2(double x){
	
	return x*x*x-x-0.231;
}




int main(){
	
	double precision = 0.0001;
	
	res reslt = CALC_kirstiniuMetodas(funct, precision, 0, 1);
	
	//res reslt = CALC_kirstiniuMetodas(func2,0.00001, 4, 3);
	
	
	
	if(reslt.n < 0){
	    printf("Nepavyko rasti tasku. Pameginkite pakeisti pradines reiksmes.\n");
	} else {
		printf("\nIšspresta:\nn=%d\nx=%.10lf\nf(x)=%.10lf", reslt.n, reslt.x, reslt.reslt);
	}
	
	
	return 0;
}
