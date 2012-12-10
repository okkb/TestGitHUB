package Factorial;

class Factorials {
	public static int factorial( int n )
	{
		int result=0;
		if(n==1)
		{
			return n;
		}
		else
		{
			result = n*factorial(n-1); 
			return result;
		}
	}
}
