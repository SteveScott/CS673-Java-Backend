package main.java;

public class HoldingObj {
	
	int userId;
	int stockId;
	double shares;
	String time;
	
	public HoldingObj(
						int i_userId,
						int i_stockId,
						double d_shares,
						String dt_time){
		
		userId = i_userId;
		stockId = i_stockId;
		shares = d_shares;
		time = dt_time;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public int getStockId(){
		return stockId;
	}
	
	public double getShares(){
		return shares;
	}
	
	public String getTime(){
		return time;
	}
	
	public void setShares(double newShares){
		shares = newShares;
	}
	
	public void setDateTime(String dateTime) {
		time = dateTime;
	}
	
	
	

}
