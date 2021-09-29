package chess;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
	private String[] users = {"jsmith@uca.edu","msmith@uca.edu","tjones@yahoo.com","jjones@yahoo.com"};
	private String[] passwords = {"hello123","pass123","123456","hello1234"};
	
	private Database db;

	@Before
	public void setUp() throws Exception 
	{
		db = new Database();
	}

	//Test database connection.
	@Test
	public void testSetConnection() throws IOException 
	{
		assertNotNull("Check Connection", db.getConnection());
	}

	//NOTE:: DATABASE CAN ONLY CONTAIN THE SAME VALUES AS THE TEST ARRAYS ABOVE FOR SUCCESSFUL TEST!!!!!!!!!!
	@Test
	public void testQuery() throws IOException 
	{
		Random rand = new Random();
		int i = rand.nextInt(users.length - 1);
		
		String username = users[i]; 
		String expected = passwords[i];
		LoginData loginData = new LoginData(username, expected);
		
		try {
			db.query(loginData, username, expected);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(passwords[i]);
		System.out.println(db.getPasswordResultForUnitTest());
		assertEquals(passwords[i], db.getPasswordResultForUnitTest());
	}

	//This tests duplicate usernames. Returns true if there is a duplicate (a new account wouldn't be created).
	@Test
	public void testDML() throws SQLException  
	{	
		String username = users[0];
		String password = passwords[0];
		CreateAccountData createData = new CreateAccountData(username, password);

		//insert into database
		db.executeDML(createData, createData.getUser(), createData.getPass());
		
		//Since this is a duplicate username, the test will return true.
		assertTrue(db.getBooleanForDMLTest());
	}
}
