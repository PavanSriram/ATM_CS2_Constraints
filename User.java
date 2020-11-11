package com.company;

import java.sql.*;
import java.util.Scanner;

public class User {

    Scanner sc = new Scanner(System.in);
    User_Account u;

    //all the information of the result set is duplicated into user_account object
    User(ResultSet res) throws SQLException {
        u = new User_Account(res.getInt("id"), res.getString("fName"), res.getInt("Account_Number"), res.getInt("PIN")
        , res.getLong("Balance"), res.getLong("Phone_Number"), res.getString("IFSC"));

    }

    //shows the current balance
    void view_bal() {
        System.out.println("Your Current Balance is: " + u.getBal());
    }

    //method to deposit funds to the customer's account
    void deposit() throws SQLException {
        System.out.println("Enter the Amount you need to deposit");
        long Amount = sc.nextLong();

        long ph = u.getPhone_no();
        if(!get_otp(ph)){
            System.out.println("Unsuccessful operation...");
            return ;
        }

        Connection con = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");

        int id = u.getId();
        PreparedStatement prep;
        prep = con.prepareStatement("DELETE FROM useracc WHERE id = ?");
        prep.setInt(1, id);
        prep.execute();
        prep = con.prepareStatement("INSERT INTO useracc VALUES(?, ?, ?, ?, ?, ?, ?);");

        prep.setInt(2, u.getAcc_no());
        prep.setString(3, u.getName());
        prep.setInt(4, u.getPin());
        //u.setId(++id);
        u.setBal(Amount);

        prep.setLong(5, u.getBal());
        prep.setLong(6, u.getPhone_no());
        prep.setString(7, u.getIFSC());
        prep.execute();

        change_id(con);

        System.out.println("Transaction Successful :)");
        ATM_CS.total_cash += Amount;
        view_bal();
        con.close();
    }

    //method to withdraw amount from the customer's account
    void withdraw() throws SQLException {
        System.out.println("Enter the Amount you need to withdraw");
        long Amount = sc.nextLong();

        long ph = u.getPhone_no();
        if(!get_otp(ph)){
            System.out.println("Unsuccessful operation...");
            return ;
        }

        //checks if the total cash inside the ATM is sufficient for the withdrawl
        if(Amount <= ATM_CS.total_cash) {
            //checks if the total cash inside the customer's account is more than the withdrawl amount
            if (Amount <= u.getBal()) {

                Connection con = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");

                int id = u.getId();
                PreparedStatement prep;
                prep = con.prepareStatement("DELETE FROM useracc WHERE id = ?");
                prep.setInt(1, id);
                prep.execute();

                prep = con.prepareStatement("INSERT INTO useracc VALUES(?, ?, ?, ?, ?, ?, ?);");
                prep.setInt(2, u.getAcc_no());
                prep.setString(3, u.getName());
                prep.setInt(4, u.getPin());
                if(!u.withdraw(Amount)){
                    con.close();
                    System.out.println("Sorry Transaction Unsuccessful!.... ");
                    System.out.println("Insufficient Funds!!!");
                    return;
                }
                //u.setId(++id);
                prep.setLong(5, u.getBal());
                prep.setLong(6, u.getPhone_no());
                prep.setString(7, u.getIFSC());
                prep.execute();

                change_id(con);

                System.out.println("Transaction Successful :)");
                ATM_CS.total_cash -= Amount;
                con.close();
            } else {
                System.out.println("Sorry Transaction Unsuccessful!.... ");
                System.out.println("Insufficient Funds!!!");
            }
            view_bal();
        }
        else{
            System.out.println("Insufficient cash in ATM");
        }
    }

