package dao;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Auction;
import model.Bid;
import model.Item;

public class ItemDao {

	private static final String winning_price_view =
			"CREATE OR REPLACE VIEW" +
					" WinningPrice (AuctionID, BidPrice) AS" +
					" SELECT DISTINCT Bid.AuctionID, MAX(Bid.BidPrice) " +
					" FROM Bid, Post" +
					" WHERE Bid.AuctionID = Post.AuctionID" +
					" AND Post.ExpireDate <= NOW()" +
					" GROUP BY Bid.auctionID;";

	private static final String Auction_winner_view =
			"CREATE OR REPLACE VIEW" +
					" AuctionWinner (AuctionID, Winner) AS" +
					" SELECT b1.AuctionID, b1.CurrentWinner" +
					" FROM bid b1 LEFT OUTER JOIN bid b2" +
					" ON b1.auctionID = b2.auctionID" +
					" AND b1.bidTime < b2.bidTime" +
					" WHERE b2.AuctionID IS NULL;";

	private static final String Item_type_winner_view =
			"CREATE OR REPLACE VIEW" +
					" ItemTypeWinner (ItemId, type, winner) AS" +
					" SELECT I.ItemId, I.Type, W.winner" +
					" FROM Item AS I, Auction AS a, AuctionWinner AS W" +
					" WHERE I.itemId = A.itemID" +
					" AND A.AuctionID = W.AuctionID;";

