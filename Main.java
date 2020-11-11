package com.company;

import java.sql.SQLException;
import java.util.*;

public class Main {

    static database db;
    public static void main(String[] args) throws SQLException{

        Scanner sc = new Scanner(System.in);

        db = new database();
        //the database class can throw some exceptions and those will be caught by this try catch block
        try {
            db.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //Taking the password for ATM admin at the start of the program
        System.out.println("Enter the security password of the admin: ");
        long pwd = sc.nextLong();

        //taking the total cash as input
        System.out.println("Set the initial Cash Amount: ");
        long total_cash = sc.nextLong();

        //instantiating the ATM_CS class
        ATM_CS atm = new ATM_CS(pwd, total_cash);

        //Setting the ATM to the working mode
        atm.working();
    }
}
