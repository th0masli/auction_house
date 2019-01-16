package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.Employee;

public class EmployeeDao {
	/*
	 * This class handles all the database operations related to the employee table
	 */
	
	public String addEmployee(Employee employee) {

		/*
		 * All the values of the add employee form are encapsulated in the employee object.
		 * These can be accessed by getter methods (see Employee class in model package).
		 * e.g. firstName can be accessed by employee.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database insertion of the employee details and return "success" or "failure" based on result of the database insertion.
		 */

		//prepare the sql
		//update person table
		String person_sql = "INSERT INTO Person\n" +
				"(ID, LastName, FirstName, Address, City, State, ZipCode, Telephone, Email)\n" +
				"VALUES ";

		String person_val = String.format(" ('%s', '%s', '%s', '%s', '%s', '%s', %d, '%s', '%s')",
				employee.getEmployeeID() , employee.getLastName(), employee.getFirstName(),
				employee.getAddress(), employee.getCity(), employee.getState(), employee.getZipCode(),
				employee.getTelephone(), employee.getEmail());
		//update customer table
		String employee_sql = "INSERT INTO Customer\n" +
				"(SSN, StartDate, HourlyRate, Level)\n" +
				"VALUES ";
		String employee_val = String.format(" ('%s', '%s', 0, 0, %d)",
				employee.getEmployeeID(), employee.getStartDate(), employee.getHourlyRate(), employee.getLevel());
		Query query = new Query();
		//update person table
		boolean res1 = query.update(person_sql + person_val);
		//update customer table
		boolean res2 = query.update(employee_val + employee_val);

		if (res1 && res2) {
			return "success";
		} else {
			return "fail";
		}

		/*Sample data begins*/
		//return "success";
		/*Sample data ends*/

	}

	public String editEmployee(Employee employee) {
		/*
		 * All the values of the edit employee form are encapsulated in the employee object.
		 * These can be accessed by getter methods (see Employee class in model package).
		 * e.g. firstName can be accessed by employee.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database update and return "success" or "failure" based on result of the database update.
		 */

		//prepare the sql
		//String sql = "UPDATE Customer SET Rating = 65 WHERE CustomerID = '" + customer.getCustomerID() + "'";
		String sql = "UPDATE Employee SET ";
		String val = String.format("Email = '%s', FirstName = '%s', LastName = '%s', Address = '%s', City = '%s', " +
						"State = '%s', ZipCode = %d, Telephone = '%s', StartDate = '%s', HourlyRate = '%s', Level = '%s'",
				employee.getEmail(), employee.getFirstName(), employee.getLastName(), employee.getAddress(),
				employee.getCity(), employee.getState(), employee.getZipCode(), employee.getTelephone(),
				employee.getStartDate(), employee.getHourlyRate(), employee.getLevel());
		//System.out.println(sql + val);

		Query query = new Query();

		boolean res = query.update(sql + val);
		//System.out.println(res);

		if (res) {
			return "success";
		} else {
			return "fail";
		}

		/*Sample data begins*/
		//return "success";
		/*Sample data ends*/

	}

