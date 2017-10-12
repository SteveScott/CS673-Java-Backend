package main.java;

////This is a dummy class, obviously.

public class Api {
	static double dollarToRupee = 65;
	static double rupeeToDollar = 1/65.0;
	
	public Api(){}
	
	static public double fetchPrice(int stockId, String datetime){
		return 20;
	}
	
	static public double convertDollars(double rupees){
		double toDollar = rupees * rupeeToDollar;
		return toDollar;
	}

}
