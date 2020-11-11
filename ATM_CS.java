package com.company;

import java.sql.*;
import java.util.Scanner;

public class ATM_CS {

    Connection conn;
    Statement stmt = null;
    ResultSet res = null;

    long pwd;
    static long total_cash;
    Scanner sc = new Scanner(System.in);

    //constructor for initializing the ATM
    ATM_CS(long pwd, long cash) {
        this.pwd = pwd;
        total_cash = cash;
    }

    //method that keeps the ATM in the working mode
    void working() throws SQLException {
        while(true) {
            System.out.println("press 1 if admin");
            System.out.println("press 2 if customer");
            int option = sc.nextInt();


            //admin : shutdown the ATM , add cash to the ATM, check the total cash, add customer, display all the customers
            if (option == 1) {
                System.out.println("Enter the password: ");
                long p = sc.nextLong();

                //checks whether the admin password is correct or not
                if (p == pwd) {

                    while(true) {
                        //functionalities of the admin
                        System.out.println("press 0 to shutdown the system");
                        System.out.println("press 1 to add cash");
                        System.out.println("press 2 to get the cash inside the ATM");
                        System.out.println("press 3 to add Customer into the database");
                        System.out.println("press 4 to display all the customers");

                        int chose = sc.nextInt();
                        if (chose == 0) {
                            return;
                        } else if (chose == 1) {
                            System.out.println("Enter the amount: ");
                            long add = sc.nextLong();
                            total_cash += add;
                            System.out.println("Cash added successfully");
                        } else if (chose == 2) {
                            System.out.println("Remaining cash inside the ATM = " + total_cash);
                        } else if (chose == 3) {
                            // db = new database();
                            System.out.println("Enter the name of the customer");
                            String name = sc.next();
                            System.out.println("Enter the phone number of the customer");
                            long ph = sc.nextLong();
                            System.out.println("Enter the opening balance");
                            long o_amount = sc.nextLong();
                            System.out.println("Enter the ifsc code");
                            String ifsc = sc.next();
                            Main.db.addUser(name, ph, o_amount, ifsc);
                        }
                        else if(chose == 4){
                            //database db = new database();
                            Main.db.displayUsers();
                        }
                        //to check if the option is invalid
                        else {
                            System.out.println("Invalid Option");
                        }
                        System.out.println("Press 1 if you want to continue");
                        System.out.println("Press any number to logout");
                        int opt = sc.nextInt();
                        if(opt == 1){
                            continue;
                        }
                        if(opt == 0){
                            break;
                        }
                    }
                } else {
                    System.out.println("Wrong Password.... Redirecting to main menu");
                }
            }
            //entering the customer menu
            else if(option == 2){

                boolean login = true;
                while(login){
                    System.out.println("Welcome");
                    System.out.println("Enter your 5-digit Account Number");
                    long acc = sc.nextLong();
                    boolean acc_flag=false;

                    conn = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");
                    stmt = conn.createStatement();
                    res = stmt.executeQuery("SELECT id, Account_Number, fName, PIN, Balance, Phone_Number, IFSC FROM useracc");

                    while( res.next() ){
                        if(res.getInt("Account_Number") == acc){
                            acc_flag = true;
                            break;
                        }
                    }
                    if(!acc_flag){
                        System.out.println("The DataBase does not contain the Account Number please try again...");
                        continue;
                    }

                    System.out.println("Enter your 5-digit PIN number");
                    long pin = sc.nextLong();

                    //checks whether the entered pin matches the original pin or not
                    if(res.getInt("PIN") != pin){
                        System.out.println("This PIN number does not match with the original PIN");
                        continue;
                    }

                    //if matches
                    System.out.println("Welcome " + res.getString("fName"));
                    login = false;
                }

                //functionalities of the customer
                User user = new User(res);

                while(true) {
                    System.out.println("Press 1 to View Your Balance");
                    System.out.println("Press 2 to Deposit Funds");
                    System.out.println("Press 3 to Withdrawl Amount");
                    System.out.println("Press 4 to transfer amount");
                    System.out.println("Press 5 to change your PIN");
                    System.out.println("Press 6 to change Phone Number");

                    int opt = sc.nextInt();

                    //to view balance
                    if(opt == 1){
                        user.view_bal();
                    }
                    //to deposit funds
                    else if(opt == 2){
                        conn.close();
                        user.deposit();
                    }
                    //to withdraw amount
                    else if(opt == 3){
                        conn.close();
                        user.withdraw();
                    }
                    else if(opt == 4){
                        if(conn != null)
                            conn.close();

                        System.out.println("Enter the account number and the ifsc code: ");
                        int acc2 = sc.nextInt();
                        //sc.nextLine();
                        String ifsc = sc.next();
                        boolean acc2_flag = false;

                        Connection conn = DriverManager.getConnection("jdbc:sqlite:BankAcc.db");
                        Statement st = conn.createStatement();
                        res = st.executeQuery("SELECT * FROM useracc");
                        while( res.next() ){
                            if(res.getInt("Account_Number") == acc2){
                                if(res.getString("IFSC").equals(ifsc)){
                                    acc2_flag = true;
                                    break;
                                }
                            }
                        }

                        if(!acc2_flag){
                            System.out.println("The entered account number or IFSC code is wrong. Please try again...");
                            continue;
                        }
                        User_Account u = new User_Account(res.getInt("id"), res.getString("fName"), res.getInt("Account_Number"), res.getInt("PIN")
                                , res.getLong("Balance"), res.getLong("Phone_Number"), res.getString("IFSC"));

                        conn.close();
                        user.transfer(u);
                    }

                    else if(opt == 5){
                        conn.close();
                        user.change_pin();
                    }
                    else if(opt == 6){
                        conn.close();
                        user.change_ph();
                    }

                    else{
                        System.out.println("Invalid Option...");
                    }
                    System.out.println("To Stay logged in to your account press 1");
                    System.out.println("To logout press 0");
                    int sel = sc.nextInt();
                    if(sel == 0)
                        break;
                }
            }
            //to check whether the option is valid or not
            else{
                System.out.println("Invalid Option");
            }
        }
    }
}