	private void create_winning_price_view() {

		Query query = new Query();
		//Query query = new Query();

		try {
			//execute the sql
			ResultSet res = query.execute(winning_price_view);
			if (res != null) {
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

	};
	private void create_auction_winner_view() {

		Query query = new Query();
		//Query query = new Query();

		try {
			//execute the sql
			ResultSet res = query.execute(Auction_winner_view);
			if (res != null) {
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

	};
	private void create_item_type_winner_view() {

		Query query = new Query();
		//Query query = new Query();

		try {
			//execute the sql
			ResultSet res = query.execute(Item_type_winner_view);
			if (res != null) {
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

	};

	public static Auction getAuctionByAuctionID(String auctionID) {

		Auction auction = new Auction();

		Query query = new Query();
		//Query query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT Auction.* FROM Auction" +
					" WHERE Auction.auctionID = " +"'" + auctionID + "'";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					auction.setAuctionID(res.getInt(1));
					auction.setBidIncrement(res.getFloat(2));
					auction.setMinimumBid(res.getFloat(3));
					auction.setCopiesSold(res.getInt(4));
					auction.setMonitor(res.getInt(5));
					auction.setItemID(res.getInt(6));
					auction.setReserve(res.getInt(7));
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

		return auction;
	}

	private Bid getBidByAuctionID_BidPrice(String auctionID, float price) {

		Bid bid = new Bid();

		Query query = new Query();
		//Query query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT * FROM BID " +
					"WHERE Bid.auctionID = " +"'" + auctionID + "' " +
					"AND Bid.bidPrice = " + price;
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					bid.setCustomerID(res.getString(1));
					bid.setAuctionID(res.getInt(2));
					bid.setBidTime(res.getString(3));
					bid.setMaxBid(res.getFloat(4));
					bid.setBidPrice(res.getFloat(5));
					bid.setCurrentWinner(res.getString(6));
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

		return bid;
	}

	//done
	public List<Item> getItems() {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to fetch details of all the items has to be implemented
		 * Each record is required to be encapsulated as a "Item" class object and added to the "items" List
		 */

		List<Item> items = new ArrayList<Item>();

		Query query = new Query();
		//Query query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT * FROM ITEM";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					Item item = new Item();
					item.setItemID(res.getInt(1));
					item.setDescription(res.getString(2));
					item.setName(res.getString(3));
					item.setType(res.getString(4));
					item.setYearManufactured(res.getInt(5));
					item.setSoldPrice(res.getInt(6));
					item.setNumCopies(res.getInt(7));
					items.add(item);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
		
		return items;

	}

	//done
	public Item getItemByAuctionID(String AuctionID) {
		Query query = new Query();
		//Query query = new Query();
		Item item = new Item();

		try {
			//prepare the sql
			String sql = "SELECT ITEM.* FROM AUCTION NATURAL JOIN ITEM" +
					" WHERE AUCTION.auctionid = " + "'"+AuctionID+"'";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {

					item.setItemID(res.getInt(1));
					item.setDescription(res.getString(2));
					item.setName(res.getString(3));
					item.setType(res.getString(4));
					item.setYearManufactured(res.getInt(5));
					item.setSoldPrice(res.getInt(6));
					item.setNumCopies(res.getInt(7));
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

		return item;
	}

	//done
	public List<Item> getBestsellerItems() {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to fetch details of the bestseller items has to be implemented
		 * Each record is required to be encapsulated as a "Item" class object and added to the "items" List
		 */

		List<Item> items = new ArrayList<>();

		Query query = new Query();
		//Query query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT * FROM item ORDER BY (Sold) DESC";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					Item item = new Item();
					item.setItemID(res.getInt(1));
					item.setDescription(res.getString(2));
					item.setName(res.getString(3));
					item.setType(res.getString(4));
					item.setYearManufactured(res.getInt(5));
					item.setSoldPrice(res.getInt(6));
					item.setNumCopies(res.getInt(7));
					items.add(item);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
		
		return items;

	}

	//done
	public List<Item> getSummaryListing(String searchKeyword) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to fetch details of summary listing of revenue generated by a particular item or item type must be implemented
		 * Each record is required to be encapsulated as a "Item" class object and added to the "items" ArrayList
		 * Store the revenue generated by an item in the soldPrice attribute, using setSoldPrice method of each "item" object
		 */

		Query query = new Query();
		List<Item> items = new ArrayList<Item>();

		//create_winning_price_view();

		//search by item name
		try {
			//prepare the sql
			String sql = "SELECT Item.*, WinningPrice.bidPrice" +
					" FROM Item, WinningPrice, Auction" +
					" WHERE ITEM.name LIKE '%" + searchKeyword + "%'" +
					" AND WinningPrice.auctionId = auction.auctionId" +
					" AND Auction.ItemId = Item.ItemId;";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					Item item = new Item();
					item.setItemID(res.getInt(1));
					item.setDescription(res.getString(2));
					item.setName(res.getString(3));
					item.setType(res.getString(4));
					item.setYearManufactured(res.getInt(5));
					item.setSoldPrice(res.getInt(8));
					item.setNumCopies(res.getInt(7));
					items.add(item);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try


		//search by TYPE
		query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT Item.*, WinningPrice.bidPrice" +
					" FROM ITEM, Auction, WinningPrice" +
					" WHERE Auction.itemID = item.itemID" +
					" AND Auction.auctionID = WinningPrice.auctionID" +
					" AND ITEM.type ='" + searchKeyword + "';";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					Item item = new Item();
					item.setItemID(res.getInt(1));
					item.setDescription(res.getString(2));
					item.setName(res.getString(3));
					item.setType(res.getString(4));
					item.setYearManufactured(res.getInt(5));
					item.setSoldPrice(res.getInt(8));
					item.setNumCopies(res.getInt(7));
					items.add(item);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try


		//search by customer name (seller Name)
		query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT Item.*, WinningPrice.bidPrice FROM WinningPrice, Auction, Item, Post, Person" +
					" WHERE WinningPrice.AuctionID = Auction.AuctionID" +
					" AND Auction.ItemID = Item.ItemID" +
					" AND WinningPrice.AuctionID = Post.AuctionID" +
					" AND Person.ID = Post.CustomerID" +
					" AND (Person.FirstName LIKE '%" + searchKeyword + "'" +
					" OR Person.LastName LIKE '%" + searchKeyword + "');";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					Item item = new Item();
					item.setItemID(res.getInt(1));
					item.setDescription(res.getString(2));
					item.setName(res.getString(3));
					item.setType(res.getString(4));
					item.setYearManufactured(res.getInt(5));
					item.setSoldPrice(res.getInt(8));
					item.setNumCopies(res.getInt(7));
					items.add(item);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
		
		return items;

	}

	//done
	public List<Item> getItemSuggestions(String customerID) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to fetch item suggestions for a customer, indicated by customerID, must be implemented
		 * customerID, which is the Customer's ID for whom the item suggestions are fetched, is given as method parameter
		 * Each record is required to be encapsulated as a "Item" class object and added to the "items" ArrayList
		 */

		List<Item> items = new ArrayList<Item>();


		//create or update the following views before query
		//create_auction_winner_view();
		//create_item_type_winner_view();

		Query query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT I.* FROM Item AS I, ItemTypeWinner AS T" +
					" WHERE I.type = T.Type AND I.NumCopies > 0" +
					" AND T.Winner = '" + customerID + "';";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					Item item = new Item();
					item.setItemID(res.getInt(1));
					item.setDescription(res.getString(2));
					item.setName(res.getString(3));
					item.setType(res.getString(4));
					item.setYearManufactured(res.getInt(5));
					//item.setSoldPrice(res.getInt(6));
					item.setNumCopies(res.getInt(7));
					items.add(item);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

		return items;

	}

	//done
	public List getItemsBySeller(String sellerID) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to fetch items sold by a given seller, indicated by sellerID, must be implemented
		 * sellerID, which is the Sellers's ID who's items are fetched, is given as method parameter
		 * The bid and auction details of each of the items should also be fetched
		 * The bid details must have the highest current bid for the item
		 * The auction details must have the details about the auction in which the item is sold
		 * Each item record is required to be encapsulated as a "Item" class object and added to the "items" List
		 * Each bid record is required to be encapsulated as a "Bid" class object and added to the "bids" List
		 * Each auction record is required to be encapsulated as a "Auction" class object and added to the "auctions" List
		 * The items, bids and auctions Lists have to be added to the "output" List and returned
		 */

		List output = new ArrayList();
		List<Item> items = new ArrayList<>();
		List<Bid> bids = new ArrayList<>();
		List<Auction> auctions = new ArrayList<>();

		Query query = new Query();

		try {

			//creates winningPrice view
			//create_winning_price_view();

			//prepare the sql
			String sql = "SELECT WinningPrice.AuctionID, sum(WinningPrice.BidPrice)" +
					" FROM WinningPrice, Post" +
					" WHERE Post.CustomerID = '" + sellerID + "'" +
					" AND Post.AuctionID = WinningPrice.AuctionID" +
					" GROUP BY WinningPrice.AuctionID";
			//System.out.println(sql);
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					String auctionID = res.getString(1);
					Item item = getItemByAuctionID(auctionID);
					items.add(item);

					float price = res.getFloat(2);
					Bid bid = getBidByAuctionID_BidPrice(auctionID, price);
					bids.add(bid);

					Auction auction = getAuctionByAuctionID(auctionID);
					auctions.add(auction);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
		
		output.add(items);
		output.add(bids);
		output.add(auctions);
		
		return output;
	}

	//done
	public List getItemsByName(String itemName) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * The itemName, which is the item's name on which the query has to be implemented, is given as method parameter
		 * Query to fetch items containing itemName in their name has to be implemented
		 * Each item's corresponding auction data also has to be fetched
		 * Each item record is required to be encapsulated as a "Item" class object and added to the "items" List
		 * Each auction record is required to be encapsulated as a "Auction" class object and added to the "auctions" List
		 * The items and auctions Lists are to be added to the "output" List and returned
		 */

		List output = new ArrayList();
		List<Item> items = new ArrayList<Item>();
		List<Auction> auctions = new ArrayList<Auction>();



		Query query = new Query();
		//Query query = new Query();

		try {
			//creates winningPrice view
			//create_winning_price_view();

			//prepare the sql
			String sql = "SELECT Auction.AuctionID" +
					" FROM Auction, Item, Post" +
					" WHERE Auction.ItemID = Item.itemID" +
					" AND Auction.auctionID = Post.auctionID" +
					" AND Item.name LIKE '%" +itemName + "%'" +
					" AND Post.ExpireDate > NOW()" +
					" GROUP BY Auction.auctionID";
			//execute the sql
			//System.out.println(sql);
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					String auctionID = res.getString(1);
					Item item = getItemByAuctionID(auctionID);
					items.add(item);

					Auction auction = getAuctionByAuctionID(auctionID);
					auctions.add(auction);
					//System.out.println(auction.getAuctionID());
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
		
		output.add(items);
		output.add(auctions);
		
		return output;
	}

	//done
	public List getItemsByType(String itemType) {
		
		/*
		 * The students code to fetch data from the database will be written here
		 * The itemType, which is the item's type on which the query has to be implemented, is given as method parameter
		 * Query to fetch items containing itemType as their type has to be implemented
		 * Each item's corresponding auction data also has to be fetched
		 * Each item record is required to be encapsulated as a "Item" class object and added to the "items" List
		 * Each auction record is required to be encapsulated as a "Auction" class object and added to the "auctions" List
		 * The items and auctions Lists are to be added to the "output" List and returned
		 */

		List output = new ArrayList();
		List<Item> items = new ArrayList<Item>();
		List<Auction> auctions = new ArrayList<Auction>();

		//creates winningPrice view
		//create_winning_price_view();

		Query query = new Query();
		//Query query = new Query();

		try {
			//prepare the sql
			String sql = "SELECT auction.auctionID" +
					" FROM Auction, post, item" +
					" WHERE Auction.auctionId = post.auctionId" +
					" AND auction.itemID = Item.itemID" +
					" AND post.ExpireDate > NOW()" +
					" AND Item.type = '"+itemType+"';";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					String auctionID = res.getString(1);
					Item item = getItemByAuctionID(auctionID);
					items.add(item);
					//System.out.println(item.getItemID());
					//System.out.println(item.getName());

					Auction auction = getAuctionByAuctionID(auctionID);
					auctions.add(auction);
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch(SQLException se2){}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
		
		output.add(items);
		output.add(auctions);
		
		return output;
	}

	//wrapped List<Item> getItemSuggestion(String customerID)
	public List<Item> getBestsellersForCustomer(String customerID) {

		/*
		 * The students code to fetch data from the database will be written here.
		 * Each record is required to be encapsulated as a "Item" class object and added to the "items" ArrayList.
		 * Query to get the Best-seller list of items for a particular customer, indicated by the customerID, has to be implemented
		 * The customerID, which is the customer's ID for whom the Best-seller items have to be fetched, is given as method parameter
		 */
		
		return getItemSuggestions(customerID);

	}

	//TODO: what for ?

	public List<Item> getItemTypes() {

		/*
		 * The students code to fetch data from the database will be written here
		 * Each record is required to be encapsulated as a "Item" class object and added to the "items" ArrayList
		 * A query to fetch the unique item types has to be implemented
		 * Each item type is to be added to the "item" object using setType method
		 */
		List<Item> items = new ArrayList<Item>();
		ArrayList<String> types = new ArrayList<>();
		Query query = new Query();
		//Query query = new Query();

		try {
			//creates winningPrice view
			//create_winning_price_view();

			//prepare the sql
			String sql = "SELECT DISTINCT Item.type FROM ITEM;";
			//execute the sql
			ResultSet res = query.execute(sql);
			if (res != null) {
				//iterate through result
				while (res.next()) {
					types.add(res.getString(1));
				}
				//clean up the result set
				res.close();
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			//finally block used to close resources
			try {
				if (query.statement != null)
					query.statement.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (query.connection != null)
					query.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

		for (int i = 0; i < types.size(); i++) {

			query = new Query();

			String type = types.get(i);
			try {
				//creates winningPrice view
				//create_winning_price_view();

				//prepare the sql
				String sql = "SELECT * FROM ITEM" +
						" WHERE Item.type = '" + type + "'" +
						" ORDER BY ItemID DESC LIMIT 1;";
				//execute the sql
				ResultSet res = query.execute(sql);
				if (res != null) {
					//iterate through result
					while (res.next()) {
						Item item = new Item();
						item.setItemID(res.getInt(1));
						item.setDescription(res.getString(2));
						item.setName(res.getString(3));
						item.setType(res.getString(4));
						item.setYearManufactured(res.getInt(5));
						item.setSoldPrice(res.getInt(6));
						item.setNumCopies(res.getInt(7));
						items.add(item);
					}
					//clean up the result set
					res.close();
				}
			} catch (SQLException se) {
				//Handle errors for JDBC
				se.printStackTrace();
			} catch (Exception e) {
				//Handle errors for Class.forName
				e.printStackTrace();
			} finally {
				//finally block used to close resources
				try {
					if (query.statement != null)
						query.statement.close();
				} catch (SQLException se2) {
				}// nothing we can do
				try {
					if (query.connection != null)
						query.connection.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}//end finally try
			}//end try
		}


		return items;
	}

}
