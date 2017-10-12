package main.java;

public class DbPassword {
	
	String dbPassword = "minuend65";
	String dbUsername = "wss5";
	String dbUrl = "jdbc:mysql://sql.njit.edu:3306/wss5";
	DbPassword(){
		
	}
	
	public String getDbPassword(){
		return dbPassword;
	}
	
	public String getDbUsername(){
		return dbUsername;
	}
	public String getDbUrl(){
		return dbUrl;
	}
	
	

}
