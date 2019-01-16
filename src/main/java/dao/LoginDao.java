package dao;

import model.Login;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDao {
	/*
	 * This class handles all the database operations related to login functionality
	 */
	
	
	public Login login(String username, String password) {
		/*
		 * Return a Login object with role as "manager", "customerRepresentative" or "customer" if successful login
		 * Else, return null
		 * The role depends on the type of the user, which has to be handled in the database
		 * username, which is the email address of the user, is given as method parameter
		 * password, which is the password of the user, is given as method parameter
		 * Query to verify the username and password and fetch the role of the user, must be implemented
		 */

		Login login = new Login();
		//check the posted username and password
		Query query = new Query();
		//check if user exist in our database
		try {
			//prepare the sql
			String user = String.format("select id from person where email = '%s'", username);
			//check if user exists
			//execute the sql
			ResultSet userRes = query.execute(user);
			if (userRes != null && userRes.next()) {
				//get the userId
				String userId = userRes.getString(1);
				//check employee
				String employee = String.format("select level from employee where ssn = '%s'", userId);
				ResultSet employeeRes = query.execute(employee);
				//manager or employee
				if (employeeRes != null && employeeRes.next()) {
					int level = employeeRes.getInt(1);
					//manager
					if (level == 1) {
						login.setRole("manager");
					}
					else if (level == 0) {
						login.setRole("customerRepresentative");
					}
					employeeRes.close();
				} else {
					login.setRole("customer");
				}
				//clean up the result set
				userRes.close();
			}
			//no such user
			else {
				return null;
			}
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if(query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try

		/*Sample data begins*/
		/*
		Login login = new Login();
		login.setRole("customerRepresentative");
		return login;
		*/
		/*Sample data ends*/

		return login;
		
	}
	
	public String addUser(Login login) {
		/*
		 * Query to insert a new record for user login must be implemented
		 * login, which is the "Login" Class object containing username and password for the new user, is given as method parameter
		 * The username and password from login can get accessed using getter methods in the "Login" model
		 * e.g. getUsername() method will return the username encapsulated in login object
		 * Return "success" on successful insertion of a new user
		 * Return "failure" for an unsuccessful database operation
		 */
		
		/*Sample data begins*/
		return "success";
		/*Sample data ends*/
	}

}
