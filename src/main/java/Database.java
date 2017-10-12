package main.java;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.exceptions.NotEnoughSharesToSellException;


public class Database {
	Connection conn = null;
	public Database(Connection c_conn) {
		conn = c_conn;
	}
	
	public void loadDriver(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
	}
	
	public void addTransaction(TransactionObj thisTrans) throws SQLException{
		Statement statement = conn.createStatement();
		//result = statement.executeQuery("SELECT table_name, table_type FROM information_schema.tables WHERE table_schema = 'db5';");
		//System.out.println(result);
		//result.close();
		String sqlQuery = String.format("INSERT INTO TRANSACTIONS (USER_ID, STOCK_ID, TRANS_TYPE, SHARES, PRICE, TIME)"
				+ " VALUES (%d, %d, %b, %f, %f, STR_TO_DATE('%s', '%%Y-%%m-%%d %%H:%%i:%%s'));",
				//"INSERT INTO TRANSACTIONS (USER_ID, STOCK_ID, TRANS_TYPE, SHARES, PRICE, TIME) VALUES (%d, %d, %b, %f, %f, %s);", 
				thisTrans.getUser(),
				thisTrans.getStockId(),
				thisTrans.getTransType(),
				thisTrans.getShares(), 
				thisTrans.getPrice(),
				thisTrans.getDateTime());
		//System.out.println(sqlQuery);
		try{
			statement.execute(sqlQuery);
			//System.out.println("Transaction recorded");
		} catch (Exception e){
			e.printStackTrace();;
			System.out.println("Transaction failed");
		}
	}
	
	
	
	public void createHolding(int userId, int stockId, double shares) throws SQLException{
		Statement statement = conn.createStatement();
		String time = WhatTime.now(); 
		String sql = String.format("INSERT INTO HOLDINGS (USER_ID, STOCK_ID, SHARES, TIME)"
				+ "VALUES (%d, %d, %f, STR_TO_DATE('%s', '%%Y-%%m-%%d %%H:%%i:%%s' ));",
				userId, stockId, shares, time);
		try{
			statement.execute(sql);
			//System.out.println("New holding created.");
		} catch (Exception e){
			//System.out.println("Couldn't create a new holding");
			e.printStackTrace();
		}
		
	}
	public void createHolding(TransactionObj transaction) throws SQLException{
		Statement statement = conn.createStatement();
		
		int userId = transaction.getUser();
		int stockId = transaction.getStockId();
		double shares = transaction.getShares();
		String time = WhatTime.now(); 	
		String sql = String.format("INSERT INTO HOLDINGS (USER_ID, STOCK_ID, SHARES, TIME)"
				+ "VALUES (%d, %d, %f, STR_TO_DATE('%s', '%%Y-%%m-%%d %%H:%%i:%%s' ));",
				userId, stockId, shares, time);
		try{
			statement.execute(sql);
			//System.out.println("New holding created.");
		} catch (Exception e){
			System.out.println("Couldn't create a new holding");
			e.printStackTrace();
		}
		
	}
	
	public HoldingObj fetchHolding(int userId, int stockId) throws SQLException{
		
		String time = "";
		double returnedShares = 9999.999999;
		ResultSet result = null;
		Statement statement = conn.createStatement();
		boolean exist = false;
		
		String query = String.format(
				  "SELECT SHARES, TIME "
				+ "FROM HOLDINGS "
				+ "WHERE USER_ID = %d "
				+ "AND STOCK_ID = %d;", userId, stockId);
		//System.out.println(query);
		result = statement.executeQuery(query);
		//test to see if a holding exists
		exist = result.next();
		//System.out.println("Is there a holding? " + exist);
		//System.out.println("ResultSet: " + result);
		//if the holding exists:
		if (exist == true){

			returnedShares = result.getDouble("SHARES");
			
			time = result.getString("TIME"); 
			//System.out.println("Shares: " + shares);
			//System.out.println("Time: " + time);
			
			//System.out.println("Returned Shares: " + returnedShares);
			
			
				
			HoldingObj fetchedHolding = new HoldingObj(userId, stockId, returnedShares, time);
			//System.out.println("returning a holding");
			return fetchedHolding;
		} else {
			//System.out.println("returning null");
			return null;
		}
		
		
	}
	
	public void updateHolding(TransactionObj transaction) throws SQLException, NotEnoughSharesToSellException{
		int userId = transaction.getUser();
		int stockId = transaction.getStockId();
		double buySellShares = transaction.getShares();
		Boolean buySell = transaction.getTransType();
		HoldingObj holding = fetchHolding(userId, stockId);
		double currentShares = holding.getShares();
		//System.out.println("Initial Shares" + currentShares);
		if (buySell == true){
			holding.setShares(currentShares + buySellShares);
			System.out.println(buySellShares + " have been added.  You now have " + holding.getShares() + " shares.");
		} else {
			if (buySellShares > currentShares){
				throw new NotEnoughSharesToSellException();
			} else{
				holding.setShares(currentShares - buySellShares);
				System.out.println(buySellShares + " have been removed.  You now have " + holding.getShares() + " shares.");
			}
			
		}
		holding.setDateTime(WhatTime.now());
		Statement statement = conn.createStatement();
		String sql = String.format(""
				+ "UPDATE HOLDINGS "
				+ "SET SHARES = %f, TIME = '%s' "
				+ "WHERE USER_ID = %d AND STOCK_ID = %d;",
				holding.getShares(),
				holding.getTime(),
				holding.getUserId(),
				holding.getStockId());
		//System.out.println(sql);
		statement.execute(sql);		
	}
	///
	
