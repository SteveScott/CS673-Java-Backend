package main.java;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.exceptions.CantAffordException;
import main.java.exceptions.NotEnoughSharesToSellException;

//import com.mysql.jdbc.Connection;

public class User {
	
	int userId = 0;
	Connection conn = null;
	Database database = null;
	
	
	
	public User(Connection conn2, int i_userId, Database db_database){
		conn = conn2;
		userId = i_userId;
		database = db_database;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void addCash(double dollars) throws SQLException{
		String dateTime = WhatTime.now();
		TransactionObj cashTransaction = new TransactionObj(userId,
				1,
				true,
				dollars,
				1,
				dateTime);
		database.addTransaction(cashTransaction);
		database.changeBalance(userId, dollars);
	}
	
	public void withdrawCash(double dollars) throws SQLException, CantAffordException{
		double negativeDollars = (-1 * dollars);
		double balance = database.getBalance(userId);
		double testMe = balance - dollars;
		if (testMe < 0){
			throw new CantAffordException();
		} else {
			String dateTime = WhatTime.now();
			TransactionObj cashTransaction = new TransactionObj(
					userId,
					1,
					false,
					negativeDollars,
					1,
					dateTime);
			database.addTransaction(cashTransaction);
			database.changeBalance(userId, negativeDollars);
		}
	}
	
	public double getBalance() throws SQLException{
		return database.getBalance(userId);
	}
	
	public void buyStock(int stockId, double shares) throws SQLException, CantAffordException, NotEnoughSharesToSellException{
		//lookup price--
		String time = WhatTime.now();
		double price = Api.fetchPrice(stockId, time);
		//calculate transaction cost
		double transactionCost = price * shares;
		//test to see if you can afford it
		double balance = getBalance();
		//if you can't afford it throw CantAffordException
		if (balance - transactionCost < 0){
			throw new CantAffordException();
		}else{
			//create a transaction object
			String dateTime = WhatTime.now(); 
			TransactionObj transaction = new TransactionObj(userId, stockId, true, shares, price, dateTime );
			
			//if a holding exists in the database
			HoldingObj holding = database.fetchHolding(userId, stockId);
			try{
				//System.out.println(holding);
				//System.out.println(holding.getShares());
			} catch (Exception e) {
				
			} finally{
				if (holding != null){
					//update holding
					database.updateHolding(transaction);
				} else{	//else
					//create holding
					database.createHolding(transaction);
				}
			}
			// save transaction to the database
			//save transaction object to database
			database.addTransaction(transaction);
			//update balance
			database.changeBalance(userId, (-1.0 * transactionCost));
		}
		
	}
	
	public void buyStock(int stockId, double shares, String s_datetime) throws SQLException, CantAffordException, NotEnoughSharesToSellException{
		//lookup price--
		String time = s_datetime;
		double price = Api.fetchPrice(stockId, time);
		//calculate transaction cost
		double transactionCost = price * shares;
		//test to see if you can afford it
		double balance = getBalance();
		//if you can't afford it throw CantAffordException
		if (balance - transactionCost < 0){
			throw new CantAffordException();
		}else{
			//create a transaction object
			String dateTime = WhatTime.now(); 
			TransactionObj transaction = new TransactionObj(userId, stockId, true, shares, price, dateTime );
			
			//if a holding exists in the database
			HoldingObj holding = database.fetchHolding(userId, stockId);
			try{
				//System.out.println(holding);
				//System.out.println(holding.getShares());
			} catch (Exception e) {
				
			} finally{
				if (holding != null){
					//update holding
					database.updateHolding(transaction);
				} else{	//else
					//create holding
					database.createHolding(transaction);
				}
			}
			// save transaction to the database
			//save transaction object to database
			database.addTransaction(transaction);
			//update balance
			database.changeBalance(userId, (-1.0 * transactionCost));
		}
		//
	}
	
	public void sellStocks(int stockId, double shares) throws SQLException, NotEnoughSharesToSellException {
		//fetch price
		String time = WhatTime.now();
		double price = Api.fetchPrice(stockId, time);
		
		//create transaction object
		TransactionObj transaction = new TransactionObj(userId, stockId, false, shares, price, time);
		//check to see if you have enough shares
			//get shares from db
		HoldingObj existingHolding = database.fetchHolding(userId, stockId);
		if (existingHolding == null) {
			throw new NotEnoughSharesToSellException();
		} else {
			double existingShare = existingHolding.getShares();
			double newShares = (existingShare - shares);
			if (newShares < 0){
				throw new NotEnoughSharesToSellException();
			} else {
				//HoldingObj newHolding = new HoldingObj (userId, stockId, newShares, time);
				double existingBalance = getBalance();
				System.out.println("sellStocks shares: " + shares);
				System.out.println ("sellStocks price: " + price);
				double newTransaction = shares * price;
				database.updateHolding(transaction);
				database.changeBalance(userId, newTransaction);
			}
		} 	
	}
	
	public HoldingObj fetchHolding(int stockId) throws SQLException{
		return database.fetchHolding(userId, stockId);
		
	}
	
	public void setBalance(double newBalance) throws SQLException{
		
		database.setBalance(userId, newBalance);
	}
	
	public int getStockId(String symbol) throws SQLException{
		return database.getStockId(symbol);
	}
	
	public String getStockSymbol(int stockId) throws SQLException{
		return database.getStockSymbol(stockId);
	}
	
	public String getStockName(int stockId) throws SQLException{
		return database.getStockName(stockId);
	}
	
	public List<HoldingObj> getAllHoldings(int userId) throws SQLException
		{List<HoldingObj> listOfHoldings = new ArrayList<HoldingObj>();
		List<Integer> holdingsIds = database.fetchAllHoldingId(userId);
		for(int id : holdingsIds){
			HoldingObj i = database.fetchHolding(userId, id);
			System.out.println("i : " + i);
			System.out.println(i.getStockId());
			
			listOfHoldings.add(i);
		}
		return listOfHoldings;
	}
		
}
	

