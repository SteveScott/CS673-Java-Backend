package main.java;
import java.util.List;
import java.sql.*;

import main.java.exceptions.CantAffordException;
import main.java.exceptions.NotEnoughSharesToSellException;

class SteveTestApp{
	/*
	public static void main(String args[]) throws SQLException, NotEnoughSharesToSellException {
	//System.out.println("test test test");
	Database fooBase = new Database();
	System.out.println(fooBase.returnHello());
	
	Connection conn = fooBase.connectDatabase();
	
	TransactionObj t_o = new TransactionObj(1, 999, true, 5, 19.95, "MyNull");
	t_o.setDateTime();
	System.out.println(t_o.getUser() + ", " + t_o.getStockId() + ", " + t_o.getTransType() + ", " + t_o.getShares() + ", " + t_o.getDateTime());
	
	fooBase.addTransaction(t_o);
	
	
	fooBase.createUser("Steve", "Scott", "wss5@njit.edu", "12345abcde");
	//fooBase.createHolding(1, 517,  1000);
	//System.out.println(WhatTime.now());
	
	//HoldingObj thisHolding = fooBase.fetchHolding(1, 517);
	//System.out.println(thisHolding.getUserId() + " " + thisHolding.getStockId() + " " + thisHolding.getShares() + " " + thisHolding.getTime());
	HoldingObj holding = fooBase.fetchHolding(1, 517);
	System.out.println("Initial Shares: " + holding.getShares());
	TransactionObj t_o = new TransactionObj(1, 517, false, 100.0 , 19.95, WhatTime.now());
	fooBase.updateHolding(t_o);
	holding = fooBase.fetchHolding(1,  517);
	System.out.println("Final Shares: " + holding.getShares());
	
	fooBase.closeDatabase(conn);
	
	}
	*/
	public static void main(String[] args) throws SQLException, CantAffordException{

		
		Session session = new Session();
		session.connectDatabase();
		
		//session.createUser("Steve", "Scott", "stevescott517@gmail.com", "12345");
	try{
		User user = session.login("stevescott517@gmail.com", "12345");
		System.out.println("UserId: " + user.getUserId());
		int userId = user.getUserId();
		user.setBalance(5000);
		System.out.println("Initial balance: " + user.getBalance());
		//user.addCash(300);
		//System.out.println("Add Cash: " + user.getBalance());
		//user.withdrawCash(100);
		//System.out.println("Withdraw Cash: " + user.getBalance());
		//database.fetchHolding(userId 517);
		user.buyStock(517, 10);
		HoldingObj holding = user.fetchHolding(517);
		System.out.println("balance: " + user.getBalance());
		System.out.println("Holding of " + holding.getStockId() + " is " + holding.getShares());	
		user.sellStocks(517, 10);
		holding = user.fetchHolding(517);
		System.out.println("Holding of " + holding.getStockId() + " is " + holding.getShares());	
		System.out.println("new balance: " + user.getBalance());
		
		//int stockId = user.getStockId("USD");
		//System.out.println(user.getStockName(stockId));
		///System.out.println(user.getStockSymbol(stockId));
		
		user.buyStock(518, 10);
		user.buyStock(519, 10);
		user.buyStock(520, 10);
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		List<HoldingObj> listOfHoldings = user.getAllHoldings(user.getUserId());
		System.out.println("List of Holdings = " + listOfHoldings);
		for(HoldingObj i : listOfHoldings){
			System.out.println(i.getUserId() + ", " + i.getStockId() +", " + i.getShares() + ", " + i.getTime() + ";;;");
		}
		session.logout();
	
				
	} catch (NotEnoughSharesToSellException e){
		e.getStackTrace();
	}
}
}