	public int fetchUserId(String email) throws SQLException{
		int userId = 9999;
		//System.out.println("Email1: " + email);
		Statement statement = conn.createStatement();
		String sql = String.format(""
				+ "SELECT USER_ID FROM USERS "
				+ "WHERE EMAIL = '%s';", email);
		ResultSet result = statement.executeQuery(sql);
		result.next();
		//int myUserId = result.getInt("USER_ID");
		//System.out.println("Result:" + myUserId);
		userId = result.getInt("USER_ID");
		
		
		//System.out.println(userId);
		return userId;
	}
	
	public String fetchPassword(int userId) throws SQLException{
		String password = "XXXX";
		Statement statement = conn.createStatement();
		String sql = String.format(""
				+ "SELECT PASSWORD FROM USERS "
				+ "WHERE USER_ID = %d;", userId);
		ResultSet result = statement.executeQuery(sql);
		result.next();
		password = result.getString("PASSWORD");
		
		return password;
	} 
	
	public void changeBalance(int userId, double dollars) throws SQLException{
		Statement statement = conn.createStatement();
		double initBalance = getBalance(userId);
		double newDollars = dollars + initBalance;
		String sql = String.format(""
				+ "UPDATE BALANCES "
				+ "SET BALANCE = %f "
				+ "WHERE USER_ID = %d;", newDollars, userId);
		statement.execute(sql);
	}
	
	public void setBalance(int userId, double dollars) throws SQLException{
		Statement statement = conn.createStatement();
		String sql = String.format(""
				+ "UPDATE BALANCES "
				+ "SET BALANCE = %f "
				+ "WHERE USER_ID = %d;", dollars, userId);
		statement.execute(sql);
	}
	
	public void initBalance(int userId) throws SQLException{
		Statement statement = conn.createStatement();
		String sql = String.format(""
				+  "INSERT INTO BALANCES "
				+ "VALUES (%d, 0);", userId);
		statement.execute(sql);
				
	}
	
	public double getBalance(int userId) throws SQLException{
		double balance = 9999;
		Statement statement = conn.createStatement();
		String sql = String.format(""
				+ "SELECT BALANCE FROM BALANCES "
				+ "WHERE USER_ID = %d;", userId);
		ResultSet result = statement.executeQuery(sql);
		while (result.next()){
			balance = result.getDouble("BALANCE");
		}
		
		return balance;
	}
	
	public int getStockId(String symbol) throws SQLException{
		int stockId = 9999; 
		Statement statement = conn.createStatement();
		ResultSet result = null;
		String sql = String.format(""
				+ "SELECT STOCK_ID FROM STOCK_NAMES "
				+ "WHERE SYMBOL = '%s'; "
				, symbol);
		result = statement.executeQuery(sql);
		while (result.next()){
			stockId = result.getInt("Stock_Id");
		}
		return stockId;
	}	
	

	public String getStockSymbol(int stockId) throws SQLException {
		String stockSymbol;
		Statement statement = conn.createStatement();
		ResultSet result = null;
		String sql = String.format(""
				+ "SELECT SYMBOL FROM STOCK_NAMES "
				+ "WHERE STOCK_ID = %d;", stockId);
		result = statement.executeQuery(sql);
		result.next();
		stockSymbol = result.getString("SYMBOL");
		return stockSymbol;		
	}
	
	public String getStockName(int stockId) throws SQLException {
		String stockName;
		Statement statement = conn.createStatement();
		ResultSet result = null;
		String sql = String.format(""
				+ "SELECT STOCK_NAME FROM STOCK_NAMES "
				+ "WHERE STOCK_ID = %d;", stockId);
		result = statement.executeQuery(sql);
		result.next();
		stockName = result.getString("STOCK_NAME");
		return stockName;
	}
	
	public List<Integer> fetchAllHoldingId(int userId) throws SQLException{
		List<Integer> listOfStock = new ArrayList<Integer>(); 
		Statement statement = conn.createStatement();
		ResultSet result = null;
		String sql = String.format(""
				+ "SELECT STOCK_ID FROM HOLDINGS "
				+ "WHERE USER_ID = %d;", userId);
		result = statement.executeQuery(sql);
		System.out.println("ResultSet: " + result);
		while(result.next()){
			int thisResult = result.getInt("STOCK_ID");
			System.out.println("thisResult = " +thisResult);
			listOfStock.add(thisResult);
		}
		System.out.println(listOfStock);
		return listOfStock;
				
	}
/*	
	public List<HoldingObj> fetchAllHolding(int userId) throws SQLException{
		List<HoldingObj> listOfStock = new ArrayList<HoldingObj>(); 
		Statement statement = conn.createStatement();
		ResultSet result = null;
		String sql = String.format(""
				+ "SELECT STOCK_ID FROM HOLDINGS "
				+ "WHERE USER_ID = %d", userId);
		result = statement.executeQuery(sql);
		while(result.next()){
			int thisResult = result.getInt("STOCK_ID");
			HoldingObj thisHolding =
			listOfStock.add(thisResult);
		}
		System.out.println(listOfStock);
		return listOfStock;
				
	}
	*/
}
