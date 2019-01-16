package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.*;
//import model.Customer;

public class BidDao {

    public List<Bid> getBidHistory(String auctionID) {


        /*
         * The students code to fetch data from the database
         * Each record is required to be encapsulated as a "Bid" class object and added to the "bids" ArrayList
         * auctionID, which is the Auction's ID, is given as method parameter
         * Query to get the bid history of an auction, indicated by auctionID, must be implemented
         */
        List<Bid> bids = new ArrayList<Bid>();
        if (auctionID != null) {
            Query query = new Query();

            try {
                String sql = String.format("SELECT * FROM BID WHERE AuctionID = %s;", auctionID);
                System.out.println("getBidHistory sql query: " + sql);

                ResultSet res = query.execute(sql);

                if (res != null) {
                    while (res.next()) {
                        Bid bid = new Bid();
                        bid.setAuctionID(Integer.parseInt(auctionID));
                        bid.setCustomerID(res.getString(1));
                        bid.setBidTime(res.getString(3));
                        bid.setBidPrice(res.getFloat(5));
                        bids.add(bid);
                    }
                    //clean up the result set
                    res.close();
                }
            } catch (SQLException se) {
                System.err.println("Error in res.getString() - unable to get tuple attribute.");
            } finally {
                //finally block used to close resources
                try {
                    if (query.statement != null)
                        query.statement.close();
                } catch (SQLException se2) {
                    System.err.println("Unable to close query statement");
                }// nothing we can do
                try {
                    if (query.connection != null)
                        query.connection.close();
                } catch (SQLException se) {
                    System.err.println("Unable to close query connection");
                }//end finally try
            }//end try
        }

        return bids;
    }

    public List<Bid> getAuctionHistory(String customerID) {

        List<Bid> bids = new ArrayList<Bid>();

        /*
         * The students code to fetch data from the database
         * Each record is required to be encapsulated as a "Bid" class object and added to the "bids" ArrayList
         * customerID, which is the Customer's ID, is given as method parameter
         * Query to get the bid history of all the auctions in which a customer participated, indicated by customerID, must be implemented
         */

        if (customerID != null) {
            Query query = new Query();

            try {
                String sql = String.format("SELECT * FROM Bid WHERE Bid.CustomerID = '%s';", customerID);
                System.out.println("getBidHistory sql query: " + sql);

                ResultSet res = query.execute(sql);

                if (res != null) {
                    while (res.next()) {
                        Bid bid = new Bid();
                        bid.setAuctionID(res.getInt(2));
                        bid.setCustomerID(customerID);
                        bid.setBidTime(res.getString(3));
                        bid.setBidPrice(res.getFloat(5));
                        bids.add(bid);
                    }
                    //clean up the result set
                    res.close();
                }
            } catch (SQLException se) {
                System.err.println("Error in getting tuple attributes.");
            } finally {
                //finally block used to close resources
                try {
                    if (query.statement != null)
                        query.statement.close();
                } catch (SQLException se2) {
                    System.err.println("Unable to close query statement.");
                }// nothing we can do
                try {
                    if (query.connection != null)
                        query.connection.close();
                } catch (SQLException se) {
                    System.err.println("Unable to close query connection");
                }//end finally try
            }//end try
        }

        return bids;
    }

    private Bid getCurrentWinner(String auctionID) {
        Bid winningBid = new Bid();

        if (auctionID != null) {
            Query query = new Query();
            try {
                String sql = "SELECT auctionWInner.Winner, auctionWinner.currentHighBid " +
                        " FROM auctionWinner" +
                        " WHERE auctionWinner.auctionId = '"+ auctionID + "';";
                System.out.println("getCurrentWinner sql query: " + sql);

                ResultSet res = query.execute(sql);

                if (res != null) {
                    while (res.next()) {
                        winningBid.setCurrentWinner(res.getString(1));
                        winningBid.setMaxBid(Float.parseFloat(res.getString(2)));
                        //winningBid.setBidPrice(Float.parseFloat(res.getString(3)));
                    }
                    //clean up the result set
                    res.close();
                }
            } catch (SQLException se) {
                System.err.println("Error in getting tuple attributes.");
            } finally {
                //finally block used to close resources
                try {
                    if (query.statement != null)
                        query.statement.close();
                } catch (SQLException se2) {
                    System.err.println("Unable to close query statement.");
                }// nothing we can do
                try {
                    if (query.connection != null)
                        query.connection.close();
                } catch (SQLException se) {
                    System.err.println("Unable to close query connection");
                }
            }
        }
        return winningBid;
    }


