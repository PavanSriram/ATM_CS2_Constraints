package com.company;

import java.sql.*;

public class database {

    private Connection connection;
    Statement state;
    static int acc = 10016;

    //method to display the Customer information in the database -- can be accessible by the admin only
    public void displayUsers() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM useracc");
        while(res.next()){
            System.out.println(res.getInt("Account_Number") + "----" + res.getString("fName") + "----" + res.getLong("Balance") +
                    "----" + res.getLong("Phone_Number") + "----" + res.getString("IFSC"));
        }
        con.close();
    }

    //creates connection to the database
    public void getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        //instantiating the connection object
        connection = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");
        initialise();
    }

    //initialises the database with some predefined values in case when the table and the data does not exist already
    private void initialise() throws SQLException {

        state = connection.createStatement();
        ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='useracc'");

        if( !res.next() ){
            System.out.println("Filling the data base with default values...");

            state.execute("CREATE TABLE useracc (id integer," + "Account_Number integer," + "fName varchar(20)," +
                     "PIN integer," + "Balance bigint, " + "Phone_Number bigint," +"IFSC varchar(12)," + "primary key(id));");

            state.executeUpdate("INSERT INTO useracc VALUES (1, 10000, 'Steve Rogers', 10000, 50000, 9876543210, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (2, 10001, 'Tony Stark', 10001, 50000, 9632587410, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (3, 10002, 'Peter Parker', 10002, 50000, 9874561230, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (4, 10003, 'Bruce Banner', 10003, 50000, 9876543210, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (5, 10004, 'Natasha', 10004, 50000, 9632587410, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (6, 10005, 'Nick Fury', 10005, 50000, 9874561230, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (7, 10006, 'Happy', 10006, 50000, 9876543210, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (8, 10007, 'Sam Wilson', 10007, 50000, 9632587410, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (9, 10008, 'Strange', 10008, 50000, 9874561230, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (10, 10009, 'Clint', 10009, 50000, 9876543210, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (11, 10010, 'Rhodes', 10010, 50000, 9632587410, 'sbin0000000');");
            state.executeUpdate("INSERT INTO useracc VALUES (12, 10011, 'Anthony Russo', 10011, 50000, 9874561230, 'sbin0000000');");
        }
        connection.close();
    }

    //method to add a new customer into the database -- only accessible by admin
    public void addUser(String name, long phone, long bal, String ifsc) throws SQLException{

        if(acc > 99999){
            System.out.println("Sorry Maximum customer limit exceeded....");
            System.out.println("Unable to add the customer");
            return;
        }
         Connection connection = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");
         PreparedStatement prep = connection.prepareStatement("INSERT INTO useracc VALUES (?, ?, ?, ?, ?, ?, ?);");

        int pin;
         while(true) {
             pin = (int) (Math.random() * 100000);
             if( 10000 <= pin && pin <= 99999 ){
                 break;
             }
         }
         prep.setInt(2, acc++);
         prep.setString(3, name);
         prep.setInt(4, pin);
         prep.setLong(5, bal);
         prep.setLong(6, phone);
         prep.setString(7, ifsc);
         prep.execute();

        System.out.println("Account added successfully!....");
        connection.close();
    }
}
