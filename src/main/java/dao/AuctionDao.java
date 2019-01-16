package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Auction;
import model.Bid;
import model.Customer;
import model.Item;

public class AuctionDao {
	
	public List<Auction> getAllAuctions() {
		
		List<Auction> auctions = new ArrayList<Auction>();
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Each record is required to be encapsulated as a "Auction" class object and added to the "auctions" ArrayList
		 * Query to get data about all the auctions should be implemented
		 */

		//init a query object by default
		Query query = new Query();
		try {
			//prepare the sql
			String sql = "SELECT * FROM Auction";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate the result set
				while (res.next()) {
					//encapsulate the result
					Auction auction = new Auction();
					auction.setAuctionID(res.getInt(1));
					auction.setBidIncrement(res.getFloat(2));
					auction.setMinimumBid(res.getFloat(3));
					auction.setCopiesSold(res.getInt(4));
					auction.setItemID(res.getInt(6));
					auction.setClosingBid(120);
					auction.setCurrentBid(120);
					auction.setCurrentHighBid(120);
					auction.setReserve(res.getInt(7));
					auctions.add(auction);
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
			Auction auction = new Auction();
			auction.setAuctionID(1);
			auction.setBidIncrement(10);
			auction.setMinimumBid(10);
			auction.setCopiesSold(12);
			auction.setItemID(1234);
			auction.setClosingBid(120);
			auction.setCurrentBid(120);
			auction.setCurrentHighBid(120);
			auction.setReserve(10);
			auctions.add(auction);
		}
		*/
		/*Sample data ends*/
		
		return auctions;

	}

	public List<Auction> getAuctions(String customerID) {
		
		List<Auction> auctions = new ArrayList<Auction>();
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Each record is required to be encapsulated as a "Auction" class object and added to the "auctions" ArrayList
		 * Query to get data about all the auctions in which a customer participated should be implemented
		 * customerID is the customer's primary key, given as method parameter
		 */

		//init a query object by default
		Query query = new Query();
		try {
			//prepare the sql
			String sql = String.format("SELECT a.* from Auction a, Bid b where a.AuctionID = b.AuctionID and b.CustomerID = '%s';", customerID);
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate the result set
				while (res.next()) {
					//encapsulate the result
					Auction auction = new Auction();
					auction.setAuctionID(res.getInt(1));
					auction.setBidIncrement(res.getFloat(2));
					auction.setMinimumBid(res.getFloat(3));
					auction.setCopiesSold(res.getInt(4));
					auction.setItemID(res.getInt(6));
					auction.setClosingBid(120);
					auction.setCurrentBid(120);
					auction.setCurrentHighBid(120);
					auction.setReserve(res.getInt(7));
					auctions.add(auction);
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
		for (int i = 0; i < 5; i++) {
			Auction auction = new Auction();
			auction.setAuctionID(1);
			auction.setBidIncrement(10);
			auction.setMinimumBid(10);
			auction.setCopiesSold(12);
			auction.setItemID(1234);
			auction.setClosingBid(120);
			auction.setCurrentBid(120);
			auction.setCurrentHighBid(120);
			auction.setReserve(10);
			auctions.add(auction);
		}
		*/
		/*Sample data ends*/
		
		return auctions;

	}

	public List<Auction> getOpenAuctions(String employeeEmail) {

		//System.out.println(employeeEmail);

		List<Auction> auctions = new ArrayList<Auction>();
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Each record is required to be encapsulated as a "Auction" class object and added to the "auctions" ArrayList
		 * Query to get data about all the open auctions monitored by a customer representative should be implemented
		 * employeeEmail is the email ID of the customer representative, which is given as method parameter
		 */

		//init a query object by default
		Query query = new Query();
		try {
			//get the employee's id by his/her email address
			String employeeId = "";
			String getEmployeeID = String.format("SELECT ID FROM Person WHERE Email = '%s'", employeeEmail);
			ResultSet employeeRes = query.execute(getEmployeeID);
			if (employeeRes != null && employeeRes.next()) {
				employeeId = employeeRes.getString(1);
				employeeRes.close();
			}
			//prepare the sql
			String sql = String.format("SELECT A.*, W.currentBid, W.currentHighBid FROM Auction A, Post P, AuctionWinner W WHERE A.AuctionID = P.AuctionID AND " +
									   "P.ExpireDate <= now() AND Monitor = %s AND W.AuctionID = A.AuctionID", employeeId);
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate the result set
				while (res.next()) {
					//encapsulate the result
					Auction auction = new Auction();
					auction.setAuctionID(res.getInt(1));
					auction.setBidIncrement(res.getFloat(2));
					auction.setMinimumBid(res.getFloat(3));
					auction.setCopiesSold(res.getInt(4));
					auction.setItemID(res.getInt(6));
					//auction.setClosingBid(120);
					auction.setCurrentBid(res.getInt(8));
					auction.setCurrentHighBid(res.getInt(9));
					auction.setReserve(res.getInt(7));
					auctions.add(auction);
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
		for (int i = 0; i < 5; i++) {
			Auction auction = new Auction();
			auction.setAuctionID(1);
			auction.setBidIncrement(10);
			auction.setMinimumBid(10);
			auction.setCopiesSold(12);
			auction.setItemID(1234);
			auction.setClosingBid(120);
			auction.setCurrentBid(120);
			auction.setCurrentHighBid(120);
			auction.setReserve(10);
			auctions.add(auction);
		}
		*/
		/*Sample data ends*/
		
		return auctions;

		
		
	}

	public String recordSale(String auctionID) {
		/*
		 * The students code to update data in the database will be written here
		 * Query to record a sale, indicated by the auction ID, should be implemented
		 * auctionID is the Auction's ID, given as method parameter
		 * The method should return a "success" string if the update is successful, else return "failure"
		 */

		/*
		1. Increment the Copies_Sold by 1 in Auction table;
		2. Increment the Item’s Sold by 1 ;
		3. Decrement the Item’s NumCopies by 1;
		4. Increment the Customer’s ItemsPurchased by 1;
		5. Increment the ItemsSold for buyer by 1.
		*/
		String sql1 = String.format("UPDATE Auction SET Copies_Sold = Copies_Sold + 1 WHERE auctionID = %s;", auctionID);
		String sql2 = String.format("UPDATE Item as I, Auction as A SET Sold = Sold + 1 WHERE A.AuctionID = %s AND I.ItemID = A.ItemID", auctionID);
		String sql3 = String.format("UPDATE Item as I, Auction as A SET NumCopies = NumCopies - 1 WHERE A.AuctionID = %s AND I.ItemID = A.ItemID", auctionID);
		String sql4 = String.format("UPDATE Customer as C, AuctionWinner as W " +
									"SET C.ItemsPurchased = C.ItemsPurchased + 1 " +
									"WHERE C.CustomerID = W.Winner AND W.AuctionID = %s", auctionID);
		String sql5 = String.format("UPDATE Customer as C, Post as P, Auction as A " +
									"SET C.ItemsSold = C.ItemsSold + 1 " +
									"WHERE C.CustomerID = P.CustomerID and A.AuctionID = P.AuctionID and A.AuctionID = %s;", auctionID);
		//init the query
		Query query = new Query();
		//execute the 5 sql
		boolean res1 = query.update(sql1);
		boolean res2 = query.update(sql2);
		boolean res3 = query.update(sql3);
		boolean res4 = query.update(sql4);
		boolean res5 = query.update(sql5);

		if (res1 && res2 && res3 && res4 && res5) {
			return "success";
		} else {
			return "fail";
		}

		/* Sample data begins */
		//return "success";
		/* Sample data ends */
	}

	public List getAuctionData(String auctionID, String itemID) {
		
		List output = new ArrayList();
		Item item = new Item();
		Bid bid = new Bid();
		Auction auction = new Auction();
		Customer customer = new Customer();
		
		/*
		 * The students code to fetch data from the database will be written here
		 * The item details are required to be encapsulated as a "Item" class object
		 * The bid details are required to be encapsulated as a "Bid" class object
		 * The auction details are required to be encapsulated as a "Auction" class object
		 * The customer details are required to be encapsulated as a "Customer" class object
		 * Query to get data about auction indicated by auctionID and itemID should be implemented
		 * auctionID is the Auction's ID, given as method parameter
		 * itemID is the Item's ID, given as method parameter
		 * The customer details must include details about the current winner of the auction
		 * The bid details must include details about the current highest bid
		 * The item details must include details about the item, indicated by itemID
		 * The auction details must include details about the item, indicated by auctionID
		 * All the objects must be added in the "output" list and returned
		 */

		//init a query object by default
		Query query = new Query();
		try {
			//check if it is the first bid
			String checkbid_sql = String.format("select * from bid where auctionid = %s", auctionID);
			//auction without bidding history
			String nobid_sql = String.format("select i.itemid, i.description, i.type, i.name, a.minimubid, a.bidincrement " +
											 "from item i, auction a where a.auctionID = %s AND a.itemid = %s and a.itemid = i.itemid", auctionID, itemID);
			//prepare the sql
			String full_sql = String.format("select i.itemid, i.description, i.type, i.name," +
										    " w.winner, w.currentbid, p.id, p.firstname, p.lastname," +
											" a.minimubid, a.bidincrement, w.currentbid, w.currenthighbid " +
											"from auctionwinner w, item i, person p, customer c, auction a " +
											"where w.auctionid = %s and i.itemid = %s and a.itemid = i.itemid and " +
											"p.id = c.customerid and w.winner = c.customerid;", auctionID, itemID);
			//execute the sql
			ResultSet res1 = query.execute(checkbid_sql);
			if (res1 != null) {
				ResultSet res2;
				//there is bid of that auction
				if (res1.next()) {
					res2 = query.execute(full_sql);
					//iterate the result set
					if (res2 != null && res2.next()) {
						//encapsulate the result
						item.setItemID(res2.getInt(1));
						item.setDescription(res2.getString(2));
						item.setType(res2.getString(3));
						item.setName(res2.getString(4));

						bid.setCustomerID(res2.getString(5));
						bid.setBidPrice(res2.getInt(6));

						customer.setCustomerID(res2.getString(5));
						customer.setFirstName(res2.getString(8));
						customer.setLastName(res2.getString(9));

						auction.setMinimumBid(res2.getInt(10));
						auction.setBidIncrement(res2.getInt(11));
						auction.setCurrentBid(res2.getInt(12));
						auction.setCurrentHighBid(res2.getInt(13));
						auction.setAuctionID(Integer.parseInt(auctionID));
					}
				} else {
					res2 = query.execute(nobid_sql);
					if (res2 != null && res2.next()) {
						//encapsulate the result
						item.setItemID(res2.getInt(1));
						item.setDescription(res2.getString(2));
						item.setType(res2.getString(3));
						item.setName(res2.getString(4));

						auction.setMinimumBid(res2.getInt(5));
						auction.setBidIncrement(res2.getInt(6));
						auction.setAuctionID(Integer.parseInt(auctionID));
					}
				}
				//clean up the result set
				res1.close();
				res2.close();
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
		for (int i = 0; i < 4; i++) {
			item.setItemID(123);
			item.setDescription("sample description");
			item.setType("BOOK");
			item.setName("Sample Book");
			
			bid.setCustomerID("123-12-1234");
			bid.setBidPrice(120);
			
			customer.setCustomerID("123-12-1234");
			customer.setFirstName("Shiyong");
			customer.setLastName("Lu");
			
			auction.setMinimumBid(100);
			auction.setBidIncrement(10);
			auction.setCurrentBid(110);
			auction.setCurrentHighBid(115);
			auction.setAuctionID(Integer.parseInt(auctionID));
		}
		*/
		/*Sample data ends*/
		
		output.add(item);
		output.add(bid);
		output.add(auction);
		output.add(customer);
		
		return output;

	}

	
}