    public Bid submitBid(String auctionID, String itemID, Float currentBid, Float maxBid, String customerID) {

        /*
         * The students code to insert data in the database
         * auctionID, which is the Auction's ID for which the bid is submitted, is given as method parameter
         * itemID, which is the Item's ID for which the bid is submitted, is given as method parameter
         * currentBid, which is the Customer's current bid, is given as method parameter
         * maxBid, which is the Customer's maximum bid for the item, is given as method parameter
         * customerID, which is the Customer's ID, is given as method parameter
         * Query to submit a bid by a customer, indicated by customerID, must be implemented
         * After inserting the bid data, return the bid details encapsulated in "bid" object
         */
        // Create new bid object to return


        Bid currentWinner = getCurrentWinner(auctionID);
        Auction auction = ItemDao.getAuctionByAuctionID(auctionID);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = dtf.format(now);

        float min_bid = auction.getMinimumBid();
        float new_max_bid = maxBid;
        String new_current_winner = customerID;

        System.out.println("max_bid: " +  currentWinner.getMaxBid());
        System.out.println("old_winner: " +  currentWinner.getCurrentWinner());

        if (maxBid < min_bid || maxBid < currentWinner.getMaxBid()) {

            System.out.println("unsatisfied");
            new_max_bid = currentWinner.getMaxBid();
            new_current_winner = currentWinner.getCurrentWinner();

            Bid bid = new Bid();
            bid.setAuctionID(Integer.parseInt(auctionID));
            bid.setCustomerID(customerID);
            bid.setBidTime(formattedNow);
            bid.setBidPrice(currentBid);
            bid.setMaxBid(new_max_bid);
            bid.setCurrentWinner(new_current_winner);

            return bid;
        }


        String sql = String.format("INSERT INTO Bid VALUES ('%s', %s, '%s', %f, %f, '%s');", customerID, auctionID,
                formattedNow, new_max_bid, currentBid, new_current_winner);
        System.out.println("Sql insert command: " + sql);

        Query query = new Query();

        try {
            if (query.update(sql)) System.out.println("Update successful");
            else System.err.println("Unsuccessful update");
        } finally {
            //finally block used to close resources
            try {
                if (query.statement != null)
                    query.statement.close();
            } catch (SQLException se2) {
                System.err.println("Unable to close query statement.");
            }// nothing we can do
            try {
                if (query.connection != null)
                    query.connection.close();
            } catch (SQLException se) {
                System.err.println("Unable to close query connection");
            }
        }

        Bid bid = new Bid();
        bid.setAuctionID(Integer.parseInt(auctionID));
        bid.setCustomerID(customerID);
        bid.setBidTime(formattedNow);
        bid.setBidPrice(currentBid);
        bid.setMaxBid(new_max_bid);
        bid.setCurrentWinner(new_current_winner);

        return bid;
    }

    public List<Bid> getSalesListing(String searchKeyword) {
        /*
         * The students code to fetch data from the database
         * Each record is required to be encapsulated as a "Bid" class object and added to the "bids" ArrayList
         * searchKeyword, which is the search parameter, is given as method parameter
         * Query to produce a list of sales by item name or by customer name must be implemented
         * The item name or the customer name can be searched with the provided searchKeyword
         */

        List<Bid> bids = new ArrayList<>();

        if (searchKeyword != null) {
            String sql_item_name = String.format("SELECT Bid.* FROM Auction, WinningPrice, Item, Bid WHERE " +
                    "Auction.ItemID = Item.ItemID AND Auction.AuctionID = WinningPrice.AuctionID AND " +
                    "Bid.AuctionID = WinningPrice.AuctionID AND Bid.BidPrice = WinningPrice.BidPrice AND " +
                    "Item.Name LIKE '%%%s%%';", searchKeyword);
            String sql_customer_name = String.format("SELECT Bid.* FROM Bid, WinningPrice, Person WHERE " +
                    "Bid.CustomerID = Person.ID AND Bid.AuctionID = WinningPrice.AuctionID AND " +
                    "Bid.bidPrice = WinningPrice.BidPrice AND PERSON.firstName " +
                    "LIKE '%%%s%%' OR " +
                    "PERSON.LastName LIKE '%%%s%%';", searchKeyword, searchKeyword);
            System.out.println(String.format("sql_item_name query: %s", sql_item_name));
            System.out.println(String.format("sql_customer_name query: %s", sql_customer_name));
            Query query = new Query();

            try {
                // Two queries will be performed and results will be combined into one list
                ResultSet res_item_name = query.execute(sql_item_name);
                ResultSet res_customer_name = query.execute(sql_customer_name);

                if (res_item_name != null) {
                    while(res_item_name.next()) {
                        Bid bid = new Bid();
                        bid.setCustomerID(res_item_name.getString(1));
                        bid.setAuctionID(res_item_name.getInt(2));
                        bid.setBidTime(res_item_name.getString(3));
                        bid.setMaxBid(res_item_name.getFloat(4));
                        bid.setBidPrice(res_item_name.getFloat(5));
                        bid.setCurrentWinner(res_item_name.getString(6));
                        bids.add(bid);
                    }
                    res_item_name.close();
                }

                if (res_customer_name != null) {
                    while(res_customer_name.next()) {
                        Bid bid = new Bid();
                        bid.setCustomerID(res_customer_name.getString(1));
                        bid.setAuctionID(Integer.parseInt(res_customer_name.getString(2)));
                        bid.setBidTime(res_customer_name.getString(3));
                        bid.setMaxBid(res_customer_name.getFloat(4));
                        bid.setBidPrice(res_customer_name.getFloat(5));
                        bid.setCurrentWinner(res_customer_name.getString(6));
                        bids.add(bid);
                    }
                    res_customer_name.close();
                }
            } catch (SQLException se) {
                System.err.println("Error in getting tuple attributes.");
            } finally {
                //finally block used to close resources
                try {
                    if (query.statement != null)
                        query.statement.close();
                } catch (SQLException se2) {
                    System.err.println("Unable to close query statement.");
                }// nothing we can do
                try {
                    if (query.connection != null)
                        query.connection.close();
                } catch (SQLException se) {
                    System.err.println("Unable to close query connection");
                }
            }

        }

        return bids;
    }

}
