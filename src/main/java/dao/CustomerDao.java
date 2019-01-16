package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Customer;

import java.util.stream.IntStream;

public class CustomerDao {
	/*
	 * This class handles all the database operations related to the customer table
	 */
	
	/**
	 * @param String searchKeyword
	 * @return ArrayList<Customer> object
	 */
	public List<Customer> getCustomers(String searchKeyword) {
		/*
		 * This method fetches one or more customers based on the searchKeyword and returns it as an ArrayList
		 */
		
		List<Customer> customers = new ArrayList<Customer>();

		/*
		 * The students code to fetch data from the database based on searchKeyword will be written here
		 * Each record is required to be encapsulated as a "Customer" class object and added to the "customers" List
		 */

        //init a query object by default
        Query query = new Query();
        try {
            //no keyword then select all customers
            if (searchKeyword == null)
                searchKeyword = "";
            //prepare the sql
            String sql = "SELECT * FROM Customer c, Person p WHERE " +
                         "(c.CustomerID Like '%" + searchKeyword + "%' or p.lastname like '%" + searchKeyword + "%' or p.firstname like '%" + searchKeyword + "%') " +
                         " AND p.ID = c.CustomerID;";
            //System.out.println(sql);
            //execute the sql
            ResultSet res = query.execute(sql);
            if (res != null) {
                //iterate the result set
                while (res.next()) {
                    //encapsulate the result
                    Customer customer = new Customer();
                    customer.setCustomerID(res.getString(1));
                    customer.setCreditCard(res.getString(2));
                    customer.setRating(res.getInt(5));
                    customer.setLastName(res.getString(7));
                    customer.setFirstName(res.getString(8));
                    customer.setAddress(res.getString(9));
                    customer.setCity(res.getString(10));
                    customer.setState(res.getString(11));
                    customer.setZipCode(res.getInt(12));
                    customer.setTelephone(res.getString(13));
                    customer.setEmail(res.getString(14));
                    customers.add(customer);
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

		return customers;
	}


	public Customer getHighestRevenueCustomer() {
		/*
		 * This method fetches the customer who generated the highest total revenue and returns it
		 * The students code to fetch data from the database will be written here
		 * The customer record is required to be encapsulated as a "Customer" class object
		 */

        Customer customer = new Customer();

        //init a query object by default
        Query query = new Query();
        try {
            //prepare the sql
            String sql = "SELECT Customer.CustomerID, Person.LastName, Person.FirstName, Person.Email, Winner.TotalPurchase * 0.1 " +
                         "FROM Winner, Customer, Person " +
                         "WHERE Winner.CustomerID = Customer.CustomerID and Customer.CustomerID = Person.ID " +
                         "ORDER BY Winner.TotalPurchase DESC LIMIT 1;";
            //System.out.println(sql);
            //execute the sql
            ResultSet res = query.execute(sql);
            if (res != null && res.next()) {
                //encapsulate the result
                customer.setCustomerID(res.getString(1));
                customer.setLastName(res.getString(2));
                customer.setFirstName(res.getString(3));
                customer.setEmail(res.getString(4));
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
		Customer customer = new Customer();
		customer.setCustomerID("111-11-1111");
		customer.setLastName("Lu");
		customer.setFirstName("Shiyong");
		customer.setEmail("shiyong@cs.sunysb.edu");
		*/
		/*Sample data ends*/
	
		return customer;
		
	}

	public List<Customer> getCustomerMailingList() {

		/*
		 * This method fetches the all customer mailing details and returns it
		 * The students code to fetch data from the database will be written here
		 * Each customer record is required to be encapsulated as a "Customer" class object and added to the "customers" List
		 */

		
		List<Customer> customers = new ArrayList<Customer>();

        //init a query object by default
        Query query = new Query();
        try {
            //prepare the sql
            String sql = "SELECT Person.* FROM Customer, Person WHERE Person.ID = Customer.CustomerID";
            //execute the sql
            ResultSet res = query.execute(sql);
            if (res != null) {
                //iterate the result set
                while (res.next()) {
                    //encapsulate the result
                    /*
                    Customer customer = new Customer();
                    customer.setCustomerID(res.getString(1));
                    customer.setEmail(res.getString(2));
                    customers.add(customer);
                    */
                    Customer customer = new Customer();
                    customer.setCustomerID(res.getString(1));
                    customer.setAddress(res.getString(4));
                    customer.setLastName(res.getString(2));
                    customer.setFirstName(res.getString(3));
                    customer.setCity(res.getString(5));
                    customer.setState(res.getString(6));
                    customer.setEmail(res.getString(9));
                    customer.setZipCode(res.getInt(7));
                    customers.add(customer);
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
			Customer customer = new Customer();
			customer.setCustomerID("111-11-1111");
			customer.setAddress("123 Success Street");
			customer.setLastName("Lu");
			customer.setFirstName("Shiyong");
			customer.setCity("Stony Brook");
			customer.setState("NY");
			customer.setEmail("shiyong@cs.sunysb.edu");
			customer.setZipCode(11790);
			customers.add(customer);			
		}
		*/
		/*Sample data ends*/
		
		return customers;
	}

	public Customer getCustomer(String customerID) {

		/*
		 * This method fetches the customer details and returns it
		 * customerID, which is the Customer's ID who's details have to be fetched, is given as method parameter
		 * The students code to fetch data from the database will be written here
		 * The customer record is required to be encapsulated as a "Customer" class object
		 */

        Customer customer = new Customer();

        //init a query object by default
        Query query = new Query();
        try {
            //prepare the sql
            String sql = "SELECT * FROM Customer c, Person p WHERE c.CustomerID = '" + customerID + "' AND p.ID = c.CustomerID";
            //System.out.println(sql);
            //execute the sql
            ResultSet res = query.execute(sql);
            if (res != null && res.next()) {
                //encapsulate the result
                customer.setCustomerID(customerID);
                customer.setAddress("123 Success Street");
                customer.setLastName("Lu");
                customer.setFirstName("Shiyong");
                customer.setCity("Stony Brook");
                customer.setState("NY");
                customer.setEmail("shiyong@cs.sunysb.edu");
                customer.setZipCode(11790);
                customer.setTelephone("5166328959");
                customer.setCreditCard("1234567812345678");
                customer.setRating(res.getInt(5));
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
		Customer customer = new Customer();
		customer.setCustomerID("111-11-1111");
		customer.setAddress("123 Success Street");
		customer.setLastName("Lu");
		customer.setFirstName("Shiyong");
		customer.setCity("Stony Brook");
		customer.setState("NY");
		customer.setEmail("shiyong@cs.sunysb.edu");
		customer.setZipCode(11790);
		customer.setTelephone("5166328959");
		customer.setCreditCard("1234567812345678");
		customer.setRating(1);
		*/
		/*Sample data ends*/
		
		return customer;
	}


	//cannot delete due to the foreign key constraint
	public String deleteCustomer(String customerID) {

		/*
		 * This method deletes a customer returns "success" string on success, else returns "failure"
		 * The students code to delete the data from the database will be written here
		 * customerID, which is the Customer's ID who's details have to be deleted, is given as method parameter
		 */

        //prepare the sql
        //String sql = "UPDATE Customer SET Rating = 65 WHERE CustomerID = '" + customer.getCustomerID() + "'";
        String customer_sql = "Delete from Customer where customerID = '" + customerID + "'";
        String person_sql = "Delete from Person where ID = '" + customerID + "'";
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


	public String getCustomerID(String username) {
		/*
		 * This method returns the Customer's ID based on the provided email address
		 * The students code to fetch data from the database will be written here
		 * username, which is the email address of the customer, who's ID has to be returned, is given as method parameter
		 * The Customer's ID is required to be returned as a String
		 */

		String customerID = "";
        //init a query object by default
        Query query = new Query();
        try {
            //prepare the sql
            String sql = "select customerID from customer c, person p where p.email = '" + username + "' and c.customerid = p.id";
            //System.out.println(sql);
            //execute the sql
            ResultSet res = query.execute(sql);
            if (res != null && res.next()) {
                //encapsulate the result
                customerID = res.getString(1);
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

		return customerID;
	}


	public List<Customer> getSellers() {
		
		/*
		 * This method fetches the all seller details and returns it
		 * The students code to fetch data from the database will be written here
		 * The seller (which is a customer) record is required to be encapsulated as a "Customer" class object and added to the "customers" List
		 */

		List<Customer> customers = new ArrayList<Customer>();

        //init a query object by default
        Query query = new Query();
        try {
            //prepare the sql
            String sql = "SELECT c.CustomerID, p.Address, p.LastName, p.FirstName, p.City, p.State, p.Email, p.ZipCode, c.Rating " +
                         "From Customer c, Person p, Post p1 WHERE c.CustomerID = p1.CustomerID AND c.CustomerID = p.ID";
            //System.out.println(sql);
            //execute the sql
            ResultSet res = query.execute(sql);
            if (res != null) {
                while (res.next()) {
                    //encapsulate the result
                    Customer customer = new Customer();
                    customer.setCustomerID(res.getString(1));
                    customer.setRating(res.getInt(9));
                    customer.setAddress(res.getString(2));
                    customer.setLastName(res.getString(3));
                    customer.setFirstName(res.getString(4));
                    customer.setCity(res.getString(5));
                    customer.setState(res.getString(6));
                    customer.setEmail(res.getString(7));
                    customer.setZipCode(res.getInt(8));
                    customers.add(customer);
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
			Customer customer = new Customer();
			customer.setCustomerID("111-11-1111");
			customer.setAddress("123 Success Street");
			customer.setLastName("Lu");
			customer.setFirstName("Shiyong");
			customer.setCity("Stony Brook");
			customer.setState("NY");
			customer.setEmail("shiyong@cs.sunysb.edu");
			customer.setZipCode(11790);
			customers.add(customer);			
		}
		*/
		/*Sample data ends*/
		
		return customers;

	}


	public String addCustomer(Customer customer) {

		/*
		 * All the values of the add customer form are encapsulated in the customer object.
		 * These can be accessed by getter methods (see Customer class in model package).
		 * e.g. firstName can be accessed by customer.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database insertion of the customer details and return "success" or "failure" based on result of the database insertion.
		 */

		//prepare the sql
        //update person table
        String person_sql = "INSERT INTO Person\n" +
                            "(ID, LastName, FirstName, Address, City, State, ZipCode, Telephone, Email)\n" +
                            "VALUES ";

        String person_val = String.format(" ('%s', '%s', '%s', '%s', '%s', '%s', %d, '%s', '%s')",
                                    customer.getCustomerID(), customer.getLastName(), customer.getFirstName(),
                                    customer.getAddress(), customer.getCity(), customer.getState(), customer.getZipCode(),
                                    customer.getTelephone(), customer.getEmail());
        //update customer table
        String customer_sql = "INSERT INTO Customer\n" +
                              "(CustomerID, CreditCardNum, ItemsSold, ItemsPurchased, Rating)\n" +
                              "VALUES ";
        String customer_val = String.format(" ('%s', '%s', 0, 0, %d)",
                                            customer.getCustomerID(), customer.getCreditCard(), customer.getRating());

        //System.out.println(person_sql + person_val);
        //System.out.println(customer_sql + customer_val);

        Query query = new Query();
        //update person table
        boolean res1 = query.update(person_sql + person_val);
        //update customer table
        boolean res2 = query.update(customer_sql + customer_val);

        if (res1 && res2) {
            return "success";
        } else {
            return "fail";
        }

		/*Sample data begins*/
		//return "success";
		/*Sample data ends*/

	}

	public String editCustomer(Customer customer) {
		/*
		 * All the values of the edit customer form are encapsulated in the customer object.
		 * These can be accessed by getter methods (see Customer class in model package).
		 * e.g. firstName can be accessed by customer.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database update and return "success" or "failure" based on result of the database update.
		 */

        //prepare the sql
        //String sql = "UPDATE Customer SET Rating = 65 WHERE CustomerID = '" + customer.getCustomerID() + "'";
        String sql = "UPDATE Customer SET ";
        String val = String.format("Email = '%s', FirstName = '%s', LastName = '%s', Address = '%s', City = '%s', " +
                                   "State = '%s', ZipCode = %d, Telephone = '%s', CreditCardNum = '%s', Rating = %d",
                                    customer.getEmail(), customer.getFirstName(), customer.getLastName(), customer.getAddress(),
                                    customer.getCity(), customer.getState(), customer.getZipCode(), customer.getTelephone(),
                                    customer.getCreditCard(), customer.getRating());
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

}
