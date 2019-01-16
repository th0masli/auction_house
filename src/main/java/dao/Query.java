package dao;

import java.sql.*;

public class Query {
    // init the connection and statement
    public Connection connection = null;
    public Statement statement = null;
    // JDBC driver name and database URL
    private String driver;
    private String url;
    //  Database credentials
    private String user;
    private String password;

    //constructor
    Query(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    //default constructor
    Query() {
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.url = "jdbc:mysql://localhost:3306/auction_house";
        this.user = "root";
        this.password = "";
    }

    //connect and query the database
    public ResultSet execute(String sql) {
        ResultSet res = null;
        try {
            Class.forName(driver);
            //get the connection
            connection = DriverManager.getConnection(url, user, password);
            //create statement
            statement = connection.createStatement();
            //execute sql statement
            res = statement.executeQuery(sql);
            //return the result set object
            return res;
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        //return null if exception occurs
        return res;
    }

    //connect and do updates database
    public boolean update(String sql) {
        //System.out.println(sql);
        boolean res = false;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            int countUpdated = statement.executeUpdate(sql);

            if (countUpdated > 0)
                res = true;

            return res;
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement!=null)
                    connection.close();
            } catch (SQLException se) {}// do nothing
            try {
                if (connection!=null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

        return res;
    }

}
