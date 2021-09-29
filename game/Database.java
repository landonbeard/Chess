/*
 * David Black
 * Software Engineering T-TR 4:05pm
 * Dr. Smith
 * Lab 7 out: Database.java
 */

package chess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
	//Class variable declarations.
	private Connection con;
	private Properties props;
	private String query;
	private String dml;
	private String test;
	private boolean flag = true;
	
	public Database() {
		//Create a new Properties object and a new FileInputStream.
		props = new Properties();
		FileInputStream in;
		
		//Initialize the FIS with the URL of the database properties file and load into props.
		try {
			in = new FileInputStream("db.properties");
			props.load(in);
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Get the contents of each specific field in the properties file.
		try {
			String url = props.getProperty("url");
			String username = props.getProperty("user");
			String password = props.getProperty("password");

			//Initialize a database connection using the properties.
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	//This method is called when the object is loginData.
	public void query(LoginData loginData, String user, String password) throws SQLException{
		//Prepare a SQL statement with a specific query.
		PreparedStatement prepStatement;
		String query = "select username, aes_decrypt(password, 'key') from users "
				+ "where username = ? and aes_decrypt(password, 'key') = ?";
		
		try {
			/*
			 * Initialize prepStatement, then set the data contained in the '?' fields 
			 * of the previous query with the username and password. Then, execute the query, 
			 * collecting the results (a username/password combination) in a ResultSet object.
			 */
			System.out.println("Accessing database for login");
			prepStatement = con.prepareStatement(query);
			prepStatement.setString(1, user);
			prepStatement.setString(2, password);
			ResultSet rs = prepStatement.executeQuery();
			
			//If no results were returned, the username/password combination did not exist.
			//Invalidate the login, close the objects, and return to the calling method.
			if(!rs.next()) {
				loginData.setValid(false);
				prepStatement.close();
				rs.close();
				return;
			}
			
			rs.previous();
			
			while(rs.next()) {
				rs.getString(1);
				setPasswordResultForUnitTest(rs.getString(2));
			}
			
			//If results were returned, the username/password combination existed.
			//Validate the login and close the objects.
			loginData.setValid(true);
			prepStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void executeDML(CreateAccountData createData, String username, String password) throws SQLException{
		//Prepare a SQL statement with a specific query.
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		query = "select username from users where username = ?";
		
		try {
			/*
			 * Initialize prepStatement, then set the data contained in the '?' fields 
			 * of the previous query with the username. Then, execute the query, 
			 * collecting the results (a username) in a ResultSet object.
			 */
			System.out.println("Accessing database for createAccount");
			prepStatement = con.prepareStatement(query);
			prepStatement.setString(1, username);
			rs = prepStatement.executeQuery();
			
			/*
			 * If a result was not returned, the username did not already exist in the database.
			 * Validate the new account, close the prepStatement, then create a new
			 * prepStatement containing an insert statement. Execute the statement, close 
			 * all objects, and return to the calling method.
			 */
			if(!rs.next()) {
				createData.setValid(true);
				prepStatement.close();
				dml = "INSERT INTO users(username, password) VALUES (?, aes_encrypt(?, 'key'))";
				prepStatement = con.prepareStatement(dml);
				prepStatement.setString(1, username);
				prepStatement.setString(2, password);
				prepStatement.executeUpdate();
				prepStatement.close();
				rs.close();
				return;
			}
			//If we made it here, results were returned and therefore there was a duplicate.
			//Invalidate the account creation and close all objects.
			setBooleanForDMLTest(true);
			createData.setValid(false);	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			prepStatement.close();
			rs.close();
		}
	}
	
	public Connection getConnection() {
		return this.con;
	}
	
	public void setPasswordResultForUnitTest(String password) {
		this.test = password;
	}
	
	public String getPasswordResultForUnitTest() {
		return this.test;
	}
	
	public void setBooleanForDMLTest(boolean flag) {
		this.flag = flag;
	}
	
	public boolean getBooleanForDMLTest() {
		return this.flag;
	}
}