    //transfers amount from one account to other account by taking account number and ifsc code of the other account
    void transfer(User_Account user2) throws SQLException {
        System.out.println("Enter the amount to transfer: ");
        long Amount = sc.nextLong();

        long ph = u.getPhone_no();
        if(!get_otp(ph)){
            System.out.println("Unsuccessful operation...");
            return ;
        }

        if (Amount <= user2.getBal()) {

            Connection con = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");

            PreparedStatement prep;
            int id = u.getId();
            prep = con.prepareStatement("DELETE FROM useracc WHERE id = ?");
            prep.setInt(1, id);
            prep.execute();

            prep = con.prepareStatement("INSERT INTO useracc VALUES(?, ?, ?, ?, ?, ?, ?);");
            prep.setInt(2, u.getAcc_no());
            prep.setString(3, u.getName());
            prep.setInt(4, u.getPin());
            if(!u.withdraw(Amount)){
                con.close();
                System.out.println("Sorry Transaction Unsuccessful!.... ");
                System.out.println("Insufficient Funds!!!");
                return;
            }
            prep.setLong(5, u.getBal());
            prep.setLong(6, u.getPhone_no());
            prep.setString(7, u.getIFSC());
            prep.execute();

            change_id(con);

            int id1 = user2.getId();
            prep = con.prepareStatement("DELETE FROM useracc WHERE id = ?");
            prep.setInt(1, id1);
            prep.execute();

            prep = con.prepareStatement("INSERT INTO useracc VALUES(?, ?, ?, ?, ?, ?, ?);");
            prep.setInt(2, user2.getAcc_no());
            prep.setString(3, user2.getName());
            prep.setInt(4, user2.getPin());
            user2.setBal(Amount);
            prep.setLong(5, user2.getBal());
            prep.setLong(6, user2.getPhone_no());
            prep.setString(7, user2.getIFSC());
            prep.execute();

            System.out.println("Transaction Successful :)");
            System.out.println(Amount + " transferred from account number " + u.getAcc_no() + " to account number " + user2.getAcc_no());
            con.close();
        } else {
            System.out.println("Sorry Transaction Unsuccessful!.... ");
            System.out.println("Insufficient Funds!!!");
        }
    }

    //method to change pin for the account
    public void change_pin() throws SQLException {
        System.out.println("Enter the New PIN: ");
        int new_pin = sc.nextInt();

        long ph = u.getPhone_no();

        if(!get_otp(ph)){
            System.out.println("Unsuccessful operation...");
            return ;
        }

        Connection con = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");

        int id = u.getId();
        PreparedStatement prep = con.prepareStatement("DELETE FROM useracc WHERE id=?");
        prep.setInt(1, id);
        prep.execute();

        u.setPin(new_pin);

        prep = con.prepareStatement("INSERT INTO useracc VALUES (?, ?, ?, ?, ?, ?, ?);");
        prep.setInt(2, u.getAcc_no());
        prep.setString(3, u.getName());
        prep.setInt(4, u.getPin());
        prep.setLong(5, u.getBal());
        prep.setLong(6, u.getPhone_no());
        prep.setString(7, u.getIFSC());
        prep.execute();

        change_id(con);

        System.out.println("Pin changed successfully");
        con.close();
    }

    //method to change the phone number
    public void change_ph() throws SQLException {
        System.out.println("Enter the New Phone Number: ");
        long new_ph = sc.nextLong();

        long ph = u.getPhone_no();

        if(!get_otp(ph)){
            System.out.println("Unsuccessful operation...");
            return ;
        }
        Connection con = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");

        int id = u.getId();
        PreparedStatement prep = con.prepareStatement("DELETE FROM useracc WHERE id=?");
        prep.setInt(1, id);
        prep.execute();

        u.setPhone_no(new_ph);

        prep = con.prepareStatement("INSERT INTO useracc VALUES (?, ?, ?, ?, ?, ?, ?);");
        prep.setInt(2, u.getAcc_no());
        prep.setString(3, u.getName());
        prep.setInt(4, u.getPin());
        prep.setLong(5, u.getBal());
        prep.setLong(6, u.getPhone_no());
        prep.setString(7, u.getIFSC());
        prep.execute();

        System.out.println("Phone Number changed successfully");

        change_id(con);
        con.close();
    }

    //all the transactions are proceeded only by validating the otp
    //method to generate otp and validate
    boolean get_otp(long ph){
        long hold = ph % 100;
        while(true) {
            int otp = (int)(Math.random()*10000);

            System.out.println("Please enter the new OTP sent to the old mobile number XXXXXXXX" + hold);
            System.out.println("Your OTP is " + otp);
            System.out.println("To resend OTP press 1");
            int input = sc.nextInt();

            if(otp == input)
                break;
            else if(input == 1){
                continue;
            }
            else{
                System.out.println("Sorry wrong OTP...");
                return false;
            }
        }
        return true;
    }

    //while updating the database when some transaction happens , this program identifies the customer with the id assigned to him in the database
    //this method updates the id of the customer after every transaction
    void change_id(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery("SELECT id, Account_Number FROM useracc");
        while( res.next() ){
            if(res.getInt("Account_Number") == u.getAcc_no()){
                break;
            }
        }

        u.setId(res.getInt("id"));
    }
}