	public String deleteEmployee(String employeeID) {
		/*
		 * employeeID, which is the Employee's ID which has to be deleted, is given as method parameter
		 * The sample code returns "success" by default.
		 * You need to handle the database deletion and return "success" or "failure" based on result of the database deletion.
		 */

		//prepare the sql
		//String sql = "UPDATE Customer SET Rating = 65 WHERE CustomerID = '" + customer.getCustomerID() + "'";
		String customer_sql = "Delete from Employee where customerID = '" + employeeID + "'";
		String person_sql = "Delete from Person where ID = '" + employeeID + "'";
		//System.out.println(sql);
		Query query = new Query();

		boolean res1 = query.update(customer_sql);
		boolean res2 = query.update(person_sql);
		//System.out.println(res);

		if (res1 && res2) {
			return "success";
		} else {
			return "fail";
		}

		/*Sample data begins*/
		//return "success";
		/*Sample data ends*/

	}

	
	public List<Employee> getEmployees() {

		/*
		 * The students code to fetch data from the database will be written here
		 * Query to return details about all the employees must be implemented
		 * Each record is required to be encapsulated as a "Employee" class object and added to the "employees" List
		 */

		List<Employee> employees = new ArrayList<Employee>();

		//init a query object by default
		Query query = new Query();
		try {
			//prepare the sql
			String sql = "SELECT * FROM Employee e, Person p WHERE p.ID = e.SSN";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate the result set
				while (res.next()) {
					//encapsulate the result
					Employee employee = new Employee();
					employee.setEmail(res.getString(11));
					employee.setFirstName(res.getString(7));
					employee.setLastName(res.getString(6));
					employee.setAddress(res.getString(8));
					employee.setCity(res.getString(9));
					employee.setStartDate(res.getString(2));
					employee.setState(res.getString(10));
					employee.setZipCode(res.getInt(11));
					employee.setTelephone(res.getString(12));
					employee.setEmployeeID(res.getString(1));
					employee.setHourlyRate(res.getInt(3));
					employees.add(employee);
				}
				//clean up the result set
				res.close();
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
		for (int i = 0; i < 10; i++) {
			Employee employee = new Employee();
			employee.setEmail("shiyong@cs.sunysb.edu");
			employee.setFirstName("Shiyong");
			employee.setLastName("Lu");
			employee.setAddress("123 Success Street");
			employee.setCity("Stony Brook");
			employee.setStartDate("2006-10-17");
			employee.setState("NY");
			employee.setZipCode(11790);
			employee.setTelephone("5166328959");
			employee.setEmployeeID("631-413-5555");
			employee.setHourlyRate(100);
			employees.add(employee);
		}
		*/
		/*Sample data ends*/
		
		return employees;
	}

	public Employee getEmployee(String employeeID) {

		/*
		 * The students code to fetch data from the database based on "employeeID" will be written here
		 * employeeID, which is the Employee's ID who's details have to be fetched, is given as method parameter
		 * The record is required to be encapsulated as a "Employee" class object
		 */

		Employee employee = new Employee();

		//init a query object by default
		Query query = new Query();
		try {
			//prepare the sql
			String sql = "SELECT * FROM Employee e, Person p WHERE e.SSN = '" + employeeID + "' AND p.ID = e.SSN";
			//System.out.println(sql);
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//encapsulate the result
				employee.setEmail(res.getString(11));
				employee.setFirstName(res.getString(7));
				employee.setLastName(res.getString(6));
				employee.setAddress(res.getString(8));
				employee.setCity(res.getString(9));
				employee.setStartDate(res.getString(2));
				employee.setState(res.getString(10));
				employee.setZipCode(res.getInt(11));
				employee.setTelephone(res.getString(12));
				employee.setEmployeeID(res.getString(1));
				employee.setHourlyRate(res.getInt(3));
				//clean up the result set
				res.close();
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
		employee.setEmail("shiyong@cs.sunysb.edu");
		employee.setFirstName("Shiyong");
		employee.setLastName("Lu");
		employee.setAddress("123 Success Street");
		employee.setCity("Stony Brook");
		employee.setStartDate("2006-10-17");
		employee.setState("NY");
		employee.setZipCode(11790);
		employee.setTelephone("5166328959");
		employee.setEmployeeID("631-413-5555");
		employee.setHourlyRate(100);
		*/
		/*Sample data ends*/
		
		return employee;
	}
	
	public Employee getHighestRevenueEmployee() {
		
		/*
		 * The students code to fetch employee data who generated the highest revenue will be written here
		 * The record is required to be encapsulated as a "Employee" class object
		 */
		
		Employee employee = new Employee();

		//init a query object by default
		Query query = new Query();
		try {
			//prepare the sql
			String sql = "SELECT Employee.SSN, Person.Email, Person.FirstName, Person.LastName, EmployeeRevenue.TotalRevenue * 0.1 \n" +
						 "FROM Employee, EmployeeRevenue, Person \n" +
						 "WHERE Employee.SSN = EmployeeRevenue.EmployeeID AND Employee.SSN = Person.ID\n" +
						 "ORDER BY EmployeeRevenue.TotalRevenue DESC LIMIT 1;";
			//System.out.println(sql);
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null && res.next()) {
				//encapsulate the result
				employee.setEmail(res.getString(2));
				employee.setFirstName(res.getString(3));
				employee.setLastName(res.getString(4));
				employee.setEmployeeID(res.getString(1));
				//clean up the result set
				res.close();
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
		employee.setEmail("shiyong@cs.sunysb.edu");
		employee.setFirstName("Shiyong");
		employee.setLastName("Lu");
		employee.setEmployeeID("631-413-5555");
		*/
		/*Sample data ends*/
		
		return employee;
	}

	public String getEmployeeID(String username) {
		/*
		 * The students code to fetch data from the database based on "username" will be written here
		 * username, which is the Employee's email address who's Employee ID has to be fetched, is given as method parameter
		 * The Employee ID is required to be returned as a String
		 */

		String employeeID = "";
		//init a query object by default
		Query query = new Query();
		try {
			//prepare the sql
			String sql = "SELECT SSN FROM Employee e, Person p where P.Email = '" + username + "' and e.SSN = p.ID";
			//System.out.println(sql);
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				if (res.next()) {
					//encapsulate the result
					employeeID = res.getString(1);
				}
				//clean up the result set
				res.close();
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

		return employeeID;

		//return "111-11-1111";
	}

}
