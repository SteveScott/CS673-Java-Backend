package main.java;

public class TransactionObj {
	int transactionId;
	int userId;
	int stockId;
	boolean transType;
	double shares;
	double price;
	String time;
	
	//public TransactionObj(int i_userId, int i_stockId, boolean b_transType, double d_shares, double d_price){	
	//}

	public TransactionObj(int i_userId, int i_stockId, boolean b_transType,
			double d_shares, double d_price, String dt_dateTime) {
		transactionId = 55;
		userId = i_userId;
		stockId = i_stockId;
		transType = b_transType;
		shares = d_shares;
		price = d_price;
		time = dt_dateTime;
		// TODO Auto-generated constructor stub
	}
	
	public boolean setDateTime(){
		try{
			time = WhatTime.now(); 
			return true;
		} catch ( Exception ex){
			return false;
		}
		
	}

	public String getDateTime(){
		return time;
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	
	public int getUser() {
		return userId;
	}
	
	public int getStockId(){
		return stockId;
	}
	
	public boolean getTransType(){
		return transType;
	}
	
	public double getShares(){
		return shares;
	}
	
	public double getPrice(){
		return price;
	}
	
	
	 
	
	
	


}
