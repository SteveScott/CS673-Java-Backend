package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Session {
	
	public Session(){}
	Connection conn = null;
	User user = null;
	public Database database = null;
	
	public Connection connectDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			DbPassword thisPass = new DbPassword();
			String connectionUrl = thisPass.getDbUrl();
			String userName = thisPass.getDbUsername();
			String password = thisPass.getDbPassword();
			System.out.println("establishing connection...");
			conn = DriverManager.getConnection(connectionUrl, userName, password); //only accessible from NJIT campus, so secure even though public. Removed hard-coded password, changed the password, and hid the password.
			System.out.println("successfully connected");
			database = new Database(conn);
			//statement = conn.createStatement();
			//result = statement.executeQuery("SELECT table_name, table_type FROM information_schema.tables WHERE table_schema = 'db5';");
			//System.out.println(result);
			//result.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			
		}
			catch (IllegalAccessException ex) {
				System.out.println("Illegal Access Exemption: " + ex.getMessage()); 
			}
			catch (InstantiationException ex) {
				System.out.println("Instantiation Exemption: " + ex.getMessage());
			} catch (ClassNotFoundException ex) {
				System.out.println("ClassNotFound Exemption: " + ex.getMessage());
				ex.printStackTrace();
			}
		return conn;
	}
	
	public Boolean disconnectDatabase(){
		try {System.out.println("Closing connection..");
			conn.close();
			System.out.println("Connection closed.");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public void createUser(String s_firstName, String s_lastName, String s_email, String s_password) throws SQLException{
		Statement statement = conn.createStatement();
		if(s_firstName.length() > 32 || s_lastName.length() >32  || s_email.length() > 32) {
			System.out.println("A string argument is longer than 32 characters");
			//throw StringTooLongException;
		}else {	
			try {
				String sql = String.format("INSERT INTO USERS (FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)"
					+ "VALUES ('%s', '%s', '%s', '%s');", 
					s_firstName, s_lastName, s_email, s_password);
				System.out.println("creating new user");
				statement.execute(sql);
				System.out.println("new user created");
			} catch (Exception e){
				System.out.println("couldn't insert new user");
				e.printStackTrace();
			}
		}
		int userId = database.fetchUserId(s_email);
		database.initBalance(userId);
		
	}
	
	public User login (String email, String password) throws SQLException{
		int userId = 9999;
		userId = database.fetchUserId(email);
		
		try{
			database.fetchPassword(userId);
		} catch (Exception e){
			e.printStackTrace();
		}
		User user = new User(conn, userId, database); 
		System.out.println("Logged in as " + email);
		return user;
	}
	
	public int logout(){
		disconnectDatabase();
		return 0;	
	}

}
