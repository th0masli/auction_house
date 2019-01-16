package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import model.Item;
import model.Post;

public class PostDao {


    public List<Item> getSalesReport(Post post) {

        /*
         * The students code to fetch data from the database will be written here
         * Each record is required to be encapsulated as a "Item" class object and added to the "items" List
         * Query to get sales report for a particular month must be implemented
         * post, which has details about the month and year for which the sales report is to be generated, is given as method parameter
         * The month and year are in the format "month-year", e.g. "10-2018" and stored in the expireDate attribute of post object
         * The month and year can be accessed by getter method, i.e., post.getExpireDate()
         */
        if (post != null) {
            List<Item> items = new ArrayList<Item>();
            Query query = new Query();

            try {
                // expireDateSplit[0] = month and expireDateSplit[1] = year
                int dashIndex = post.getExpireDate().indexOf('-');
                String sqlDate = String.format("%s-%s", post.getExpireDate().substring(dashIndex + 1),
                        post.getExpireDate().substring(0, dashIndex));
                String sql = String.format("SELECT Item.Name, WinningPrice.BidPrice FROM Item, Auction, WinningPrice, " +
                        "Post WHERE Auction.ItemId = Item.ItemID AND Auction.AuctionID = WinningPrice.AuctionID " +
                        "AND Auction.AuctionID = Post.AuctionID " +
                        "AND Post.ExpireDate <= '%s-31' AND Post.ExpireDate >= '%s-01';", sqlDate, sqlDate);
                System.out.println("getSalesReport sql query: " + sql);

                ResultSet res = query.execute(sql);

                if (res != null) {
                    while (res.next()) {
                        Item item = new Item();
                        // Column 1 is the item name
                        item.setName(res.getString(1));
                        // Column 2 is WinningPrice.BidPrice
                        item.setSoldPrice(Double.parseDouble(res.getString(2)));
                        items.add(item);
                    }
                    //clean up the result set
                    res.close();
                }
            } catch (SQLException se) {
                System.err.println("Error in res.getString() - unable to get tuple attribute.");
            } finally{
                //finally block used to close resources
                try {
                    if(query.statement != null)
                        query.statement.close();
                } catch(SQLException se2) {
                    System.err.println("Unable to close query statement");
                }// nothing we can do
                try {
                    if (query.connection != null)
                        query.connection.close();
                } catch (SQLException se){
                    System.err.println("Unable to close query connection");
                }//end finally try
            }//end try

            return items;
        }

        // Unable to retrieve items - return null
        return null;

    }
}
